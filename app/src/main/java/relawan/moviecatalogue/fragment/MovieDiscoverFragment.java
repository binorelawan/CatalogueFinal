package relawan.moviecatalogue.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.adapter.MovieAdapter;
import relawan.moviecatalogue.listener.ItemClickSupport;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;
import relawan.moviecatalogue.view.detail.DetailMovieActivity;
import relawan.moviecatalogue.viewmodel.MovieViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDiscoverFragment extends Fragment {

    private static final int MOVIE_FAVORITE = 100;
    private static final int REQUEST_CODE_MOVIE = 200;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    MovieAdapter movieAdapter;
    MovieViewModel movieViewModel;


    public MovieDiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress);
        showLoading(true);

        recyclerView = view.findViewById(R.id.rv_category);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(getContext());

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getDiscoverMovie().observe(this, new Observer<List<MovieCatalogue>>() {
            @Override
            public void onChanged(@Nullable final List<MovieCatalogue> movieCatalogues) {

                movieAdapter.setListMovieCatalogue(movieCatalogues);
                recyclerView.setAdapter(movieAdapter);

                showLoading(false);

                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Intent movieIntent = new Intent(getContext(), DetailMovieActivity.class);
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

                Toast.makeText(getContext(), movieCatalogue.getTitle() + " " + getResources().getString(R.string.insert_favorite), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
