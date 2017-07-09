package read.gravitytales;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import read.gravitytales.util.Network;
import read.gravitytales.util.WuxiaService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
