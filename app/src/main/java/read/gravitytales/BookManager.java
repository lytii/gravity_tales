package read.gravitytales;


import java.util.ArrayList;

import read.gravitytales.objects.Chapter;
import read.gravitytales.objects.ObjectBox;
import read.gravitytales.util.Network;

/**
 * Handles setting/getting from cache
 * Also does the network calls if not in cache
 */
public class BookManager {

   //   private static String BASE_NOVEL_URL = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-";
//   private static String BASE_URL = "http://gravitytales.com";
   private String bookUrl = "http://www.wuxiaworld.com/emperorofsoloplay-index/esp-chapter-";
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
      this.bookUrl = bookUrl;
   }

   private void fromCacheOrNetwork(int number) {
      loadChapterNumber = number;
      Chapter chapter = objectBox.queryChapter(number);
      if (chapter == null) {
         loading = true;
         network.loadChapterFromNetwork(bookUrl, number);
      } else if(willShow){
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
