package read.gravitytales.objects;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

import static android.content.ContentValues.TAG;

/**
 * wrapper class to use objectbox
 */
public class ObjectBox {
   BoxStore boxStore;

   public ObjectBox(Activity activity) {
      boxStore = MyObjectBox.builder().androidContext(activity).build();
   }

   /**
    * Adds new chapter to chapter box/paragraph box
    */
   public Chapter storeChapter(ArrayList<String> chapterItems, int chapterNumber) {
      // Make new chapter
      Chapter chapter = new Chapter();
      chapter.setChapterNumber(chapterNumber);
      boxStore.boxFor(Chapter.class).put(chapter);

      // Make new paragraph list
      List<Paragraph> paragraphs = chapter.getParagraphs();
      Box<Paragraph> paragraphBox = boxStore.boxFor(Paragraph.class);

      // add paragraphs to box
      for (String item : chapterItems) {
         Paragraph newParagraph = new Paragraph();
         newParagraph.setParagraphText(item);
         newParagraph.setChapterId(chapter.getChapterId());

         paragraphBox.put(newParagraph);
         paragraphs.add(newParagraph);
      }
      Log.d(TAG, "storeChapter: " + chapterNumber);
      return chapter;
   }

   /**
    * Get chapter from chapter box
    * returns null if not cached
    */
   public Chapter queryChapter(int chapterNumber) {
      Box<Chapter> chapterBox = boxStore.boxFor(Chapter.class);
      List<Chapter> chapterList = chapterBox.query()
                                            .equal(Chapter_.chapterNumber, chapterNumber)
                                            .build()
                                            .find();
      if (chapterList.size() == 0)
         return null;
      return chapterList.get(0);
   }

   public void clear() {
      Box<Chapter> chapterBox = boxStore.boxFor(Chapter.class);
      chapterBox.removeAll();
   }
}
