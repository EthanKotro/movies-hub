package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Movie;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.adapter.MoviePosterAdapter;
import com.example.myapplication.ui.adapter.MovieVerticalAdapter;
import com.example.myapplication.ui.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment
        implements MoviePosterAdapter.OnItemClickListener, MovieVerticalAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    private MoviePosterAdapter trendingAdapter;
    private MoviePosterAdapter topRatedAdapter;
    private MoviePosterAdapter upcomingAdapter;
    private MoviePosterAdapter recommendedAdapter;
    private MovieVerticalAdapter discoverAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory())
                .get(HomeViewModel.class);

        setupRecyclerViews();
        setupGenreFilters();
        observeViewModel();
    }

    private void setupGenreFilters() {
        binding.chipGroupGenres.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                viewModel.setGenreId(null);
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chip_action)
                    viewModel.setGenreId("28");
                else if (checkedId == R.id.chip_comedy)
                    viewModel.setGenreId("35");
                else if (checkedId == R.id.chip_drama)
                    viewModel.setGenreId("18");
                else if (checkedId == R.id.chip_scifi)
                    viewModel.setGenreId("878");
            }
        });
    }

    private void setupRecyclerViews() {
        trendingAdapter = new MoviePosterAdapter(this);
        binding.recyclerTrending.setAdapter(trendingAdapter);

        topRatedAdapter = new MoviePosterAdapter(this);
        binding.recyclerTopRated.setAdapter(topRatedAdapter);

        upcomingAdapter = new MoviePosterAdapter(this);
        binding.recyclerUpcoming.setAdapter(upcomingAdapter);

        recommendedAdapter = new MoviePosterAdapter(this);
        binding.recyclerRecommended.setAdapter(recommendedAdapter);

        discoverAdapter = new MovieVerticalAdapter(this);
        binding.recyclerDiscover.setAdapter(discoverAdapter);
    }

    private void observeViewModel() {
        viewModel.getTrendingMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null)
                trendingAdapter.setMovies(movies);
        });

        viewModel.getTopRatedMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null)
                topRatedAdapter.setMovies(movies);
        });

        viewModel.getUpcomingMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null)
                upcomingAdapter.setMovies(movies);
        });

        viewModel.getDiscoverMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null)
                discoverAdapter.setMovies(movies);
        });

        viewModel.getRecommendedMovies().observe(getViewLifecycleOwner(), movies -> {
            boolean hasRecommendations = movies != null && !movies.isEmpty();
            binding.textRecommended.setVisibility(hasRecommendations ? View.VISIBLE : View.GONE);
            binding.recyclerRecommended.setVisibility(hasRecommendations ? View.VISIBLE : View.GONE);
            if (hasRecommendations) {
                recommendedAdapter.setMovies(movies);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
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
