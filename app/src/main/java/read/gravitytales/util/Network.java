package read.gravitytales.util;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.IOException;

import read.gravitytales.BookManager;

import static android.content.ContentValues.TAG;

public class Network {
   private BookManager callback;
   private String bookUrl = "http://www.wuxiaworld.com/emperorofsoloplay-index/esp-chapter-";

   public Network(BookManager callback) {
      this.callback = callback;
   }

   public Network() {

   }

   public void loadChapterFromNetwork(String bookUrl, int chapterNumber) {
      Log.d(TAG, "loadChapterFromNetwork: ");
      LoadChapterTask dt = new LoadChapterTask();
      this.bookUrl = bookUrl;
      dt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, chapterNumber);
   }

   private class LoadChapterTask extends AsyncTask<Integer, Void, Elements> {
      @Override
      protected Elements doInBackground(Integer... integers) {
         try {
            return connect(integers);
         } catch (IOException e) {
            e.printStackTrace();
         }
         return null;
      }

      @Override
      protected void onPostExecute(Elements chapterItems) {
         callback.loadChapter(chapterItems);
      }
   }

   public Elements connect(Integer[] integers) throws IOException {
      Document toParse = Jsoup.connect(bookUrl + integers[0]).get();
      Elements selected = toParse.select("[dir]");
      // When easy selector doesn't work
      if (selected.size() == 0) {
         System.out.println("NOT EASY");
         selected = toParse.select("p");

         selected.remove(0);
         Elements title = selected.get(0).select("span");
         for (Element a : title) {
            // If Next/Previous Chapter is first chapter item, remove it
            if (!a.text().contains("Next Chapter") && !a.text().contains("Previous Chapter")) {
               selected.set(0, a);
               System.out.println("NEXT CHAPTER IS THERE");
            }
         }
         // Remove item single item
         String prevNext = selected.get(0).text();
         if (prevNext.contains("Next Chapter") | prevNext.contains("Previous Chapter")) {
            System.out.println("\nremoving: " + selected.get(0));
            selected.remove(0);
         }
         // Remove space after chapter title
         if (selected.get(1).text().startsWith("Chapter") || selected.get(1).text().charAt(0) == 160) {
            selected.remove(1);
         }
         String NEXT_PREV = "Previous Chapter Next Chapter";
         while (!selected.get(selected.size() - 1).text().contains(NEXT_PREV)) {
            selected.remove(selected.size() - 1);
         }
         int LAST_ITEM = selected.size() - 1;
         if (selected.get(LAST_ITEM).text().contains(NEXT_PREV))
            selected.set(LAST_ITEM, selected.get(LAST_ITEM).getElementsByIndexEquals(0).get(0));
      } else {
         System.out.println(selected.get(1));
         System.out.println("TEXT: " + selected.get(1).text());
         // Removes excess NBSP and Chapter Titles
         while(selected.get(1).text().charAt(0) == 160) {
            selected.remove(1);
         }
      }
      return selected;
   }
}
