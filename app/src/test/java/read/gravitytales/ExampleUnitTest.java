package read.gravitytales;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest {
   static String novelUrl = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapterNumber-111";
   static String prevNovelUrl = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapterNumber-110";
   String title110 = "Chapter 110: Dragon Slayer and True Love";
   String text110 = "“‘Dragon Slayer’ Bastlar. Is the Goddess of the Moon crazy? Why would she send this mad dog out?”";
   String title111 = "Chapter 111: The Arrival of the Storm";
   String text111 = "The carriages on the road progress slowly. Suddenly, a thunderbolt strikes " +
         "across the sky. Then, dark clouds start to gather and the sky darkens. A moment later, rain starts to fall.";
   BoxStore boxStore;
   int chapterNumber = 111;

   @Test
   public void getChapter111Number() throws IOException {
      Network network = new Network();
      Elements paragraphs = network.connect(chapterNumber);
      boxStore = MyObjectBox.builder().androidContext(this).build();
      putChapter(paragraphs);
      queryChapter(chapterNumber);
   }

   public void putChapter(Elements chapterItems) {
      // Make new chapter
      Chapter chapter = new Chapter();
      chapter.setChapterNumber(chapterNumber);
      boxStore.boxFor(Chapter.class).put(chapter);

      // Make new paragraph list
      List<Paragraph> paragraphs = chapter.getParagraphs();
      Box<Paragraph> paragraphBox = boxStore.boxFor(Paragraph.class);

      for (Element item : chapterItems) {
         Paragraph newParagraph = new Paragraph();
         newParagraph.setParagraphText(item.text());
         newParagraph.setChapterId(chapter.getChapterId());

         paragraphBox.put(newParagraph);
         paragraphs.add(newParagraph);
      }
   }

   public void queryChapter(int chapterNumber) {

      Box<Paragraph> paragraphBox = boxStore.boxFor(Paragraph.class);
      Box<Chapter> chapterBox = boxStore.boxFor(Chapter.class);
      List<Chapter> chapter = chapterBox.query().equal(Chapter_.chapterNumber, chapterNumber).build().find();
      System.out.println("Chapter size " + chapter.size());
   }
}