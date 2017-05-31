package read.gravitytales.util;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

   private class LoadChapterTask extends AsyncTask<Integer, Void, ArrayList<String>> {
      @Override
      protected ArrayList<String> doInBackground(Integer... integers) {
         try {
            return connect(integers);
         } catch (IOException e) {
            e.printStackTrace();
         }
         return null;
      }

      @Override
      protected void onPostExecute(ArrayList<String> chapterItems) {
         callback.loadChapter(chapterItems);
      }
   }

   public ArrayList<String> connect(Integer[] integers) throws IOException {
      Document toParse = Jsoup.connect(bookUrl + integers[0]).get();
      List<Node> all = toParse.select("div#chapterContent").get(0).childNodes();
      ArrayList<String> d = new ArrayList<String>();
      int i = 0;
      for (Node node : all) {
         String string = node.toString();
         string = string.replaceAll("<p[^>]+>|</p>|<p>", "");
         if (!string.equals(" ") && !string.equals("<hr>") && !string.equals("<br>") && !string.equals("&nbsp;")) {
            d.add(string);
         }
      }
      return d;
   }
}
