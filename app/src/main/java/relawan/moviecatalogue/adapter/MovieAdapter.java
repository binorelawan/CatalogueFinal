package relawan.moviecatalogue.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.List;

import relawan.moviecatalogue.R;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CategoryViewHolder> {


    private List<MovieCatalogue> listMovieCatalogue;
    private Context context;

    public MovieAdapter(Context context)    {
        this.context = context;
    }

    public void setListMovieCatalogue(List<MovieCatalogue> listMovieCatalogue) {
        this.listMovieCatalogue = listMovieCatalogue;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_catalogue, viewGroup, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position) {
        categoryViewHolder.bind(listMovieCatalogue.get(position));


    }

    @Override
    public int getItemCount() {
        return listMovieCatalogue.size();

    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        private TextView txtTitle;
        private TextView txtScore;
        private ImageView imgPoster;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardview);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtScore = itemView.findViewById(R.id.txt_score);
            imgPoster = itemView.findViewById(R.id.img_poster);
        }

        public void bind(MovieCatalogue movieCatalogue) {
            txtTitle.setText(movieCatalogue.getTitle());
            String vote = new DecimalFormat("##.##").format(movieCatalogue.getVoteAverage());
            txtScore.setText(vote);
            String imageUrl = "https://image.tmdb.org/t/p/w500";
            Glide.with(itemView)
                    .load(imageUrl + movieCatalogue.getPosterPath())
                    .apply(new RequestOptions())
                    .into(imgPoster);
        }
    }


}
