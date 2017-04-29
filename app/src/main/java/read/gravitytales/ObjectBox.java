package read.gravitytales;

import android.app.Activity;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

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
   public void putChapter(Elements chapterItems, int chapterNumber) {
      // Make new chapter
      Chapter chapter = new Chapter();
      chapter.setChapterNumber(chapterNumber);
      boxStore.boxFor(Chapter.class).put(chapter);

      // Make new paragraph list
      List<Paragraph> paragraphs = chapter.getParagraphs();
      Box<Paragraph> paragraphBox = boxStore.boxFor(Paragraph.class);

      for (int i = 0; i < 2; i++) {
         chapterItems.remove(0);
      }
      // trim chapter items
      for (int i = 0; i < 16; i++) {
         chapterItems.remove(chapterItems.size() - 1);
      }

      // add paragraphs to box
      for (Element item : chapterItems) {
         Paragraph newParagraph = new Paragraph();
         newParagraph.setParagraphText(item.text());
         newParagraph.setChapterId(chapter.getChapterId());

         paragraphBox.put(newParagraph);
         paragraphs.add(newParagraph);
      }
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
}