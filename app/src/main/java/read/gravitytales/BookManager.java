package read.gravitytales;

import android.util.Log;

import org.jsoup.select.Elements;

import read.gravitytales.objects.Chapter;
import read.gravitytales.objects.ObjectBox;
import read.gravitytales.util.Network;

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
      Log.d(TAG, "BookManager: ");
      this.readPresenter = readPresenter;
      objectBox = readPresenter.getObjectBox();
      network = new Network(this);
   }

   private void showChapter(int number) {
      Log.d(TAG, "showChapter: ");
      willShow = true;
      fromCacheOrNetwork(number);
   }

   private void fromCacheOrNetwork(int number) {
      Log.d(TAG, "fromCacheOrNetwork: ");
      loadChapterNumber = number;
      Chapter chapter = objectBox.queryChapter(number);
      if (chapter == null) {
         loading = true;
         network.loadChapterFromNetwork(number);
      } else {
         displayChapter(chapter);
      }
   }

   public void preLoadNextChapter() {
      Log.d(TAG, "preLoadNextChapter: ");
      fromCacheOrNetwork(currentChapter + 1);
   }

   public void jumpToChapter(int chapter) {
      Log.d(TAG, "jumpToChapter: ");
      currentChapter = chapter;
      showChapter(chapter);
   }

   public void loadChapter(Elements chapterItems) {
      Log.d(TAG, "loadChapter:  willshow " + willShow);
      loading = false;
      objectBox.putChapter(chapterItems, loadChapterNumber);
      if (willShow) {
         displayChapter(objectBox.queryChapter(loadChapterNumber));
      }
   }

   private void displayChapter(Chapter chapter) {
      Log.d(TAG, "displayChapter: ");
      willShow = false;
      readPresenter.bookmarkChapter(currentChapter);
      readPresenter.displayChapter(chapter);
   }

   public void showNextChapter() {
      Log.d(TAG, "showNextChapter: ");
      if (!loading) {
         showChapter(++currentChapter);
      }
   }

   public void showPrevChapter() {
      Log.d(TAG, "showPrevChapter: ");
      if (!loading) {
         showChapter(--currentChapter);
      }
   }
}
