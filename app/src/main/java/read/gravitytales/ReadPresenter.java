package read.gravitytales;

import read.gravitytales.objects.Chapter;

public class ReadPresenter {

   ReadActivity readActivity;
   BookManager bookManager;

   public ReadPresenter(ReadActivity readActivity) {
      this.readActivity = readActivity;
      bookManager = new BookManager(this);
      bookManager.getCurrentChapter();
   }

   public void displayChapter(Chapter chapter) {
      readActivity.displayChapter(new ChapterAdapter(chapter.getParagraphs()));
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
}
