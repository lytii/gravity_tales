package read.gravitytales;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class UrlTest {
   //   String urlString = "http://m.wuxiaworld.com/ssn-index/ssn-chapter-18/";
   String urlString = "http://www.wuxiaworld.com/ssn-index/ssn-chapter-154/";

   @Test
   public void parseUrl() throws IOException {
      URL url = new URL(urlString);
//      System.out.println(url.getProtocol());
//      System.out.println(url.getHost());
//      System.out.println(url.getPath());
//      System.out.println(Arrays.toString(url.getPath().split("-|/")));
      Document toParse = Jsoup.connect(urlString).get();
//      Elements chapterContent = toParse.select("div#chapterContent");
      Elements chapterContent = toParse.select("div [itemprop='articleBody']");
//      System.out.println(chapterContent.text());
      Elements something = chapterContent.select("meta[property='og:title']");
//      System.out.println(toParse.select("meta[property='og:title']").attr("content"));
      System.out.println(something.attr("content"));
   }
}
