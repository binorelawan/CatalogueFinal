package relawan.moviecatalogue.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.adapter.release.ReleaseAdapter;
import relawan.moviecatalogue.listener.ItemClickSupport;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;
import relawan.moviecatalogue.view.detail.DetailMovieActivity;
import relawan.moviecatalogue.viewmodel.MovieViewModel;

import static relawan.moviecatalogue.BuildConfig.API_KEY;

public class ReleaseActivity extends AppCompatActivity {

    private static final int MOVIE_FAVORITE = 100;
    private static final int REQUEST_CODE_MOVIE = 200;

    private MovieViewModel movieViewModel;
    private ReleaseAdapter releaseAdapter;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        progressBar = findViewById(R.id.progress);
        showLoading(true);

        recyclerView = findViewById(R.id.rv_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        releaseAdapter = new ReleaseAdapter(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.new_release));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = formatter.format(date);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getReleaseMovie(API_KEY, todayDate, todayDate).observe(this, new Observer<List<MovieCatalogue>>() {
            @Override
            public void onChanged(final List<MovieCatalogue> movieCatalogues) {

                releaseAdapter.setListRelease(movieCatalogues);
                recyclerView.setAdapter(releaseAdapter);

                showLoading(false);

                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Intent movieIntent = new Intent(ReleaseActivity.this, DetailMovieActivity.class);
                        movieIntent.putExtra(DetailMovieActivity.MOVIE_DETAIL, movieCatalogues.get(position));
                        startActivityForResult(movieIntent, REQUEST_CODE_MOVIE);
                    }
                });
            }
        });
    }

    private void showLoading(boolean state) {
        if (state)  {
            progressBar.setVisibility(View.VISIBLE);
        }   else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MOVIE)  {
            if (resultCode == MOVIE_FAVORITE)   {
                MovieCatalogue movieCatalogue;
                movieCatalogue = data.getParcelableExtra(DetailMovieActivity.MOVIE_DETAIL);
                movieViewModel.insert(movieCatalogue);

                Toast.makeText(this, movieCatalogue.getTitle() + " " + getResources().getString(R.string.insert_favorite), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
