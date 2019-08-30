package relawan.moviecatalogue.api;

import relawan.moviecatalogue.model.moviemodel.MovieResponse;
import relawan.moviecatalogue.model.tvmodel.TvResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    /* MOVIE CATALOGUE
     *
     * **************************************************************************************************************
     * */

    // Discover = https://api.themoviedb.org/3/discover/movie?api_key=fb22ed16d6799323ff22a1bac3950cee&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1
    @GET("3/discover/movie")
    Call<MovieResponse> getDiscoverMovie(
            @Query("api_key") String apiKey);


    // Popular = https://api.themoviedb.org/3/movie/popular?api_key=fb22ed16d6799323ff22a1bac3950cee&language=en-US&page=1
    @GET("3/movie/popular")
    Call<MovieResponse> getPopularMovie(
            @Query("api_key") String apiKey);


    // Now_Playing = https://api.themoviedb.org/3/movie/now_playing?api_key=fb22ed16d6799323ff22a1bac3950cee&language=en-US&page=1
    @GET("3/movie/now_playing")
    Call<MovieResponse> getNowMovie(
            @Query("api_key") String apiKey);


    // search movie = https://api.themoviedb.org/3/search/movie?api_key={API KEY}&language=en-US&query={MOVIE NAME}
    @GET("3/search/movie")
    Call<MovieResponse> getSearchMovie(
            @Query("api_key") String apiKey,
            @Query("query") String query);


    // release movie = https://api.themoviedb.org/3/discover/movie?api_key={API KEY}&primary_release_date.gte={TODAY DATE}&primary_release_date.lte={TODAY DATE}
    @GET("3/discover/movie")
    Call<MovieResponse> getReleaseMovie(
            @Query("api_key") String apiKey,
            @Query("primary_release_date.gte") String gte,
            @Query("primary_release_date.lte") String lte);




    /* TV CATALOGUE
    *
    * **************************************************************************************************************
    * */



    // Discover https://api.themoviedb.org/3/discover/tv?api_key=fb22ed16d6799323ff22a1bac3950cee&language=en-US&sort_by=popularity.desc&page=1&timezone=America%2FNew_York&include_null_first_air_dates=false
    @GET("3/discover/tv")
    Call<TvResponse> getDiscoverTv(
            @Query("api_key") String apiKey);


    // Popular = https://api.themoviedb.org/3/tv/popular?api_key=fb22ed16d6799323ff22a1bac3950cee&language=en-US&page=1
    @GET("3/tv/popular")
    Call<TvResponse> getPopularTv(
            @Query("api_key") String apiKey);


    // Now_Playing = https://api.themoviedb.org/3/tv/on_the_air?api_key=fb22ed16d6799323ff22a1bac3950cee&language=en-US&page=1
    @GET("3/tv/on_the_air")
    Call<TvResponse> getNowTv(
            @Query("api_key") String apiKey);


    // search tv = https://api.themoviedb.org/3/search/tv?api_key={API KEY}&language=en-US&query={TV SHOW NAME}
    @GET("3/search/tv")
    Call<TvResponse> getSearchTv(
            @Query("api_key") String apiKey,
            @Query("query") String query);
}
