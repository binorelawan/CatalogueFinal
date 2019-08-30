package relawan.moviecatalogue.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import relawan.moviecatalogue.model.tvmodel.TvCatalogue;
import relawan.moviecatalogue.repository.TvRepository;

import static relawan.moviecatalogue.BuildConfig.API_KEY;

public class TvViewModel extends AndroidViewModel {

    private TvRepository tvRepository;
    private LiveData<List<TvCatalogue>> tvFavoriteLiveData;
    private MutableLiveData<List<TvCatalogue>>  listDiscoverLiveData;
    private MutableLiveData<List<TvCatalogue>> listPopularLiveData;
    private MutableLiveData<List<TvCatalogue>> listNowLiveData;

    // default constructor from AndroidViewModel
    public TvViewModel(@NonNull Application application) {
        super(application);

        tvRepository = new TvRepository(application);
        tvFavoriteLiveData = tvRepository.getFavoriteTv();
        listPopularLiveData = tvRepository.getPopularTv(API_KEY);
        listNowLiveData = tvRepository.getNowTv(API_KEY);
        listDiscoverLiveData = tvRepository.getDiscoverTv(API_KEY);
    }

    // implement to TvDiscoverFragment
    public LiveData<List<TvCatalogue>>  getDiscoverTv() {
        return listDiscoverLiveData;
    }

    // implement to TvPopularFragment
    public LiveData<List<TvCatalogue>> getPopularTv()  {
        return listPopularLiveData;
    }

    // implement to TvNowFragment
    public LiveData<List<TvCatalogue>> getNowTv()   {
        return listNowLiveData;
    }

    // implement to SearchTvActivity
    public LiveData<List<TvCatalogue>> getSearchTv(String apiKey, String query) {
        return tvRepository.getSearchTv(apiKey, query);
    }


    // implement to DetailTvActivity
    public LiveData<List<TvCatalogue>> getIdTv(int id)  {
        return tvRepository.getIdTv(id);
    }

    // implement to FavoriteTvActivity
    public LiveData<List<TvCatalogue>> getFavoriteTv()   {
        return tvFavoriteLiveData;
    }






    // insert dan delete method from MovieDao >> MovieRepository >> MovieViewModel
    public void insert(TvCatalogue tsCa)    {
        tvRepository.insert(tsCa);
    }

    public void delete(TvCatalogue tsCa)    {
        tvRepository.delete(tsCa);
    }
}
