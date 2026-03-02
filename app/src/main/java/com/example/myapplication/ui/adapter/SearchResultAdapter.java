package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.myapplication.data.model.Movie;
import com.example.myapplication.databinding.ItemMoviePosterBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public SearchResultAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMoviePosterBinding binding = ItemMoviePosterBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        private final ItemMoviePosterBinding binding;

        public SearchViewHolder(ItemMoviePosterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Adjust card margin for grid layout
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.getRoot()
                    .getLayoutParams();
            layoutParams.setMarginEnd(16);
            layoutParams.bottomMargin = 16;
            binding.getRoot().setLayoutParams(layoutParams);

            // Adjust height for grid
            binding.imagePoster.getLayoutParams().height = 360;
            binding.imagePoster.requestLayout();

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(movies.get(position));
                }
            });
        }

        public void bind(Movie movie) {
            binding.textRating.setText(String.format(Locale.getDefault(), "%.1f", movie.getVoteAverage()));
            binding.textTitle.setText(movie.getTitle());

            if (movie.getPosterPath() != null) {
                String posterUrl = "https://image.tmdb.org/t/p/w342" + movie.getPosterPath();
                Glide.with(itemView.getContext())
                        .load(posterUrl)
                        .transform(new CenterCrop())
                        .into(binding.imagePoster);
            } else {
                Glide.with(itemView.getContext()).clear(binding.imagePoster);
            }
        }
    }
}
