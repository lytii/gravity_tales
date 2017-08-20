package read.gravitytales;

import android.arch.persistence.room.EmptyResultSetException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import read.gravitytales.objects.ChapterListingParagraphs;

import static read.gravitytales.util.ChapterParser.removeBlanks;

public class UrlTest {
   //   String urlString = "http://m.wuxiaworld.com/ssn-index/ssn-chapter-18/";
   String urlString = "http://www.wuxiaworld.com/ssn-index/ssn-chapter-154/";

   @Test
   public void parseUrl() throws IOException {
      URL url = new URL(urlString);
      System.out.println(Arrays.toString(url.getPath().split("/")));// title of page

      // title of page
      Document toParse = Jsoup.connect(urlString).get();
      Elements titleElement = toParse.select("meta[property='og:title']");
      System.out.println(titleElement.attr("content"));
   }

   @Test
   public void ogUrl() throws IOException{
      Document toParse = Jsoup.connect(urlString).get();
      Elements chapterContent = toParse.select("div#chapterContent");
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
                   if(throwable instanceof EmptyResultSetException) {
                      return "instanceof";
                   } else {
                      return "failed";
                   }
                }).subscribe(a -> {System.out.println("subscribe " + a);});
   }

}