package read.gravitytales.util;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface BookNetwork {
   @GET("ssn-index/ssn-chapter-{number}")
   Observable<ResponseBody> getSSNChapter(@Path("number") int number);

   @GET
   Observable<ResponseBody> getChapter(@Url String url);

   @GET
   Call<ResponseBody> getSomething(@Url String url);
}
