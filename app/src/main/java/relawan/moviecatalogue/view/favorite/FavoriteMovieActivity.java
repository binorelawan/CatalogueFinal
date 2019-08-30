package relawan.moviecatalogue.view.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.adapter.favorite.FavoriteMovieAdapter;
import relawan.moviecatalogue.listener.ItemClickSupport;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;
import relawan.moviecatalogue.view.detail.DetailMovieActivity;
import relawan.moviecatalogue.viewmodel.MovieViewModel;

public class FavoriteMovieActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_MOVIE = 200;

    MovieViewModel movieViewModel;
    RecyclerView recyclerView;
    FavoriteMovieAdapter favoriteMovieAdapter;
    TextView emptyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie);

        emptyText = findViewById(R.id.empty_list);

        favoriteMovieAdapter = new FavoriteMovieAdapter(this);

        recyclerView = findViewById(R.id.rv_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favoriteMovieAdapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)  {
            actionBar.setTitle(getResources().getString(R.string.favorite_movie));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getFavoriteMovie().observe(this, new Observer<List<MovieCatalogue>>() {
            @Override
            public void onChanged(@Nullable final List<MovieCatalogue> movieCatalogues) {

                if (movieCatalogues != null && movieCatalogues.isEmpty())   {

                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_notice), Toast.LENGTH_SHORT).show();
                }
                else    {

                    emptyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    favoriteMovieAdapter.setListFavorite(movieCatalogues);
                    favoriteMovieAdapter.notifyDataSetChanged();


                }

                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Intent movieIntent = new Intent(FavoriteMovieActivity.this, DetailMovieActivity.class);
                        movieIntent.putExtra(DetailMovieActivity.MOVIE_DETAIL, movieCatalogues.get(position));
                        startActivityForResult(movieIntent, REQUEST_CODE_MOVIE);
                    }
                });
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
}
