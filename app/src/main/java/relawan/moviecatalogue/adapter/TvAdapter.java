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
import relawan.moviecatalogue.model.tvmodel.TvCatalogue;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.CategoryViewHolder> {

    private List<TvCatalogue> listTvCatalogue;
    private Context context;

    public TvAdapter(Context context) {
        this.context = context;
    }

    public void setlistTvCatalogue(List<TvCatalogue> listTvCatalogue) {
        this.listTvCatalogue = listTvCatalogue;
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
        categoryViewHolder.bind(listTvCatalogue.get(position));
    }

    @Override
    public int getItemCount() {
        return listTvCatalogue.size();
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

        public void bind(TvCatalogue tvCatalogue) {
            txtTitle.setText(tvCatalogue.getName());
            String vote = new DecimalFormat("##.##").format(tvCatalogue.getVoteAverage());
            txtScore.setText(vote);
            String imageUrl = "https://image.tmdb.org/t/p/w500";
            Glide.with(itemView)
                    .load(imageUrl + tvCatalogue.getPosterPath())
                    .apply(new RequestOptions())
                    .into(imgPoster);
        }
    }
}
