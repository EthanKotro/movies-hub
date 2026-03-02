package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.myapplication.data.model.Movie;
import com.example.myapplication.databinding.ItemMovieVerticalBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieVerticalAdapter extends RecyclerView.Adapter<MovieVerticalAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public MovieVerticalAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieVerticalBinding binding = ItemMovieVerticalBinding.inflate(
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
        private final ItemMovieVerticalBinding binding;

        public MovieViewHolder(ItemMovieVerticalBinding binding) {
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
            binding.textTitle.setText(movie.getTitle());
            binding.textRating.setText(String.format(Locale.getDefault(), "%.1f", movie.getVoteAverage()));

            String backdropUrl = "https://image.tmdb.org/t/p/w780" + movie.getBackdropPath();
            Glide.with(itemView.getContext())
                    .load(backdropUrl)
                    .transform(new CenterCrop())
                    .into(binding.imageBackdrop);
        }
    }
}
