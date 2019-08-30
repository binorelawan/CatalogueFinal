package relawan.moviecatalogue.view.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.adapter.search.SearchMovieAdapter;
import relawan.moviecatalogue.listener.ItemClickSupport;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;
import relawan.moviecatalogue.view.detail.DetailMovieActivity;
import relawan.moviecatalogue.viewmodel.MovieViewModel;

import static relawan.moviecatalogue.BuildConfig.API_KEY;

public class SearchMovieActivity extends AppCompatActivity {

    private static final int MOVIE_FAVORITE = 100;
    private static final int REQUEST_CODE_MOVIE = 200;

    private static final String TITLE = "title";

    private SearchView searchView;

    private MovieViewModel movieViewModel;
    private SearchMovieAdapter searchMovieAdapter;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String title = getIntent().getStringExtra(TITLE);

        searchView = findViewById(R.id.search_movie);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.rv_category);


        showLoading(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.search_movie));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initSearch(title);

    }

    private void searchMovie(String title) {

        searchMovieAdapter = new SearchMovieAdapter(this);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getSearchMovie(API_KEY, title).observe(this, new Observer<List<MovieCatalogue>>() {
            @Override
            public void onChanged(final List<MovieCatalogue> movieCatalogues) {

                searchMovieAdapter.setListSearchMovie(movieCatalogues);
                recyclerView.setAdapter(searchMovieAdapter);

                showLoading(false);

                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Intent searchIntent = new Intent(SearchMovieActivity.this, DetailMovieActivity.class);
                        searchIntent.putExtra(DetailMovieActivity.MOVIE_DETAIL, movieCatalogues.get(position));
                        startActivityForResult(searchIntent, REQUEST_CODE_MOVIE);

                    }
                });
            }
        });

    }

    private void initSearch(String title)   {

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.onActionViewExpanded();
        searchView.setQuery(title, false);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String title) {

                if (!title.isEmpty())    {

                    searchMovie(title);

                    // close keyboard setelah click enter
                    searchView.clearFocus();

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String titleText) {

                if (!titleText.isEmpty())   {

                    searchMovie(titleText);
                }

                return false;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MOVIE)  {
            if (resultCode == MOVIE_FAVORITE)   {
                MovieCatalogue movieCatalogue;
                movieCatalogue = Objects.requireNonNull(data).getParcelableExtra(DetailMovieActivity.MOVIE_DETAIL);
                movieViewModel.insert(movieCatalogue);

                Toast.makeText(this, movieCatalogue.getTitle() + " " + getResources().getString(R.string.insert_favorite), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
