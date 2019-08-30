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
import relawan.moviecatalogue.adapter.search.SearchTvAdapter;
import relawan.moviecatalogue.listener.ItemClickSupport;
import relawan.moviecatalogue.model.tvmodel.TvCatalogue;
import relawan.moviecatalogue.view.detail.DetailTvActivity;
import relawan.moviecatalogue.viewmodel.TvViewModel;

import static relawan.moviecatalogue.BuildConfig.API_KEY;

public class SearchTvActivity extends AppCompatActivity {

    private static final int TV_FAVORITE = 101;
    private static final int REQUEST_CODE_TV = 201;

    private static final String TITLE = "title";

    private SearchView searchView;

    private TvViewModel tvViewModel;
    private SearchTvAdapter searchTvAdapter;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
            actionBar.setTitle(getResources().getString(R.string.search_tv));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initSearch(title);
    }

    private void searchTv(String title) {

        searchTvAdapter = new SearchTvAdapter(this);
        tvViewModel = ViewModelProviders.of(this).get(TvViewModel.class);
        tvViewModel.getSearchTv(API_KEY, title).observe(this, new Observer<List<TvCatalogue>>() {
            @Override
            public void onChanged(final List<TvCatalogue> tvCatalogues) {

                searchTvAdapter.setListSearchTv(tvCatalogues);
                recyclerView.setAdapter(searchTvAdapter);

                showLoading(false);

                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Intent searchIntent = new Intent(SearchTvActivity.this, DetailTvActivity.class);
                        searchIntent.putExtra(DetailTvActivity.TV_DETAIL, tvCatalogues.get(position));
                        startActivityForResult(searchIntent, REQUEST_CODE_TV);
                    }
                });
            }
        });
    }

    private void initSearch(String title)   {

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.onActionViewExpanded();
        searchView.setQuery(title, false);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String title) {


                if (!title.isEmpty())    {

                    searchTv(title);

                    // close keyboard setelah click enter
                    searchView.clearFocus();

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String titleText) {

                if (!titleText.isEmpty())   {

                    searchTv(titleText);
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

         if (requestCode == REQUEST_CODE_TV) {
            if (resultCode == TV_FAVORITE)  {
                TvCatalogue tvCatalogue;
                tvCatalogue = Objects.requireNonNull(data).getParcelableExtra(DetailTvActivity.TV_DETAIL);
                tvViewModel.insert(tvCatalogue);

                Toast.makeText(this, tvCatalogue.getName() + " " + getResources().getString(R.string.insert_favorite), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
