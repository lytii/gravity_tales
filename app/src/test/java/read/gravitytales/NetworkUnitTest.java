package read.gravitytales;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import read.gravitytales.util.Network;

public class NetworkUnitTest {

   @Test
   public void testNetwork() throws IOException {
//      download(63);
      download(53);
      divdownload(53);
//      download(54);
   }

   public void download(int number) throws IOException {
      Network network = new Network();
      Integer[] f = {number};
      ArrayList<String> a = network.connect(f);
      ArrayList<String> b = a;
   }

   public void divdownload(int number) throws IOException {
      Network network = new Network();
      Integer[] f = {number};
   }
}
