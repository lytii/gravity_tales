package read.gravitytales;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class Network {
   private static String BASE_NOVEL_URL = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-";
   private static String BASE_URL = "http://gravitytales.com";
   private int chapterNumber;
   private static Document toParse;
   ReadPresenter callback;

   public void setCallBack(ReadPresenter callback) {
      this.callback = callback;
   }

   public void addChapter(int chapterNumber) {
      this.chapterNumber = chapterNumber;
      startSetTask();
   }

   private void startSetTask() {
      SetTask dt = new SetTask();
      dt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, chapterNumber);
   }

   private class SetTask extends AsyncTask<Integer, Void, Elements> {
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
         callback.putChapter(chapterItems);
         callback.getChapter();
      }
   }

   private Elements connect(Integer[] integers) throws IOException {
      toParse = Jsoup.connect(BASE_URL + BASE_NOVEL_URL + integers[0]).get();
      return Network.toParse.select("p");
   }
}
