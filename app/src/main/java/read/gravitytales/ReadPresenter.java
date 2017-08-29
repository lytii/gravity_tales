package read.gravitytales;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;
import read.gravitytales.objects.ChapterDAO;
import read.gravitytales.objects.ChapterDatabase;
import read.gravitytales.objects.ChapterListingParagraphs;

import static android.content.ContentValues.TAG;

public class ReadPresenter {

   private ReadActivity readActivity;
   private BookManager bookManager;
   private ChapterAdapter chapterAdapter;
   private ChapterDAO chapterDAO;
   private SharedPreferences sharedPreferences;

   private boolean loading = false;

   public ReadPresenter(ReadActivity readActivity) {
      this.readActivity = readActivity;
      initializeStuff(); // using readActivity

      // Load last seen chapter
      int currentChapter = sharedPreferences.getInt(readActivity.getString(R.string.chapter_store), 161);
      String currentBookUrl = sharedPreferences.getString(readActivity.getString(R.string.book_url_store), "https://www.wuxiaworld.com/ssn-index/ssn-chapter-");
      bookManager.changeBook(currentBookUrl, currentChapter);
      setScroll();
   }

   public void changeBook(String bookUrl) {
      startLoadingStatus();
      String[] urlPieces = bookUrl.split("-|/");
      String numberString = urlPieces[urlPieces.length - 1];
      // trim chapter number
      String baseUrl = bookUrl.substring(0, bookUrl.indexOf(numberString));
      bookManager.changeBook(baseUrl, Integer.parseInt(numberString));
   }

   /**
    * Tells readActivity to set the chapter with new adapter
    *
    * @param chapter
    */
   public void displayChapter(ChapterListingParagraphs chapter) {
      chapterAdapter = new ChapterAdapter(chapter.getParagraphs());
      readActivity.displayChapter(chapterAdapter);

      String title = chapter.getParagraphs().remove(0).getText();
      if (title.contains("Next Chapter") || title.contains("Previous Chapter")) {
         title = chapter.getParagraphs().get(1).getText();
      }
      // if chapter number isn't in title, prepend it
      if (!title.contains("" + bookManager.getCurrentChapter())) {
         title = bookManager.getCurrentChapter() + title;
      }

      readActivity.setTitle(Html.fromHtml(title));
      endLoadingStatus();

   }

   /**
    * Saves chapterNumber to load later
    *
    * @param chapterNumber
    */
   public void bookmarkChapter(int chapterNumber) {
      sharedPreferences.edit()
                       .putInt(readActivity.getString(R.string.chapter_store), chapterNumber)
                       .apply();
   }

   public void bookmarkBook(String bookUrl) {
      sharedPreferences.edit()
                       .putString(readActivity.getString(R.string.book_url_store), bookUrl)
                       .apply();
   }

   public void markScroll(int scrollPosition) {
      sharedPreferences.edit()
                       .putInt(readActivity.getString(R.string.scroll_store), scrollPosition)
                       .apply();
   }

   public void setScroll() {
      int scrollPosition = sharedPreferences.getInt("Current Scroll", 0);
      readActivity.setSroll(scrollPosition);
   }

   /**
    * initializes objects we're going to need
    */
   private void initializeStuff() {
      chapterDAO = ChapterDatabase.getINSTANCE(readActivity).chapterDAO();
      bookManager = new BookManager(this);
      sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
      ButterKnife.bind(readActivity);
   }

   /**
    * Used when clicking next button
    * to show next chapter
    */
   @OnClick(R.id.next_button)
   public void showNextChapter() {
      if (!loading) {
         bookManager.showNextChapter();
         startLoadingStatus();
      }
   }

   /**
    * Used when clicking prev button
    * to show prev chapter
    */
   @OnClick(R.id.prev_button)
   public void showPrevChapter() {
      if (!loading) {
         bookManager.showPrevChapter();
         startLoadingStatus();
      }
   }

   public void preLoadNextChapter() {
      loading = true;
      bookManager.preLoadChapter();
   }

   public void jumpToChapter(int chapter) {
      if (!loading) {
         startLoadingStatus();
         bookManager.jumpToChapter(chapter);
      }
   }

   public void makeLoadingErrorToast(Throwable throwable) {
      endLoadingStatus();
      Log.d(TAG, "makeLoadingErrorToast: " + Arrays.toString(throwable.getStackTrace()));
      Toast.makeText(readActivity, "Error: " + throwable, Toast.LENGTH_SHORT).show();
   }

   public boolean hasNetwork() {
      ConnectivityManager cm =
            (ConnectivityManager) readActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
      return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
   }

   public ChapterDAO getChapterDao() {
      return chapterDAO;
   }

   public void startLoadingStatus() {
      loading = true;
      readActivity.showLoading();
   }

   public void endLoadingStatus() {
      readActivity.doneLoading();
      loading = false;
   }
}
