package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.myapplication.data.model.CastMember;
import com.example.myapplication.databinding.ItemCastBinding;

import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<CastMember> castMembers = new ArrayList<>();

    public void setCast(List<CastMember> newCast) {
        this.castMembers = newCast;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCastBinding binding = ItemCastBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        holder.bind(castMembers.get(position));
    }

    @Override
    public int getItemCount() {
        return castMembers != null ? castMembers.size() : 0;
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {
        private final ItemCastBinding binding;

        public CastViewHolder(ItemCastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CastMember castMember) {
            binding.textName.setText(castMember.getName());
            binding.textCharacter.setText(castMember.getCharacter());

            if (castMember.getProfilePath() != null) {
                String profileUrl = "https://image.tmdb.org/t/p/w185" + castMember.getProfilePath();
                Glide.with(itemView.getContext())
                        .load(profileUrl)
                        .transform(new CircleCrop())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.imageProfile);
            } else {
                Glide.with(itemView.getContext()).clear(binding.imageProfile);
            }
        }
    }
}
