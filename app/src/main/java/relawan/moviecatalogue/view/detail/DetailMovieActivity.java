package relawan.moviecatalogue.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;
import relawan.moviecatalogue.viewmodel.MovieViewModel;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MOVIE_DETAIL = "movie_detail";
    private static final int MOVIE_FAVORITE = 100;

    private TextView dataName;
    private TextView dataYear;
    private TextView dataScore;
    private TextView dataOverview;
    private ImageView dataPoster;

    private Button dataFavorite;

    MovieCatalogue movieCatalogue;
    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        dataName = findViewById(R.id.txt_name);
        dataYear = findViewById(R.id.txt_year);
        dataScore = findViewById(R.id.txt_score);
        dataOverview = findViewById(R.id.txt_overview);
        dataPoster = findViewById(R.id.img_poster);

        dataFavorite = findViewById(R.id.btn_favorite);
        dataFavorite.setOnClickListener(this);

        movieCatalogue = getIntent().getParcelableExtra(MOVIE_DETAIL);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)  {
            actionBar.setTitle(movieCatalogue.getTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getIdMovie(movieCatalogue.getId()).observe(this, new Observer<List<MovieCatalogue>>() {
            @Override
            public void onChanged(@Nullable List<MovieCatalogue> movieCatalogues) {

                if (movieCatalogues != null) {
                    if (!movieCatalogues.isEmpty()) {
                        movieCatalogue.setFavorite(true);
                        dataFavorite.setText(getResources().getString(R.string.favorite_remove));
                    }
                    else {
                        movieCatalogue.setFavorite(false);
                        dataFavorite.setText(getResources().getString(R.string.favorite_add));
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())   {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_favorite)  {

            if (!movieCatalogue.isFavorite())   {
                Intent favoriteIntent = new Intent();
                favoriteIntent.putExtra(MOVIE_DETAIL, movieCatalogue);
                setResult(MOVIE_FAVORITE, favoriteIntent);
            }

            else {

                movieViewModel.delete(movieCatalogue);
                Toast.makeText(getApplicationContext(), movieCatalogue.getTitle() + " " + getResources().getString(R.string.delete_favorite), Toast.LENGTH_SHORT ).show();
            }
        }

        finish();

    }

    private void init() {

        dataName.setText(movieCatalogue.getTitle());

        if (movieCatalogue.getReleaseDate() == null)    {
            dataYear.setText(R.string.no_date);
        } else {
            dataYear.setText(convertFormat(movieCatalogue.getReleaseDate()));
        }

        String vote = new DecimalFormat("##.##").format(movieCatalogue.getVoteAverage());
        dataScore.setText(vote);

        if (movieCatalogue.getOverview() == null || movieCatalogue.getOverview().isEmpty()) {
            dataOverview.setText(R.string.no_overview);
        } else {
            dataOverview.setText(movieCatalogue.getOverview());
        }

        if (movieCatalogue.getPosterPath() == null) {
            dataPoster.setImageResource(R.drawable.no_image);
        } else {
            String imageUrl = "https://image.tmdb.org/t/p/w500";
            Glide.with(this)
                    .load(imageUrl + movieCatalogue.getPosterPath())
                    .apply(new RequestOptions()).into(dataPoster);
        }

    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date date = new Date();
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat convertDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        return convertDateFormat.format(date);
    }
}
