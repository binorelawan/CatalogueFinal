package relawan.moviecatalogue.fragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import relawan.moviecatalogue.R;
import relawan.moviecatalogue.adapter.TvAdapter;
import relawan.moviecatalogue.listener.ItemClickSupport;
import relawan.moviecatalogue.model.tvmodel.TvCatalogue;
import relawan.moviecatalogue.view.detail.DetailTvActivity;
import relawan.moviecatalogue.viewmodel.TvViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvNowFragment extends Fragment {

    private static final int TV_FAVORITE = 101;
    private static final int REQUEST_CODE_TV = 201;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    TvAdapter tvAdapter;
    TvViewModel tvViewModel;

    public TvNowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress);
        showLoading(true);

        recyclerView = view.findViewById(R.id.rv_category);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        tvAdapter = new TvAdapter(getContext());

        tvViewModel = ViewModelProviders.of(this).get(TvViewModel.class);
        tvViewModel.getNowTv().observe(this, new Observer<List<TvCatalogue>>() {
            @Override
            public void onChanged(@Nullable final List<TvCatalogue> tvCatalogues) {

                tvAdapter.setlistTvCatalogue(tvCatalogues);
                recyclerView.setAdapter(tvAdapter);

                showLoading(false);

                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Intent tvIntent = new Intent(getContext(), DetailTvActivity.class);
                        tvIntent.putExtra(DetailTvActivity.TV_DETAIL, tvCatalogues.get(position));
                        startActivityForResult(tvIntent, REQUEST_CODE_TV);
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

        if (requestCode == REQUEST_CODE_TV) {
            if (resultCode == TV_FAVORITE)  {
                TvCatalogue tvCatalogue;
                tvCatalogue = data.getParcelableExtra(DetailTvActivity.TV_DETAIL);
                tvViewModel.insert(tvCatalogue);

                Toast.makeText(getContext(), tvCatalogue.getName() + " " + getResources().getString(R.string.insert_favorite), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
