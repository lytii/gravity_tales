package read.gravitytales.util;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BookNetwork {
   @GET("ssn-index/ssn-chapter-{number}")
   Observable<ResponseBody> getSSNChapter(@Path("number") int number);
}
