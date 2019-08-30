package relawan.moviecatalogue.view.favorite;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import relawan.moviecatalogue.R;
import relawan.moviecatalogue.adapter.favorite.FavoriteTvAdapter;
import relawan.moviecatalogue.listener.ItemClickSupport;
import relawan.moviecatalogue.model.tvmodel.TvCatalogue;
import relawan.moviecatalogue.view.detail.DetailTvActivity;
import relawan.moviecatalogue.viewmodel.TvViewModel;

public class FavoriteTvActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_TV = 201;

    private TvViewModel tvViewModel;
    private RecyclerView recyclerView;
    private FavoriteTvAdapter favoriteTvAdapter;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_tv);

        emptyText = findViewById(R.id.empty_list);

        favoriteTvAdapter = new FavoriteTvAdapter(this);

        recyclerView = findViewById(R.id.rv_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favoriteTvAdapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)  {
            actionBar.setTitle(getResources().getString(R.string.favorite_tv));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvViewModel = ViewModelProviders.of(this).get(TvViewModel.class);
        tvViewModel.getFavoriteTv().observe(this, new Observer<List<TvCatalogue>>() {
            @Override
            public void onChanged(@Nullable final List<TvCatalogue> tvCatalogues) {

                if (tvCatalogues != null && tvCatalogues.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_notice), Toast.LENGTH_SHORT).show();
                }
                else {
                    emptyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    favoriteTvAdapter.setListFavorite(tvCatalogues);

                }

                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Intent tvIntent = new Intent(FavoriteTvActivity.this, DetailTvActivity.class);
                        tvIntent.putExtra(DetailTvActivity.TV_DETAIL, tvCatalogues.get(position));
                        startActivityForResult(tvIntent, REQUEST_CODE_TV);
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
