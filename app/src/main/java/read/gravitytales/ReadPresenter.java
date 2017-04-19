package read.gravitytales;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.select.Elements;

import static android.content.ContentValues.TAG;

public class ReadPresenter {

   ReadActivity readActivity;
   ChapterAdapter chapterAdapter;

   Network network;
   int chapterNumber = 112;

   public ReadPresenter(ReadActivity readActivity) {
      this.readActivity = readActivity;
      chapterAdapter = new ChapterAdapter(new Elements());
      network = new Network(this);
   }

   public void saveChapter() {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
      sharedPreferences.edit()
                       .putInt("Chapter", chapterNumber)
                       .apply();
      Toast.makeText(readActivity, "Chapter " + chapterNumber, Toast.LENGTH_SHORT);
   }

   public void loadChapter() {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
      chapterNumber = sharedPreferences.getInt("Chapter", 111);
   }

   public void getChapter() {
      network.addChapter(chapterNumber);
   }

   public void getChapter(int chapterNumber) {
      this.chapterNumber = chapterNumber;
      network.addChapter(chapterNumber);
   }

   public void nextChapter() {
      Log.d(TAG, "nextChapter: ");
      network.getChapter(++chapterNumber);
   }

   public void prevChapter() {
      Log.d(TAG, "prevChapter: ");
      network.getChapter(--chapterNumber);
   }

   // set items to new adapter, refreshes view
   public void setItems(Elements chapterItems) {
      readActivity.setChapter(chapterItems);
      saveChapter();
   }

   // adds items to list, adds chapters to view
   public void updateItems(Elements chapterItems) {
      chapterAdapter.addAll(chapterItems);
      chapterAdapter.notifyDataSetChanged();
      saveChapter();
   }

   public ChapterAdapter getChapterAdapter(){
      return chapterAdapter;
   }
}
