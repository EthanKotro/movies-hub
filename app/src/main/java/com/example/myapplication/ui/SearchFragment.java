package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.data.model.Movie;
import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.ui.adapter.SearchResultAdapter;
import com.example.myapplication.ui.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment implements SearchResultAdapter.OnItemClickListener {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private SearchResultAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory())
                .get(SearchViewModel.class);

        setupRecyclerView();
        setupSearchView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        adapter = new SearchResultAdapter(this);
        binding.recyclerSearch.setAdapter(adapter);
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.search(query);
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.search(newText);
                return true;
            }
        });
    }

    private void observeViewModel() {
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                adapter.setMovies(movies);
                binding.emptyState.setVisibility(View.GONE);
                binding.recyclerSearch.setVisibility(View.VISIBLE);
            } else {
                adapter.setMovies(null); // Clear
                binding.emptyState.setVisibility(View.VISIBLE);
                binding.recyclerSearch.setVisibility(View.GONE);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(requireActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, movie.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
