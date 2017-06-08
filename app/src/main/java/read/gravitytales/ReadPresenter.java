package read.gravitytales;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;

import read.gravitytales.objects.Chapter;
import read.gravitytales.objects.ObjectBox;

public class ReadPresenter {

   private ReadActivity readActivity;
   private BookManager bookManager;
   private ObjectBox objectBox;
   private ChapterAdapter chapterAdapter;

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
      objectBox.clear();
   }

   /**
    * Tells readActivity to set the chapter with new adapter
    *
    * @param chapter
    */
   public void displayChapter(Chapter chapter) {
      chapterAdapter = new ChapterAdapter(chapter.getParagraphs());
      readActivity.displayChapter(chapterAdapter);
      String title = chapter.getParagraphs().get(0).getParagraphText();
      if(title.contains("Next Chapter") || title.contains("Previous Chapter")) {
         title = chapter.getParagraphs().get(1).getParagraphText();
      }
      // if chapter number isn't in title, prepend it
      if(!title.contains("" + bookManager.getCurrentChapter())) {
         title = bookManager.getCurrentChapter() + title;
      }
      readActivity.setTitle(Html.fromHtml(title));
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
      objectBox = new ObjectBox(readActivity);
      bookManager = new BookManager(this);
      sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
   }

   /**
    * Used when clicking next button
    * to show next chapter
    */
   public void showNextChapter() {
      bookManager.showNextChapter();
   }

   /**
    * Used when clicking prev button
    * to show prev chapter
    */
   public void showPrevChapter() {
      bookManager.showPrevChapter();
   }

   public void preLoadNextChapter() {
      bookManager.preLoadNextChapter();
   }

   public void jumpToChapter(int chapter) {
      bookManager.jumpToChapter(chapter);
   }

   public ObjectBox getObjectBox() {
      return objectBox;
   }
}
