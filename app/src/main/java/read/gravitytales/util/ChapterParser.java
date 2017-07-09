package read.gravitytales.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public final class ChapterParser {
   public static ArrayList<String> parse(Document toParse) {
      Elements chapterContent = toParse.select("div#chapterContent");
      if (chapterContent.size() == 0) // backup parsing
         chapterContent = toParse.select("div [itemprop='articleBody']");
      List<Node> all = chapterContent.get(0).childNodes();
      ArrayList<String> paragraphs = new ArrayList<>();
      int i = 0;
      boolean first = false; // to remove multiple new lines in a row
      for (Node node : all) {
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
}
