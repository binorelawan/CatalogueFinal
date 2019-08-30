package relawan.moviecatalogue.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static relawan.moviecatalogue.BuildConfig.BASE_URL;

public class RetrofitApi {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
