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


}