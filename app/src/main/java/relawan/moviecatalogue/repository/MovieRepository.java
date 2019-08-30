package relawan.moviecatalogue.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import relawan.moviecatalogue.api.ApiInterface;
import relawan.moviecatalogue.api.RetrofitApi;
import relawan.moviecatalogue.model.AppDatabase;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;
import relawan.moviecatalogue.model.moviemodel.MovieDao;
import relawan.moviecatalogue.model.moviemodel.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {


    private static final String TAG = MovieRepository.class.getSimpleName();
    private ApiInterface apiInterface;

    private MovieDao movieDao;
    private LiveData<List<MovieCatalogue>> favoriteMovies;

    public MovieRepository(Application application)    {
        apiInterface = RetrofitApi.getRetrofitInstance().create(ApiInterface.class);

        AppDatabase database = AppDatabase.getInstance(application);
        movieDao = database.movieDao();

        // getFavoriteMovies from MovieDao
        favoriteMovies = movieDao.getFavoriteMovies();
    }

    // discover movie
    public MutableLiveData<List<MovieCatalogue>> getDiscoverMovie(String apiKey)    {
        final MutableLiveData<List<MovieCatalogue>> discoverMovie = new MutableLiveData<>();
        Call<MovieResponse> call = apiInterface.getDiscoverMovie(apiKey);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null)    {
                    discoverMovie.setValue(response.body().getResults());

                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                discoverMovie.setValue(null);
            }
        });

        return discoverMovie;
    }


    // popular movie
    public MutableLiveData<List<MovieCatalogue>> getPopularMovie(String apiKey) {
        final MutableLiveData<List<MovieCatalogue>> popularMovie = new MutableLiveData<>();
        Call<MovieResponse> call = apiInterface.getPopularMovie(apiKey);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null) {
                    popularMovie.setValue(response.body().getResults());

                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                popularMovie.setValue(null);
            }
        });

        return popularMovie;
    }


    // now playing movie
    public MutableLiveData<List<MovieCatalogue>> getNowMovie(String apiKey) {
        final MutableLiveData<List<MovieCatalogue>> nowMovie = new MutableLiveData<>();
        Call<MovieResponse> call = apiInterface.getNowMovie(apiKey);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null)    {
                    nowMovie.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                nowMovie.setValue(null);
            }
        });

        return nowMovie;
    }


    // search movie
    public MutableLiveData<List<MovieCatalogue>> getSearchMovie(String apiKey, String query)    {
        final MutableLiveData<List<MovieCatalogue>> searchMovie = new MutableLiveData<>();
        Call<MovieResponse> call = apiInterface.getSearchMovie(apiKey, query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null)    {
                    searchMovie.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                searchMovie.setValue(null);
            }
        });

        return searchMovie;
    }

    // release movie
    public MutableLiveData<List<MovieCatalogue>> getReleaseMovie(String apiKey, String gte, String lte) {
        final MutableLiveData<List<MovieCatalogue>> releaseMovie = new MutableLiveData<>();
        Call<MovieResponse> call = apiInterface.getReleaseMovie(apiKey, gte, lte);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null)    {
                    releaseMovie.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                releaseMovie.setValue(null);
            }
        });

        return releaseMovie;
    }


    // Query from MovieDao

    public LiveData<List<MovieCatalogue>> getFavoriteMovie() {
        return favoriteMovies;
    }

    public LiveData<List<MovieCatalogue>> getIdMovie(int id) {
        return movieDao.getIdMovie(id);
    }


    public void insert(MovieCatalogue moCa) {
        new InsertMovieAsyncTask(movieDao).execute(moCa);
    }


    public void delete(MovieCatalogue moCa) {
        new DeleteMovieAsyncTask(movieDao).execute(moCa);

    }



    private static class InsertMovieAsyncTask extends AsyncTask<MovieCatalogue, Void, Void> {

        private MovieDao movieDao;

        private InsertMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }
        @Override
        protected Void doInBackground(MovieCatalogue... movieCatalogues) {
            movieDao.insertMovie(movieCatalogues[0]);
            return null;
        }
    }



    private static class DeleteMovieAsyncTask extends AsyncTask<MovieCatalogue, Void, Void> {

        private MovieDao movieDao;

        private DeleteMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }
        @Override
        protected Void doInBackground(MovieCatalogue... movieCatalogues) {
            movieDao.deleteMovie(movieCatalogues[0]);
            return null;
        }
    }
}
