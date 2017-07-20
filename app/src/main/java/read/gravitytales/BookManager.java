package read.gravitytales;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import read.gravitytales.objects.Chapter;
import read.gravitytales.objects.ObjectBox;
import read.gravitytales.util.ChapterParser;
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
   private ReadPresenter readPresenter;
   private ObjectBox objectBox;
   private boolean loading = false;
   WuxiaService wuxiaService;

   public BookManager(ReadPresenter readPresenter) {
      this.readPresenter = readPresenter;
      objectBox = readPresenter.getObjectBox();
      Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://www.wuxiaworld.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build();
      wuxiaService = retrofit.create(WuxiaService.class);
   }

   public int getCurrentChapter() {
      return currentChapter;
   }

   public void changeBook(String bookUrl) {
//      this.bookUrl = bookUrl;
   }

   private void fromCacheOrNetwork(int number) {
      Chapter chapter = objectBox.queryChapter(number);
      if (chapter == null) {
         loading = true;
         wuxiaService.getSSNChapter(number)
                     .map(ChapterParser::parse)
                     .map(chapterStrings -> objectBox.storeChapter(chapterStrings, number))
                     .doOnNext(IGNORE -> loading = false)
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(this::displayChapter, readPresenter::makeErrorToast);
      } else {
         displayChapter(chapter);
      }
   }

   public void preLoadNextChapter() {
      int number = currentChapter + 1;
      Chapter chapter = objectBox.queryChapter(number);
      if (chapter == null) {
         loading = true;
         wuxiaService.getSSNChapter(number)
                     .map(ChapterParser::parse)
                     .doOnNext(IGNORE -> loading = false)
                     .subscribe(chapterStrings -> objectBox.storeChapter(chapterStrings, number));
      }
   }

   public void jumpToChapter(int chapter) {
      fromCacheOrNetwork(chapter);
   }

   private void displayChapter(Chapter chapter) {
      readPresenter.bookmarkChapter(currentChapter);
      readPresenter.displayChapter(chapter);
   }

   public void showNextChapter() {
      if (!loading) {
         fromCacheOrNetwork(++currentChapter);
      }
   }

   public void showPrevChapter() {
      if (!loading) {
         fromCacheOrNetwork(--currentChapter);
      }
   }
}
