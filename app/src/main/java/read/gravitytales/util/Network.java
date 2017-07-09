package read.gravitytales.util;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import read.gravitytales.BookManager;

import static android.content.ContentValues.TAG;

public class Network {
   private BookManager callback;
   private String bookUrl = "https://www.wuxiaworld.com/ssn-index/ssn-chapter-";

   public Network(BookManager callback) {
      this.callback = callback;
   }

   public Network() {

   }

   public Network(String bookUrl) {
      this.bookUrl = bookUrl;
   }

   public void loadChapterFromNetwork(String bookUrl, int chapterNumber) {
      Log.d(TAG, "loadChapterFromNetwork: ");
      LoadChapterTask dt = new LoadChapterTask();
      this.bookUrl = bookUrl;
      dt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, chapterNumber);
   }

   private class LoadChapterTask extends AsyncTask<Integer, Void, ArrayList<String>> {
      @Override
      protected ArrayList<String> doInBackground(Integer... integers) {
         try {
            return connect(integers);
         } catch (Exception e) {
            e.printStackTrace();
         }
         return null;
      }

      @Override
      protected void onPostExecute(ArrayList<String> chapterItems) {
         callback.loadChapter(chapterItems);
      }
   }

   public ArrayList<String> connect(Integer[] integers) throws Exception {
      Document toParse = Jsoup.connect(bookUrl + integers[0]).get();
      return ChapterParser.parse(toParse);
   }
}
