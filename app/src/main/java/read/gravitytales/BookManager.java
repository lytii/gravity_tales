package read.gravitytales;

import android.util.Log;

import org.jsoup.select.Elements;

import read.gravitytales.objects.Chapter;
import read.gravitytales.util.Network;
import read.gravitytales.objects.ObjectBox;

import static android.content.ContentValues.TAG;

/**
 * Handles setting/getting from cache
 * Also does the network calls if not in cache
 */
public class BookManager {

   private int currentChapter = 1;
   private int loadChapterNumber;
   private ReadPresenter readPresenter;
   private ObjectBox objectBox;
   private Network network;
   private boolean willShow;
   private boolean loading = false;

   public BookManager(ReadPresenter readPresenter) {
      this.readPresenter = readPresenter;
      objectBox = new ObjectBox(readPresenter.readActivity);
      network = new Network(this);
   }

   private void showChapter(int number) {
      willShow = true;
      fromCacheOrNetwork(number);
   }

   private void fromCacheOrNetwork(int number) {
      Log.d(TAG, "fromCacheOrNetwork: starting" + number);
      Chapter chapter = objectBox.queryChapter(number);
      if(chapter == null) {
         loading = true;
         loadChapterNumber = number;
         network.loadChapterFromNetwork(number);
      } else {
         readPresenter.displayChapter(chapter);
      }
   }

   public void preLoadNextChapter() {
      fromCacheOrNetwork(currentChapter+1);
   }

   public void getCurrentChapter() {
      showChapter(currentChapter);
   }

   public void jumpToChapter(int chapter) {
      showChapter(chapter);
   }

   public void loadChapter(Elements chapterItems) {
      loading = false;
      objectBox.putChapter(chapterItems, loadChapterNumber);
      if(willShow) {
         readPresenter.displayChapter((objectBox.queryChapter(loadChapterNumber)));
      }
   }

   public void showNextChapter() {
      if(!loading) {
         showChapter(++currentChapter);
      }
   }

   public void showPrevChapter() {
      if(!loading) {
         showChapter(--currentChapter);
      }
   }
}
