package read.gravitytales.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DivNetwork {
   private String bookUrl = "http://www.wuxiaworld.com/emperorofsoloplay-index/esp-chapter-";

   public Elements connect(Integer[] integers) throws IOException {
      Document toParse = Jsoup.connect(bookUrl + integers[0]).get();
      Elements selected = toParse.select("div");
      return selected;
   }
}
