package read.gravitytales;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import read.gravitytales.objects.Chapter;
import read.gravitytales.objects.ObjectBox;

import static android.content.ContentValues.TAG;

public class ReadPresenter {

   private ReadActivity readActivity;
   private BookManager bookManager;
   private ObjectBox objectBox;
   private ChapterAdapter chapterAdapter;

   private SharedPreferences sharedPreferences;

   public ReadPresenter(ReadActivity readActivity) {
      Log.d(TAG, "ReadPresenter: ");
      this.readActivity = readActivity;
      initializeStuff(); // using readActivity

      // Load last seen chapter
      int currentChapter = sharedPreferences.getInt("Current Chapter", 140);
      Log.d(TAG, "ReadPresenter: current" + currentChapter);
      bookManager.jumpToChapter(currentChapter);
   }

   /**
    * Tells readActivity to set the chapter with new adapter
    * @param chapter
    */
   public void displayChapter(Chapter chapter) {
      Log.d(TAG, "displayChapter: ");
      chapterAdapter = new ChapterAdapter(chapter.getParagraphs());
      readActivity.displayChapter(chapterAdapter);
   }

   /**
    * Saves chapterNumber to load later
    * @param chapterNumber
    */
   public void bookmarkChapter(int chapterNumber) {
      Log.d(TAG, "bookmarkChapter: ");
      sharedPreferences.edit().putInt("Current Chapter", chapterNumber).apply();
   }

   /**
    * initializes objects we're going to need
    */
   private void initializeStuff() {
      Log.d(TAG, "initializeStuff: ");
      objectBox = new ObjectBox(readActivity);
      bookManager = new BookManager(this);
      sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
   }

   /**
    * Used when clicking next button
    * to show next chapter
    */
   public void showNextChapter() {
      Log.d(TAG, "showNextChapter: ");
      bookManager.showNextChapter();
   }

   /**
    * Used when clicking prev button
    * to show prev chapter
    */
   public void showPrevChapter() {
      Log.d(TAG, "showPrevChapter: ");
      bookManager.showPrevChapter();
   }

   public void preLoadNextChapter() {
      Log.d(TAG, "preLoadNextChapter: ");
      bookManager.preLoadNextChapter();
   }

   public void jumpToChapter(int chapter) {
      Log.d(TAG, "jumpToChapter: ");
      bookManager.jumpToChapter(chapter);
   }

   public ObjectBox getObjectBox() {
      Log.d(TAG, "getObjectBox: ");
      return objectBox;
   }
}
