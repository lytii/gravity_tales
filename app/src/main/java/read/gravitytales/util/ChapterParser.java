package read.gravitytales.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public final class ChapterParser {
   public static ArrayList<String> parse(ResponseBody responseBody) throws IOException {
      Document toParse = Jsoup.parse(responseBody.string());
      Elements chapterContent = toParse.select("div#chapterContent");
      if (chapterContent.size() == 0) // backup parsing
         chapterContent = toParse.select("div [itemprop='articleBody']");
      List<Node> all = chapterContent.get(0).childNodes();

      // add prev/next links to 0,1
      Elements links = chapterContent.select("[href]");
      all.add(0, new Element(links.get(1).attr("href"))); // Next Chapter Link
      all.add(0, new Element(links.get(0).attr("href"))); // Previous Chapter Link

      ArrayList<String> paragraphs = removeBlanks(all);
      if (paragraphs.get(0).contains("Next Chapter") || paragraphs.get(0).contains("Previous Chapter")) {
         String title = paragraphs.get(0);
         Elements spanSelect = Jsoup.parse(title).select("span");
         if (spanSelect.size() == 2)
            paragraphs.set(0, spanSelect.get(1).toString());
         else
            paragraphs.remove(0);
      }
      return paragraphs;
   }

   public static ArrayList<String> dmParse(ResponseBody responseBody) throws IOException {
      Document toParse = Jsoup.parse(responseBody.string());
      Elements chapterContent = toParse.select("div [class='post-body entry-content']");
      List<Node> paragraphNodes = chapterContent.get(0).childNodes();

      // add prev/next links to 0,1
      Elements links = chapterContent.select("[href]");
      paragraphNodes.add(0, new Element(links.get(1).attr("href"))); // Next Chapter Link
      paragraphNodes.add(0, new Element(links.get(0).attr("href"))); // Previous Chapter Link

      return removeBlanks(paragraphNodes);
   }

   public static ArrayList<String> removeBlanks(List<Node> nodes) {
      ArrayList<String> paragraphs = new ArrayList<>();
      boolean first = false; // to remove multiple new lines in a row
      for (Node node : nodes) {
         String string = node.toString().replaceAll("<p[^>]+>|</p>|<p>", "");
         if (!string.equals(" ")
                 && !string.equals("<hr>")
                 && !string.equals("<br>")
                 && !string.equals("&nbsp;")) {
            if (string.length() == 0 && first) {
               // second new line, don't add it
               first = false;
            } else {
               // first new line usually has meaning
               first = string.length() == 0;
               paragraphs.add(string.replaceAll("&nbsp;", ""));
            }
         }
      }
      return paragraphs;
   }
}
