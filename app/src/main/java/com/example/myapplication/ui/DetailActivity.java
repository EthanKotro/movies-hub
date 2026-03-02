package com.example.myapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.myapplication.R;
import com.example.myapplication.data.model.MovieDetail;
import com.example.myapplication.data.model.Video;
import com.example.myapplication.databinding.ActivityDetailBinding;
import com.example.myapplication.ui.adapter.CastAdapter;
import com.example.myapplication.ui.viewmodel.DetailViewModel;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "movie_id";

    private ActivityDetailBinding binding;
    private DetailViewModel viewModel;
    private CastAdapter castAdapter;
    private String trailerUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
        if (movieId == -1) {
            Toast.makeText(this, "Invalid Movie ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory())
                .get(DetailViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupListeners();
        viewModel.loadMovieDetails(movieId);
        observeViewModel();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        castAdapter = new CastAdapter();
        binding.recyclerCast.setAdapter(castAdapter);
    }

    private void setupListeners() {
        binding.fabWatchlist.setOnClickListener(v -> {
            Boolean inWatchlist = viewModel.getIsInWatchlist().getValue();
            if (inWatchlist != null && inWatchlist) {
                viewModel.removeFromWatchlist();
            } else {
                showWatchlistCategoryDialog();
            }
        });

        binding.btnTrailer.setOnClickListener(v -> {
            if (trailerUrl != null) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Could not play trailer", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No trailer available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWatchlistCategoryDialog() {
        String[] categories = { "Plan to Watch", "Currently Watching", "Completed" };
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Select Category")
                .setItems(categories, (dialog, which) -> {
                    String selectedStatus = categories[which];
                    viewModel.addToWatchlistWithStatus(selectedStatus);
                })
                .show();
    }

    private void observeViewModel() {
        viewModel.getMovieDetail().observe(this, this::bindMovieDetail);

        viewModel.getIsInWatchlist().observe(this, inWatchlist -> {
            if (inWatchlist != null && inWatchlist) {
                binding.fabWatchlist.setImageResource(R.drawable.ic_bookmark);
            } else {
                binding.fabWatchlist.setImageResource(R.drawable.ic_bookmark_outline);
            }
        });

        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bindMovieDetail(MovieDetail detail) {
        if (detail == null)
            return;

        binding.textTitle.setText(detail.getTitle());
        binding.textRating.setText(String.format(Locale.getDefault(), "%.1f / 10", detail.getVoteAverage()));

        String dateString = detail.getReleaseDate() != null ? detail.getReleaseDate() : "N/A";
        binding.textDateRuntime.setText(dateString + " • " + detail.getRuntime() + "m");

        if (detail.getGenres() != null && !detail.getGenres().isEmpty()) {
            StringBuilder genres = new StringBuilder();
            for (int i = 0; i < detail.getGenres().size(); i++) {
                genres.append(detail.getGenres().get(i).getName());
                if (i < detail.getGenres().size() - 1)
                    genres.append(", ");
            }
            binding.textGenres.setText(genres.toString());
        } else {
            binding.textGenres.setVisibility(View.GONE);
        }

        binding.textOverview.setText(detail.getOverview());

        // Load Images
        if (detail.getBackdropPath() != null) {
            String backdropUrl = "https://image.tmdb.org/t/p/w1280" + detail.getBackdropPath();
            Glide.with(this).load(backdropUrl).into(binding.imageBackdrop);
        }

        if (detail.getPosterPath() != null) {
            String posterUrl = "https://image.tmdb.org/t/p/w342" + detail.getPosterPath();
            Glide.with(this).load(posterUrl).transform(new CenterCrop()).into(binding.imagePoster);
        }

        // Cast
        if (detail.getCredits() != null && detail.getCredits().getCast() != null) {
            castAdapter.setCast(detail.getCredits().getCast());
        }

        // Trailer
        binding.btnTrailer.setVisibility(View.GONE);
        if (detail.getVideos() != null && detail.getVideos().getResults() != null) {
            for (Video video : detail.getVideos().getResults()) {
                if ("YouTube".equals(video.getSite()) && "Trailer".equals(video.getType())) {
                    trailerUrl = "https://www.youtube.com/watch?v=" + video.getKey();
                    binding.btnTrailer.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }
}
