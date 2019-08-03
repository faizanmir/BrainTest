package avishkaar.com.braintest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {

   public String BASE_URL =
            "https://jsonplaceholder.typicode.com";

    @GET("/photos/")
    Call<List<ImageData>> getImages();


}
