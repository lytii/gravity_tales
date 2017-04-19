package read.gravitytales;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Network {
   private static String BASE_NOVEL_URL = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-";
   private static String NEXT_CHAPTER_CSS = "[href]:contains(Next Chapter)";
   private static String PREV_CHAPTER_CSS = "[href]:contains(Previous Chapter)";
   private static String BASE_URL = "http://gravitytales.com";

   private static Document toParse;
   private static String novelUrl;

   public Network(String novelUrl) {
      this.novelUrl = novelUrl;
   }

   public Network() {
      novelUrl = "/novel/the-experimental-log-of-the-crazy-lich/elcl-chapter-111";
   }

   public Elements getChapter(int chapterNumber) throws IOException {
      toParse = Jsoup.connect(BASE_URL + BASE_NOVEL_URL + chapterNumber).get();
      return Network.toParse.select("p");
   }

   public Elements getChapter() throws IOException {
      toParse = Jsoup.connect(BASE_URL + novelUrl).get();
      return Network.toParse.select("p");
   }

   public Elements nextChapter() throws IOException {
      novelUrl = toParse.select(NEXT_CHAPTER_CSS).attr("href");
      return getChapter();
   }

   public Elements prevChapter() throws IOException {
      novelUrl = toParse.select(PREV_CHAPTER_CSS).attr("href");
      return getChapter();
   }
}
