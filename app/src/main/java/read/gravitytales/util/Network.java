package read.gravitytales.util;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import read.gravitytales.BookManager;

import static android.content.ContentValues.TAG;

public class Network {
   private static String BASE_NOVEL_URL = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-";
   private static String BASE_URL = "http://gravitytales.com";
   private BookManager callback;

   public Network(BookManager callback) {
      this.callback = callback;
   }

   public void loadChapterFromNetwork(int chapterNumber) {
      Log.d(TAG, "loadChapterFromNetwork: ");
      LoadChapterTask dt = new LoadChapterTask();
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
      Document toParse = Jsoup.connect(BASE_URL + BASE_NOVEL_URL + integers[0]).get();
      return toParse.select("p");
   }
}
