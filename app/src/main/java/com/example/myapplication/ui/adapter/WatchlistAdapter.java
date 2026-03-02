package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.myapplication.data.local.WatchlistItem;
import com.example.myapplication.databinding.ItemWatchlistBinding;

import java.util.Locale;

public class WatchlistAdapter extends ListAdapter<WatchlistItem, WatchlistAdapter.WatchlistViewHolder> {

    private final OnItemInteractionListener listener;

    public interface OnItemInteractionListener {
        void onItemClick(WatchlistItem item);

        void onDeleteClick(WatchlistItem item);
    }

    public WatchlistAdapter(OnItemInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<WatchlistItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<WatchlistItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull WatchlistItem oldItem, @NonNull WatchlistItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull WatchlistItem oldItem, @NonNull WatchlistItem newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getVoteAverage() == newItem.getVoteAverage();
        }
    };

    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWatchlistBinding binding = ItemWatchlistBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new WatchlistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class WatchlistViewHolder extends RecyclerView.ViewHolder {
        private final ItemWatchlistBinding binding;

        public WatchlistViewHolder(ItemWatchlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getItem(position));
                }
            });

            binding.btnDelete.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(getItem(position));
                }
            });
        }

        public void bind(WatchlistItem item) {
            binding.textTitle.setText(item.getTitle());
            binding.textRating.setText(String.format(Locale.getDefault(), "%.1f", item.getVoteAverage()));
            binding.textDate.setText("Release: " + (item.getReleaseDate() != null ? item.getReleaseDate() : "N/A"));

            String posterUrl = "https://image.tmdb.org/t/p/w342" + item.getPosterPath();
            Glide.with(itemView.getContext())
                    .load(posterUrl)
                    .transform(new CenterCrop())
                    .into(binding.imagePoster);
        }
    }
}
