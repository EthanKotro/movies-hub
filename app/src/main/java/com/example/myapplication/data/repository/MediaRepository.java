package com.example.myapplication.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.data.local.MediaVaultDatabase;
import com.example.myapplication.data.local.WatchlistDao;
import com.example.myapplication.data.local.WatchlistItem;
import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.Movie;
import com.example.myapplication.data.model.MovieDetail;
import com.example.myapplication.data.model.TvShow;
import com.example.myapplication.data.remote.RetrofitClient;
import com.example.myapplication.data.remote.TmdbApiService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Single;

public class MediaRepository {

    private final TmdbApiService apiService;
    private final WatchlistDao watchlistDao;
    private final ExecutorService executorService;

    public MediaRepository(Application application) {
        apiService = RetrofitClient.getInstance().getApiService();
        MediaVaultDatabase database = MediaVaultDatabase.getInstance(application);
        watchlistDao = database.watchlistDao();
        executorService = Executors.newFixedThreadPool(3);
    }

    // ==================== API Calls ====================

    public Single<ApiResponse<Movie>> getTrendingMovies(int page) {
        return apiService.getTrendingMovies(page);
    }

    public Single<ApiResponse<Movie>> getTopRatedMovies(int page) {
        return apiService.getTopRatedMovies(page);
    }

    public Single<ApiResponse<Movie>> getUpcomingMovies(int page) {
        return apiService.getUpcomingMovies(page);
    }

    public Single<ApiResponse<Movie>> getPopularMovies(int page) {
        return apiService.getPopularMovies(page);
    }

    public Single<ApiResponse<Movie>> getNowPlayingMovies(int page) {
        return apiService.getNowPlayingMovies(page);
    }

    public Single<ApiResponse<Movie>> searchMovies(String query, int page) {
        return apiService.searchMovies(query, page);
    }

    public Single<MovieDetail> getMovieDetails(int movieId) {
        return apiService.getMovieDetails(movieId, "credits,videos");
    }

    public Single<ApiResponse<TvShow>> getTrendingTvShows(int page) {
        return apiService.getTrendingTvShows(page);
    }

    public Single<ApiResponse<TvShow>> searchTvShows(String query, int page) {
        return apiService.searchTvShows(query, page);
    }

    public Single<ApiResponse<Movie>> discoverMovies(int page, String genreId) {
        return apiService.discoverMovies(page, "popularity.desc", genreId);
    }

    public Single<ApiResponse<Movie>> getMovieRecommendations(int movieId, int page) {
        return apiService.getMovieRecommendations(movieId, page);
    }

    // ==================== Watchlist (Room) ====================

    public LiveData<List<WatchlistItem>> getAllWatchlistItems() {
        return watchlistDao.getAllItems();
    }

    public LiveData<List<WatchlistItem>> getWatchlistItemsByStatus(String status) {
        return watchlistDao.getItemsByStatus(status);
    }

    public Single<List<WatchlistItem>> getRecentWatchlistItems() {
        return Single.fromCallable(watchlistDao::getRecentWatchlistItemsSync);
    }

    public LiveData<Boolean> isInWatchlist(int id) {
        return watchlistDao.isInWatchlist(id);
    }

    public LiveData<WatchlistItem> getWatchlistItemById(int id) {
        return watchlistDao.getItemById(id);
    }

    public LiveData<Integer> getWatchlistCount() {
        return watchlistDao.getWatchlistCount();
    }

    public void addToWatchlist(WatchlistItem item) {
        executorService.execute(() -> watchlistDao.insert(item));
    }

    public void removeFromWatchlist(WatchlistItem item) {
        executorService.execute(() -> watchlistDao.delete(item));
    }

    public void removeFromWatchlistById(int id) {
        executorService.execute(() -> watchlistDao.deleteById(id));
    }
}
