package read.gravitytales;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.objectbox.android.AndroidScheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import read.gravitytales.objects.Chapter;
import read.gravitytales.objects.ObjectBox;
import read.gravitytales.util.ChapterParser;
import read.gravitytales.util.Network;
import read.gravitytales.util.WuxiaService;
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
   private int loadChapterNumber;
   private ReadPresenter readPresenter;
   private ObjectBox objectBox;
   private Network network;
   private boolean willShow;
   private boolean loading = false;

   public BookManager(ReadPresenter readPresenter) {
      this.readPresenter = readPresenter;
      objectBox = readPresenter.getObjectBox();
      network = new Network(this);
   }

   public int getCurrentChapter() {
      return currentChapter;
   }

   private void showChapter(int number) {
      willShow = true;
      fromCacheOrNetwork(number);
   }

   public void changeBook(String bookUrl) {
//      this.bookUrl = bookUrl;
   }

   private void fromCacheOrNetwork(int number) {
      loadChapterNumber = number;
      Chapter chapter = objectBox.queryChapter(number);
      if (chapter == null) {
         loading = true;
         Retrofit retrofit = new Retrofit.Builder()
               .baseUrl("http://www.wuxiaworld.com/")
               .addConverterFactory(GsonConverterFactory.create())
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .build();
         WuxiaService wuxiaService = retrofit.create(WuxiaService.class);
         ArrayList<String> list;
         wuxiaService.getSSNChapter(number)
                     .subscribeOn(Schedulers.newThread())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(responseBody -> {
                        loadChapter(ChapterParser.parse(Jsoup.parse(responseBody.string())));
                     });
      } else {
         displayChapter(chapter);
      }
   }

   public void preLoadNextChapter() {
      fromCacheOrNetwork(currentChapter + 1);
   }

   public void jumpToChapter(int chapter) {
      currentChapter = chapter;
      showChapter(chapter);
   }

   public void loadChapter(ArrayList<String> chapterItems) {
      loading = false;
      objectBox.putChapter(chapterItems, loadChapterNumber);
      if (willShow) {
         displayChapter(objectBox.queryChapter(loadChapterNumber));
      }
   }

   private void displayChapter(Chapter chapter) {
      willShow = false;
      readPresenter.bookmarkChapter(currentChapter);
      readPresenter.displayChapter(chapter);
   }

   public void showNextChapter() {
      if (!loading) {
         showChapter(++currentChapter);
      }
   }

   public void showPrevChapter() {
      if (!loading) {
         showChapter(--currentChapter);
      }
   }
}
