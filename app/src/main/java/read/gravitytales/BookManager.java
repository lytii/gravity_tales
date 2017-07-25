package read.gravitytales;


import android.util.Log;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import read.gravitytales.objects.Chapter;
import read.gravitytales.objects.ObjectBox;
import read.gravitytales.util.ChapterParser;
import read.gravitytales.util.BookNetwork;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Handles setting/getting from cache
 * Also does the network calls if not in cache
 */
public class BookManager {

   //   private static String BASE_NOVEL_URL = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-";
//   private static String BASE_URL = "http://gravitytales.com";
   private int currentChapter = 1;
   private ReadPresenter readPresenter;
   private ObjectBox objectBox;
   private boolean loading = false;
   BookNetwork bookNetwork;

   public BookManager(ReadPresenter readPresenter) {
      this.readPresenter = readPresenter;
      objectBox = readPresenter.getObjectBox();
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

   private void fromCacheOrNetwork(int number) {
      loading = true;
      Chapter chapter = objectBox.queryChapter(number);
      if (chapter == null) {
         bookNetwork.getSSNChapter(number)
                    .map(ChapterParser::parse)
                    .map(chapterStrings -> objectBox.storeChapter(chapterStrings, number))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::displayChapter, this::makeErrorToast);
      } else {
         displayChapter(chapter);
      }
   }

   public void preLoadNextChapter() {
      int number = currentChapter + 1;
      Chapter chapter = objectBox.queryChapter(number);
      if (chapter == null) {
         loading = true;
         bookNetwork.getSSNChapter(number)
                    .map(ChapterParser::parse)
                    .subscribe(chapterStrings -> storeChapter(chapterStrings, number),
                               this::makeErrorToast);
      }
   }

   private void displayChapter(Chapter chapter) {
      currentChapter = chapter.getChapterNumber();
      readPresenter.bookmarkChapter(currentChapter);
      readPresenter.displayChapter(chapter);
      loading = false;
   }

   private void storeChapter(ArrayList<String> chapterStrings, int number) {
      objectBox.storeChapter(chapterStrings, number);
      loading = false;
   }

   private void makeErrorToast(Throwable throwable) {
      loading = false;
      readPresenter.makeErrorToast(throwable);
   }

   public void jumpToChapter(int chapter) {
      if (!loading) {
         fromCacheOrNetwork(chapter);
      } else {

         makeErrorToast(new Throwable("Still loading"));
      }
   }

   public void showNextChapter() {
      jumpToChapter(currentChapter + 1);
   }

   public void showPrevChapter() {
      jumpToChapter(currentChapter - 1);
   }
}
