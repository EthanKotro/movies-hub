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

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public MoviePosterAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMoviePosterBinding binding = ItemMoviePosterBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ItemMoviePosterBinding binding;

        public MovieViewHolder(ItemMoviePosterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

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

            String posterUrl = "https://image.tmdb.org/t/p/w342" + movie.getPosterPath();
            Glide.with(itemView.getContext())
                    .load(posterUrl)
                    .transform(new CenterCrop())
                    .into(binding.imagePoster);
        }
    }
}
