package read.gravitytales;


import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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

   //   private static String BASE_NOVEL_URL = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-";
//   private static String BASE_URL = "http://gravitytales.com";
   private int currentChapter = 1;
   private ReadPresenter readPresenter;
   private ChapterDAO chapterDAO;
   private boolean loading = false;
   private boolean autoLoad = false;
   BookNetwork bookNetwork;

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

   public void changeBook(String bookUrl) {
//      this.bookUrl = bookUrl;
   }

   /**
    * load chapter from cache to display chapter
    */
   private void displayChapterFromCache(int number) {
      loading = true;
      chapterDAO.getChapter(number)
                .subscribeOn(Schedulers.io())
                .map(ChapterListingParagraphs::putInOrder)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayChapter, ignore -> fromNetwork(number));
   }

   /**
    * load chapter from network and store in cache
    * display chapter from cache
    */
   private void fromNetwork(int number) {
      loading = true;
      bookNetwork.getSSNChapter(number)
                 .map(ChapterParser::parse)
                 .map(chapterStrings -> saveChapter(chapterStrings, number))
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(ignore -> displayChapterFromCache(number), this::makeErrorToast);
   }

   private void displayChapter(ChapterListingParagraphs chapter) {
      currentChapter = chapter.getChapterNumber();
      readPresenter.bookmarkChapter(currentChapter);
      readPresenter.displayChapter(chapter);
      loading = false;
   }

   public Single<ChapterListingParagraphs> saveChapter(ArrayList<String> chapter, int number) {
      Chapter newChapter = new Chapter();
      newChapter.setNumber(number);
      newChapter.setId(number);

      int i = 0;
      for (String text : chapter) {
         Paragraph newParagraph = new Paragraph(text);
         newParagraph.setChapterId(newChapter.getId());
         newParagraph.setPosition(i++);
         newParagraph.setId(new Random().nextInt());
         chapterDAO.addParagraph(newParagraph);
      }
      chapterDAO.addChapter(newChapter);
      return chapterDAO.getChapter(number);
   }

   private void preFromNetwork(int number) {
      bookNetwork.getSSNChapter(number)
                 .map(ChapterParser::parse)
                 .map(chapterStrings -> saveChapter(chapterStrings, number))
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(ignore -> {
                    if (autoLoad) {
                       displayChapterFromCache(number);
                       autoLoad = false;
                    }
                    loading = false;
                 }, this::makeErrorToast);
   }

   public void isChapterInCache() {
      loading = true;
      int number = currentChapter + 1;
      chapterDAO.getChapter(number)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> loading = false, ignore -> preFromNetwork(number));
   }

   private void makeErrorToast(Throwable throwable) {
      readPresenter.makeErrorToast(throwable);
      loading = false;
   }

   public void jumpToChapter(int chapter) {
      if (!loading) {
         displayChapterFromCache(chapter);
      } else {
         readPresenter.showLoading();
         autoLoad = true;
      }
   }

   public void showNextChapter() {
      jumpToChapter(currentChapter + 1);
   }

   public void showPrevChapter() {
      jumpToChapter(currentChapter - 1);
   }
}
