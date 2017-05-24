package read.gravitytales;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

import read.gravitytales.util.DivNetwork;

public class NetworkUnitTest {

   @Test
   public void testNetwork() throws IOException {
      download(53);
      download(54);
      download(55);
   }

   public void download(int number) throws IOException {
      DivNetwork network = new DivNetwork();
      Integer[] f = {number};
      Elements a = network.connect(f);
      Element b = a.get(14);
      System.out.println(a.size());
   }
}
