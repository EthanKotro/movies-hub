package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.local.WatchlistItem;
import com.example.myapplication.databinding.FragmentWatchlistBinding;
import com.example.myapplication.ui.adapter.WatchlistAdapter;
import com.example.myapplication.ui.viewmodel.WatchlistViewModel;
import com.google.android.material.snackbar.Snackbar;

public class WatchlistFragment extends Fragment implements WatchlistAdapter.OnItemInteractionListener {

    private FragmentWatchlistBinding binding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory())
                .get(WatchlistViewModel.class);

        setupRecyclerView();
        setupSwipeToDelete();
        setupTabs();
        observeViewModel();
    }

    private void setupTabs() {
        binding.tabLayout
                .addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                        if (tab.getText() != null) {
                            viewModel.setStatusFilter(tab.getText().toString());
                        }
                    }

                    @Override
                    public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {
                    }
                });
    }

    private void setupRecyclerView() {
        adapter = new WatchlistAdapter(this);
        binding.recyclerWatchlist.setAdapter(adapter);
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                WatchlistItem item = adapter.getCurrentList().get(position);
                viewModel.removeFromWatchlist(item);

                Snackbar.make(binding.getRoot(), "Removed from Watchlist", Snackbar.LENGTH_LONG).show();
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerWatchlist);
    }

    private void observeViewModel() {
        viewModel.getWatchlistItems().observe(getViewLifecycleOwner(), items -> {
            boolean isEmpty = items == null || items.isEmpty();
            binding.emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.recyclerWatchlist.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

            if (!isEmpty) {
                adapter.submitList(items);
            }
        });
    }

    @Override
    public void onItemClick(WatchlistItem item) {
        Intent intent = new Intent(requireActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(WatchlistItem item) {
        viewModel.removeFromWatchlist(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
