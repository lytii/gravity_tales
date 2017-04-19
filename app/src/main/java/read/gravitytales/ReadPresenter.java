package read.gravitytales;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jsoup.select.Elements;

public class ReadPresenter {
   ReadActivity readActivity;
   final Network network = new Network();
   int chapter = 112;

   public ReadPresenter(ReadActivity readActivity) {
      this.readActivity = readActivity;
   }

   public void saveChapter() {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
      sharedPreferences.edit()
                       .putInt("Chapter", chapter)
                       .apply();
   }

   public void loadChapter() {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
      chapter = sharedPreferences.getInt("Chapter", 111);
   }

   public void getChapter() {
      new AsyncTask<Object, Object, Elements>() {
         @Override
         protected Elements doInBackground(Object... voids) {
            try {
               return network.getChapter(chapter);
            } catch (Exception e) {

               e.printStackTrace();
            }
            return null;
         }

         @Override
         protected void onPostExecute(Elements chapterItems) {
            Log.d("ReadPresenter", "loaded chapter");
            readActivity.setChapterRecyclerView(chapterItems);
         }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
   }

   public void getChapter(final int chapterNumber) {
      chapter = chapterNumber;
      new AsyncTask<Object, Object, Elements>() {
         @Override
         protected Elements doInBackground(Object... voids) {
            try {
               return network.getChapter(chapterNumber);
            } catch (Exception e) {

               e.printStackTrace();
            }
            return null;
         }

         @Override
         protected void onPostExecute(Elements chapterItems) {
            Log.d("ReadPresenter", "loaded chapter");
            readActivity.setChapterRecyclerView(chapterItems);
         }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
   }

   public void nextChapter() {
      chapter++;
      new AsyncTask<Object, Object, Elements>() {
         @Override
         protected Elements doInBackground(Object... voids) {
            try {
               return network.nextChapter();
            } catch (Exception e) {

               e.printStackTrace();
            }
            return null;
         }

         @Override
         protected void onPostExecute(Elements chapterItems) {
            Log.d("ReadPresenter", "loaded chapter");
            readActivity.setChapterRecyclerView(chapterItems);
         }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
   }

   public void prevChapter() {
      chapter--;
      new AsyncTask<Object, Object, Elements>() {
         @Override
         protected Elements doInBackground(Object... voids) {
            try {
               return network.prevChapter();
            } catch (Exception e) {

               e.printStackTrace();
            }
            return null;
         }

         @Override
         protected void onPostExecute(Elements chapterItems) {
            Log.d("ReadPresenter", "loaded chapter");
            readActivity.setChapterRecyclerView(chapterItems);
         }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
   }

   public void getChapterDRY() {
      new AsyncTask<Object, Object, Elements>() {
         @Override
         protected Elements doInBackground(Object... voids) {
            try {
               return network.prevChapter();
            } catch (Exception e) {

               e.printStackTrace();
            }
            return null;
         }

         @Override
         protected void onPostExecute(Elements chapterItems) {
            Log.d("ReadPresenter", "loaded chapter");
            readActivity.setChapterRecyclerView(chapterItems);
         }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
   }

   public void prevChapterDRY() {
      chapter--;

   }
}
