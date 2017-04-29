package read.gravitytales;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.select.Elements;

import java.util.ArrayList;

import read.gravitytales.BookObjects.Chapter;
import read.gravitytales.BookObjects.ObjectBox;
import read.gravitytales.BookObjects.Paragraph;

import static android.content.ContentValues.TAG;

public class ReadPresenter {

   ReadActivity readActivity;
   ChapterAdapter chapterAdapter;
   ObjectBox objectBox;
   Boolean isLoading = false;

   Network network;
   int currentChapter = 112;

   public ReadPresenter(ReadActivity readActivity) {
      this.readActivity = readActivity;
      chapterAdapter = new ChapterAdapter(new ArrayList<Paragraph>());
      network = new Network();
      network.setCallBack(this);
      objectBox = new ObjectBox(readActivity);
   }

   /**
    * Save current chapter marker in shared prefs
    */
   public void markCurrentChapter() {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
      sharedPreferences.edit()
                       .putInt("currentChapter", currentChapter)
                       .apply();
      Toast.makeText(readActivity, "currentChapter " + currentChapter, Toast.LENGTH_SHORT);
   }


   /**
    * Load current chapter marker from shared prefs
    */
   public void getCurrentChapterMarker() {
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(readActivity);
      currentChapter = sharedPreferences.getInt("currentChapter", 111);
   }

   /**
    * Get chapter from local database
    * or do network request and save chapter in database
    * callback this to then get/set chapter
    */
   public void getChapter() {
      Chapter chapter = objectBox.queryChapter(currentChapter);
      if (chapter == null) {
         isLoading = true;
         network.addChapter(currentChapter);
      } else {
         isLoading = false;
         readActivity.setChapter(chapter);
         markCurrentChapter();
      }
   }

   public void jumpToChapter(int chapterNumber) {
      this.currentChapter = chapterNumber;
      getChapter();
   }

   public void nextChapter() {
      ++currentChapter;
      getChapter();
   }

   public void prevChapter() {
      --currentChapter;
      getChapter();
   }

   public void putChapter(Elements chapterItems) {
      objectBox.putChapter(chapterItems, currentChapter);
   }

   public boolean getIsLoading() {
      return isLoading;
   }
}
