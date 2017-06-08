package read.gravitytales;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import read.gravitytales.util.Network;

public class NetworkUnitTest {

   @Test
   public void testNetwork() throws Exception {
      download(45);
   }

   public void download(int number) throws Exception {
      Network network = new Network("https://gravitytales.com/Novel/dimensional-sovereign/ds-chapter-");
      Integer[] f = {number};
      ArrayList<String> a = network.connect(f);
      System.out.println(a);
   }
}
