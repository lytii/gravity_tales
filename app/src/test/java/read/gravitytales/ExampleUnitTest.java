package read.gravitytales;

import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest {
   static String novelUrl = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-111";
   static String prevNovelUrl = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-110";
   String title110 = "Chapter 110: Dragon Slayer and True Love";
   String text110 = "“‘Dragon Slayer’ Bastlar. Is the Goddess of the Moon crazy? Why would she send this mad dog out?”";
   String title111 = "Chapter 111: The Arrival of the Storm";
   String text111 = "The carriages on the road progress slowly. Suddenly, a thunderbolt strikes " +
         "across the sky. Then, dark clouds start to gather and the sky darkens. A moment later, rain starts to fall.";

   @Test
   public void getChapter111Number() throws IOException {
      Network network = new Network();
      Elements paragraphs = network.getChapter(111);
      assertEquals(paragraphs.get(1).text(), title111);
      assertEquals(paragraphs.get(2).text(), text111);
   }

   @Test
   public void getChapter111() throws IOException {
      Network network = new Network(novelUrl);
      Elements paragraphs = network.getChapter();
      assertEquals(paragraphs.get(1).text(), title111);
      assertEquals(paragraphs.get(2).text(), text111);
   }

   @Test
   public void getChapter110Next() throws IOException {
      Network network = new Network(prevNovelUrl);
      Elements paragraphs = network.getChapter();
      assertEquals(paragraphs.get(1).text(), title110);
      assertEquals(paragraphs.get(2).text(), text110);
      paragraphs = network.nextChapter();
      assertEquals(paragraphs.get(1).text(), title111);
      assertEquals(paragraphs.get(2).text(), text111);
   }

   @Test
   public void getChapter110Prev() throws IOException {
      Network network = new Network(novelUrl);
      Elements paragraphs = network.getChapter();
      assertEquals(paragraphs.get(1).text(), title111);
      assertEquals(paragraphs.get(2).text(), text111);
      paragraphs = network.prevChapter();
      assertEquals(paragraphs.get(1).text(), title110);
      assertEquals(paragraphs.get(2).text(), text110);
   }
}