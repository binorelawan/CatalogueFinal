package relawan.moviecatalogue.adapter.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.SearchViewHolder> {

    private Context context;
    private List<MovieCatalogue> listSearchMovie = new ArrayList<>();

    public SearchMovieAdapter(Context context) {
        this.context = context;
    }

    public void setListSearchMovie(List<MovieCatalogue> listSearchMovie) {
        this.listSearchMovie = listSearchMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new SearchViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int position) {
        searchViewHolder.bind(listSearchMovie.get(position));
    }

    @Override
    public int getItemCount() {
        if (listSearchMovie != null)    {
            return listSearchMovie.size();
        } else {
            return 0;
        }

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtYear;
        private TextView txtOverview;
        private ImageView imgPoster;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtYear = itemView.findViewById(R.id.txt_year);
            txtOverview = itemView.findViewById(R.id.txt_overview);
            imgPoster = itemView.findViewById(R.id.img_poster);
        }

        public void bind(MovieCatalogue movieCatalogue) {

            txtTitle.setText(movieCatalogue.getTitle());

            if (movieCatalogue.getReleaseDate() == null)    {
                txtYear.setText(R.string.no_date);
            } else {
                txtYear.setText(convertFormat(movieCatalogue.getReleaseDate()));
            }

            if (movieCatalogue.getOverview() == null || movieCatalogue.getOverview().isEmpty()) {
                txtOverview.setText(R.string.no_overview);
            } else {
                txtOverview.setText(movieCatalogue.getOverview());
            }

            if (movieCatalogue.getPosterPath() == null) {
                imgPoster.setImageResource(R.drawable.no_image);
            } else {
                String imageUrl = "https://image.tmdb.org/t/p/w500";
                Glide.with(itemView)
                        .load(imageUrl + movieCatalogue.getPosterPath())
                        .apply(new RequestOptions()).into(imgPoster);
            }

        }
    }

    private static String convertFormat(String inputDate) {
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
