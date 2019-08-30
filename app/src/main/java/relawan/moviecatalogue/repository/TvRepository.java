package relawan.moviecatalogue.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import relawan.moviecatalogue.api.ApiInterface;
import relawan.moviecatalogue.api.RetrofitApi;
import relawan.moviecatalogue.model.AppDatabase;
import relawan.moviecatalogue.model.tvmodel.TvCatalogue;
import relawan.moviecatalogue.model.tvmodel.TvDao;
import relawan.moviecatalogue.model.tvmodel.TvResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvRepository {

    private static final String TAG = TvRepository.class.getSimpleName();
    private ApiInterface apiInterface;

    private TvDao tvDao;
    private LiveData<List<TvCatalogue>> favoriteTvs;

    public TvRepository(Application application) {
        apiInterface = RetrofitApi.getRetrofitInstance().create(ApiInterface.class);

        AppDatabase database = AppDatabase.getInstance(application);
        tvDao = database.tvDao();

        // getFavoriteTvs from TvDao
        favoriteTvs = tvDao.getFavoriteTvs();
    }

    // discover
    public MutableLiveData<List<TvCatalogue>> getDiscoverTv(String apiKey)  {
        final MutableLiveData<List<TvCatalogue>> discoverTv = new MutableLiveData<>();
        Call<TvResponse> call = apiInterface.getDiscoverTv(apiKey);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null)    {
                    discoverTv.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                discoverTv.setValue(null);
            }
        });

        return discoverTv;
    }

    // popular tv
    public MutableLiveData<List<TvCatalogue>> getPopularTv(String apiKey)  {
        final MutableLiveData<List<TvCatalogue>> popularTv = new MutableLiveData<>();
        Call<TvResponse> call = apiInterface.getPopularTv(apiKey);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null) {
                    popularTv.setValue(response.body().getResults());
                }

            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                popularTv.setValue(null);
            }
        });

        return popularTv;
    }

    // now playing tv
    public MutableLiveData<List<TvCatalogue>> getNowTv(String apiKey)   {
        final MutableLiveData<List<TvCatalogue>> nowTv = new MutableLiveData<>();
        Call<TvResponse> call = apiInterface.getNowTv(apiKey);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null)    {
                    nowTv.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                nowTv.setValue(null);
            }
        });

        return nowTv;
    }

    // search tv
    public MutableLiveData<List<TvCatalogue>> getSearchTv(String apiKey, String query)    {
        final MutableLiveData<List<TvCatalogue>> searchTv = new MutableLiveData<>();
        Call<TvResponse> call = apiInterface.getSearchTv(apiKey, query);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.body() != null)    {
                    searchTv.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                searchTv.setValue(null);
            }
        });

        return searchTv;
    }



    // Query from TvDao

    public LiveData<List<TvCatalogue>> getFavoriteTv()  {
        return favoriteTvs;
    }

    public LiveData<List<TvCatalogue>> getIdTv(int id)  {
        return tvDao.getIdTv(id);
    }



    public void insert(TvCatalogue tsCa)    {
        new InsertTvAsyncTask(tvDao).execute(tsCa);
    }

    public void delete(TvCatalogue tsCa)    {
        new DeleteTvAsyncTask(tvDao).execute(tsCa);
    }


    private static class InsertTvAsyncTask extends AsyncTask<TvCatalogue, Void, Void> {

        private TvDao tvDao;

        private InsertTvAsyncTask(TvDao tvShowDao) {
            this.tvDao = tvShowDao;
        }
        @Override
        protected Void doInBackground(TvCatalogue... tvCatalogues) {
            tvDao.insertTv(tvCatalogues[0]);
            return null;
        }
    }


    private static class DeleteTvAsyncTask extends AsyncTask<TvCatalogue, Void, Void> {

        private TvDao tvDao;

        private DeleteTvAsyncTask(TvDao tvDao) {
            this.tvDao = tvDao;
        }
        @Override
        protected Void doInBackground(TvCatalogue... tvCatalogues) {
            tvDao.deleteTv(tvCatalogues[0]);
            return null;
        }
    }
}
