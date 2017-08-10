package read.gravitytales.objects;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChapterListingParagraphs {
   @Embedded
   public Chapter chapter;

   @Relation(parentColumn = "id", entityColumn = "chapterId", entity = Paragraph.class)
   private List<Paragraph> paragraphs;

   public int getChapterNumber() {
      return chapter.getNumber();
   }

   public List<Paragraph> getParagraphs() {
      return paragraphs;
   }

   public void setParagraphs(List<Paragraph> paragraphs) {
      this.paragraphs = paragraphs;
   }

   public ArrayList<String> getText() {
      ArrayList<String> listOfText = new ArrayList<>();
      for (Paragraph paragraph : paragraphs) {
         listOfText.add(paragraph.text);
      }
      return listOfText;
   }

   public static ChapterListingParagraphs putInOrder(ChapterListingParagraphs chapter) {
      Collections.sort(chapter.getParagraphs(), new Comparator<Paragraph>() {
         @Override
         public int compare(Paragraph o1, Paragraph o2) {
            return o1.getPosition() - o2.getPosition();
         }

      });
      return chapter;
   }
}
