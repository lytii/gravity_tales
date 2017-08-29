package read.gravitytales;

import android.arch.persistence.room.EmptyResultSetException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;

import static read.gravitytales.util.ChapterParser.parse;

public class UrlTest {
   //   String urlString = "http://m.wuxiaworld.com/ssn-index/ssn-chapter-18/";
   String urlString = "http://www.wuxiaworld.com/ssn-index/ssn-chapter-154/";

   @Test
   public void parseUrl() throws IOException {
      URL url = new URL(urlString);
//      System.out.println(urlString);
//      System.out.println(url);
//      String[] pathPart1 = url.getPath().split("/");
//      String[] pathPart2 = pathPart1[2].split("-");
//      System.out.println(Arrays.toString(pathPart1));// title of page
//      System.out.println(Arrays.toString(pathPart2));// title of page
//      System.out.println(url.getProtocol()+url.getHost()+'/'+pathPart1[1]+'/'+pathPart2[0]+'-'+pathPart2[1]+'-');
//      System.out.println(url.getPath());


      String path = url.getPath();
      String[] pathParts = path.split("-");
      // get chapter number from URL
      String numberString = pathParts[pathParts.length - 1];
      // trim chapter number
      String baseUrl = path.substring(0, path.indexOf(numberString));
      System.out.println(path);
      System.out.println(baseUrl);


      // get chapter number from URL
//      String[] urlPieces = urlString.split("-|/");
//      String numberString = urlPieces[urlPieces.length-1];
      // trim chapter number
//      String basePath = urlString.substring(0, urlString.indexOf(numberString));
//      System.out.println(basePath);

      // title of page
//      Document toParse = Jsoup.connect(urlString).get();
//      Elements titleElement = toParse.select("meta[property='og:title']");
//      System.out.println(titleElement.attr("content"));
   }

   @Test
   public void titleTest() throws IOException {
      String bookBaseUrl = "http://www.wuxiaworld.com/emperorofsoloplay-index/esp-chapter-";

      Document toParse = Jsoup.connect(bookBaseUrl + 153).get();
      parse(toParse);
//      System.out.println(paragraphs.toString());
   }

   @Test
   public void ogUrl() throws IOException {
      Document toParse = Jsoup.connect(urlString).get();
      Elements chapterContent = toParse.select("div#chapterContent");
      toParse.select("li");

      if (chapterContent.size() == 0) // backup parsing
         chapterContent = toParse.select("div [itemprop='articleBody']");
      List<Node> all = chapterContent.get(0).childNodes();
      Elements links = chapterContent.get(0).child(0).select("[href]");
      System.out.println(links.get(0).attr("href"));
      System.out.println(links.get(1).attr("href"));
   }

   @Test
   public void dmUrl() throws IOException {
      String urlString = "http://www.sousetsuka.com/2017/07/death-march-kara-hajimaru-isekai.html";
      Document toParse = Jsoup.connect(urlString).get();
      Elements chapterContent = toParse.select("div [class='post-body entry-content']");
      Elements links = chapterContent.select("[href]");
//      System.out.println(links);
      System.out.println(links.get(0).attr("href"));
      System.out.println(links.get(1).attr("href"));
      Element something = chapterContent.get(0);
      List<Node> all = something.childNodes();

      ArrayList<String> paragraphs = new ArrayList<>();
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
   }

   @Test
   public void throwingExceptions() {
      Observable.empty();
      Observable.just("")
                .map(a -> {
                   throw new EmptyResultSetException("throwing");
                })
                .onErrorReturn(throwable -> {
                   if (throwable instanceof EmptyResultSetException) {
                      return "instanceof";
                   } else {
                      return "failed";
                   }
                }).subscribe(a -> {
         System.out.println("subscribe " + a);
      });
   }

   int SEC = 1000;
   int MIN = 60;
   int HOUR = 60;
   int DAY = 24;

   @Test
   public void getUpdateList() throws IOException {
      List<String> favoriteTitles = Arrays.asList("A Record of a Mortal's Journey to Immortality","Seoul Station’s Necromancer", "Emperor of Solo Play");

      String urlString = "http://www.wuxiaworld.com";
      Document toParse = Jsoup.connect(urlString).get();

      // get update list
      Elements items = toParse.select("li[class=stacked-list-item collapsible]");
      for (Element item: items) {
         String bookTitle = item.select("span[class=title]").text();
            long releaseTimeSec = Long.valueOf(item.select("span[class=ww_elapsed_time").attr("data-releasetime"));
            System.out.printf("\n%s: %s\n", bookTitle, getTimeDiffString(releaseTimeSec));
//            Elements chapters = item.select("div[class=entry-content]").select("a");
            Elements chapters = item.select("div[class=chapterBtn]").select("a");
            if (chapters.size() == 0) {
//               chapters = item.select("h1[class=entry-title]").select("a");
               chapters = item.select("div[class=entry-content]");
            }
            for (Element chapter : chapters) { // links
               chapter = chapter.select("a").first();
               if(chapter == null) break;
//               System.out.println(chapter.text() + ": " + chapter.attr("href"));
            }
         return;
      }
      Elements bookNavItems = toParse.select("ul[class=sub-menu]");
      bookNavItems.remove(bookNavItems.size()-1); //
      bookNavItems.remove(0);
      bookNavItems = bookNavItems.select("a");

      int i = 0;
      for(Element item : bookNavItems) {
         String url = item.attr("href");
         System.out.println(item.text());
         System.out.println(url);
         getChapterList(url);
         System.out.println(
         );
      }
      items.first().select("a[href]");
      toParse.select("span[class=title]").text();
      toParse.select("span[class=title]");
   }

   public String getTimeDiffString(long timeSec) {
      long diffSec = (System.currentTimeMillis() / SEC - timeSec);
      long diffDay = diffSec / MIN / HOUR / DAY;
      if(diffDay > 0) {
         return String.format(Locale.ENGLISH, "%d Days ago", diffDay);
      } else {
         long diffHour = (diffSec - diffDay * MIN * HOUR * DAY) / MIN / HOUR;
         if( diffHour > 0) {
            return String.format(Locale.ENGLISH, "%d Hours ago", diffHour);
         } else {
            long diffMin = (diffSec - diffDay * MIN * HOUR * DAY - diffHour * MIN * HOUR) / MIN;
            return String.format(Locale.ENGLISH, "%d Minutes ago", diffMin);
         }
      }
   }

//   @Test
   public void getChapterList(String urlString) throws IOException {
//      String urlString = "http://www.wuxiaworld.com/emperorofsoloplay-index/";
//      String urlString = "http://www.wuxiaworld.com/ssn-index/";
//      String urlString = "http://www.wuxiaworld.com/ti-index/";
      Document toParse = Jsoup.connect(urlString).get();
      Elements p = toParse.select("div[itemprop=articleBody]").select("a");
      Iterator<Element> ip = p.iterator();
      while(ip.hasNext()) {
         Element item = ip.next();
         if(!item.attr("href").contains("index"))
            ip.remove();
      }
//      for(Element a: p) {
//         if(!a.attr("href").contains("index"))
//            a.remove();
//            System.out.println(a);
//         else
//
//            System.out.println("no index: "+a);
//      }
      for(Element a: p) {
         System.out.println(a);
      }
   }

   @Test
   public void testList() throws IOException {
      getChapterList("http://www.wuxiaworld.com/mga-index/");
//      getChapterList("http://www.wuxiaworld.com/ssn-index/");
//      getChapterList("/Users/long.lam/code/git/gravity_tales/app/src/test/java/read/gravitytales/UrlTest.java");
   }
}