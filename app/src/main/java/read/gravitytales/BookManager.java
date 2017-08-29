package read.gravitytales;


import org.jsoup.Jsoup;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import read.gravitytales.objects.Book;
import read.gravitytales.objects.Chapter;
import read.gravitytales.objects.ChapterDAO;
import read.gravitytales.objects.ChapterListingParagraphs;
import read.gravitytales.objects.Paragraph;
import read.gravitytales.util.BookNetwork;
import read.gravitytales.util.ChapterParser;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Handles setting/getting from cache
 * Also does the network calls if not in cache
 */
public class BookManager {

   private ReadPresenter readPresenter;
   private ChapterDAO chapterDAO;
   private boolean autoLoad = false;
   BookNetwork bookNetwork;
   Book currentBook;

   private int currentChapter = 1;

   public BookManager(ReadPresenter readPresenter) {
      this.readPresenter = readPresenter;
      chapterDAO = readPresenter.getChapterDao();
      Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://www.wuxiaworld.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build();
      bookNetwork = retrofit.create(BookNetwork.class);
   }

   public int getCurrentChapter() {
      return currentChapter;
   }

   public void changeBook(String bookUrl, int chapter) {
      chapterDAO.getBookByUrl(bookUrl)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> {
                   Book book = new Book();
                   book.setBookUrl(bookUrl);
                   book.setId(new Random().nextInt());
                   chapterDAO.addBook(book);
                   return book;
                })
                .subscribe(book -> {
                   currentBook = book;
                   readPresenter.bookmarkBook(book.getBookUrl());
                   jumpToChapter(chapter);
                });
   }
   /**
    * load chapter from cache to display chapter
    */
   private void loadChapter(int number) {
      chapterDAO.getChapter(number, currentBook.getId())
                .toObservable()
                .subscribeOn(Schedulers.io())
                .switchIfEmpty(
                      bookNetwork.getChapter(currentBook.getBookUrl() + number)
                                 .map(ResponseBody::string)
                                 .map(Jsoup::parse)
                                 .map(ChapterParser::parse)
                                 .flatMapMaybe(paragraphList -> saveChapter(paragraphList, number))
                )
                .map(ChapterListingParagraphs::putInOrder)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayChapter, readPresenter::makeLoadingErrorToast);
   }

   private void displayChapter(ChapterListingParagraphs chapter) {
      currentChapter = chapter.getChapterNumber();
      readPresenter.bookmarkChapter(currentChapter);
      readPresenter.displayChapter(chapter);
   }

   public Maybe<ChapterListingParagraphs> saveChapter(List<String> chapter, int number) {
      Chapter newChapter = new Chapter();
      newChapter.setNumber(number);
      newChapter.setId(currentBook.getId()*10000+number);
      newChapter.setBookId(currentBook.getId());

      int i = 0;
      for (String text : chapter) {
         Paragraph newParagraph = new Paragraph(text);
         newParagraph.setChapterId(newChapter.getId());
         newParagraph.setPosition(i++);
         newParagraph.setId(new Random().nextInt());
         chapterDAO.addParagraph(newParagraph);
      }
      chapterDAO.addChapter(newChapter);
      return chapterDAO.getChapter(number, currentBook.getId());
   }

   public void preLoadChapter() {
      readPresenter.startLoadingStatus();
      int number = currentChapter + 1;
      chapterDAO.getChapter(number, currentBook.getId())
                .toObservable()
                .subscribeOn(Schedulers.io())
                .switchIfEmpty(
                      bookNetwork.getChapter(currentBook.getBookUrl() + number)
                                 .map(ResponseBody::string)
                                 .map(Jsoup::parse)
                                 .map(ChapterParser::parse)
                                 .flatMapMaybe(paragraphList -> saveChapter(paragraphList, number))
                ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(savedChapter -> {
         if (autoLoad) {
            displayChapter(savedChapter);
         } else {
            readPresenter.endLoadingStatus();
         }
      }, readPresenter::makeLoadingErrorToast);
   }

   public void jumpToChapter(int chapter) {
      loadChapter(chapter);
   }

   public void showNextChapter() {
      jumpToChapter(currentChapter + 1);
   }

   public void showPrevChapter() {
      jumpToChapter(currentChapter - 1);
   }
}
