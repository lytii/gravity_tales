package read.gravitytales;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

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

   public ReadPresenter(ReadActivity readActivity) {
      this.readActivity = readActivity;
      initializeStuff(); // using readActivity

      // Load last seen chapter
      int currentChapter = sharedPreferences.getInt("Current Chapter", 58);
      bookManager.jumpToChapter(currentChapter);
      setScroll();
   }

   public void changeBook(String bookUrl) {
      bookManager.changeBook(bookUrl);
   }

   /**
    * Tells readActivity to set the chapter with new adapter
    *
    * @param chapter
    */
   public void displayChapter(ChapterListingParagraphs chapter) {
      chapterAdapter = new ChapterAdapter(chapter.getParagraphs());
      readActivity.displayChapter(chapterAdapter);

      String title = chapter.getParagraphs().get(0).getText();
      if (title.contains("Next Chapter") || title.contains("Previous Chapter")) {
         title = chapter.getParagraphs().get(1).getText();
      }
      // if chapter number isn't in title, prepend it
      if (!title.contains("" + bookManager.getCurrentChapter())) {
         title = bookManager.getCurrentChapter() + title;
      }

      readActivity.setTitle(Html.fromHtml(title));
      readActivity.doneLoading();

   }

   /**
    * Saves chapterNumber to load later
    *
    * @param chapterNumber
    */
   public void bookmarkChapter(int chapterNumber) {
      sharedPreferences.edit().putInt("Current Chapter", chapterNumber).apply();
   }

   public void markScroll(int scrollPosition) {
      sharedPreferences.edit().putInt("Current Scroll", scrollPosition).apply();
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
      readActivity.showLoading();
      bookManager.showNextChapter();
   }

   /**
    * Used when clicking prev button
    * to show prev chapter
    */
   @OnClick(R.id.prev_button)
   public void showPrevChapter() {
      readActivity.showLoading();
      bookManager.showPrevChapter();
   }

   public void preLoadNextChapter() {
      bookManager.isChapterInCache();
   }

   public void jumpToChapter(int chapter) {
      readActivity.showLoading();
      bookManager.jumpToChapter(chapter);
   }

   public void makeErrorToast(Throwable throwable) {
      readActivity.doneLoading();
      Log.d(TAG, "makeErrorToast: " + throwable);
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

   public void showLoading() {
      readActivity.showLoading();
   }

   public void doneLoading() {
      readActivity.doneLoading();
   }
}
