package relawan.moviecatalogue.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;
import relawan.moviecatalogue.repository.MovieRepository;

import static relawan.moviecatalogue.BuildConfig.API_KEY;

public class MovieViewModel extends AndroidViewModel {

    private static final String QUERY = "movie_name";

    private MovieRepository movieRepository;
    private LiveData<List<MovieCatalogue>> movieFavoriteLiveData;
    private MutableLiveData<List<MovieCatalogue>> listDiscoverLiveData;
    private MutableLiveData<List<MovieCatalogue>> listPopularLiveData;
    private MutableLiveData<List<MovieCatalogue>> listNowLiveData;


    // default constructor from AndroidViewModel
    public MovieViewModel(@NonNull Application application) {
        super(application);

        movieRepository = new MovieRepository(application);
        movieFavoriteLiveData = movieRepository.getFavoriteMovie();
        listPopularLiveData = movieRepository.getPopularMovie(API_KEY);
        listNowLiveData = movieRepository.getNowMovie(API_KEY);
        listDiscoverLiveData = movieRepository.getDiscoverMovie(API_KEY);

    }

    // Implement to MovieFragment
    public LiveData<List<MovieCatalogue>> getDiscoverMovie()    {
        return listDiscoverLiveData;
    }

    // Implement to MoviePopularFragment
    public LiveData<List<MovieCatalogue>> getPopularMovie()    {
        return listPopularLiveData;
    }

    // Implement to MovieNowFragment
    public LiveData<List<MovieCatalogue>> getNowMovie() {
        return listNowLiveData;
    }

    // Implement to SearchMovieActivity
    public LiveData<List<MovieCatalogue>> getSearchMovie(String apiKey, String query)  {
        return movieRepository.getSearchMovie(apiKey, query );
    }

    // Implement to ReleaseActivity
    public LiveData<List<MovieCatalogue>> getReleaseMovie(String apiKey, String gte, String lte)    {
        return movieRepository.getReleaseMovie(apiKey, gte, lte);
    }


    // Implement to DetailMovieActivity
    public LiveData<List<MovieCatalogue>> getIdMovie(int id)    {
        return movieRepository.getIdMovie(id);
    }

    // Implement to FavoriteMovieActivity
    public LiveData<List<MovieCatalogue>> getFavoriteMovie() {
        return movieFavoriteLiveData;

    }


    // insert dan delete favorite movie method from MovieDao >> MovieRepository >> MovieViewModel
    public void insert(MovieCatalogue moCa) {
        movieRepository.insert(moCa);
    }

    public void delete(MovieCatalogue moCa) {
        movieRepository.delete(moCa);
    }
}
