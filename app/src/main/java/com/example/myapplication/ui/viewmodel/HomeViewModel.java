package com.example.myapplication.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.Movie;
import com.example.myapplication.data.repository.MediaRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private final MediaRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Movie>> trendingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> topRatedMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> upcomingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> discoverMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> recommendedMovies = new MutableLiveData<>();

    private String currentGenreId = null;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaRepository(application);
        loadHomeData();
    }

    public void loadHomeData() {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        fetchTrending();
        fetchTopRated();
        fetchUpcoming();
        fetchRecommendations();
        fetchDiscover();
    }

    public void setGenreId(String genreId) {
        if ((currentGenreId == null && genreId == null) || (currentGenreId != null && currentGenreId.equals(genreId))) {
            return;
        }
        currentGenreId = genreId;
        fetchDiscover();
    }

    private void fetchTrending() {
        disposables.add(repository.getTrendingMovies(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> trendingMovies.setValue(response.getResults()),
                        error -> handleError("Failed to load trending movies")));
    }

    private void fetchTopRated() {
        disposables.add(repository.getTopRatedMovies(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> topRatedMovies.setValue(response.getResults()),
                        error -> handleError("Failed to load top rated movies")));
    }

    private void fetchUpcoming() {
        disposables.add(repository.getUpcomingMovies(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> upcomingMovies.setValue(response.getResults()),
                        error -> handleError("Failed to load upcoming movies")));
    }

    private void fetchRecommendations() {
        disposables.add(repository.getRecentWatchlistItems()
                .subscribeOn(Schedulers.io())
                .flatMap(items -> {
                    if (items != null && !items.isEmpty()) {
                        return repository.getMovieRecommendations(items.get(0).getId(), 1);
                    } else {
                        return Single.just(new ApiResponse<Movie>());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> recommendedMovies.setValue(response.getResults()),
                        error -> recommendedMovies.setValue(null)));
    }

    private void fetchDiscover() {
        disposables.add(repository.discoverMovies(1, currentGenreId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> isLoading.setValue(false))
                .subscribe(
                        response -> discoverMovies.setValue(response.getResults()),
                        error -> handleError("Failed to discover movies")));
    }

    private void handleError(String message) {
        isLoading.setValue(false);
        errorMessage.setValue(message);
    }

    public LiveData<List<Movie>> getTrendingMovies() {
        return trendingMovies;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return topRatedMovies;
    }

    public LiveData<List<Movie>> getUpcomingMovies() {
        return upcomingMovies;
    }

    public LiveData<List<Movie>> getDiscoverMovies() {
        return discoverMovies;
    }

    public LiveData<List<Movie>> getRecommendedMovies() {
        return recommendedMovies;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
