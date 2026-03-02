package com.example.myapplication.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.local.WatchlistItem;
import com.example.myapplication.data.model.MovieDetail;
import com.example.myapplication.data.repository.MediaRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailViewModel extends AndroidViewModel {

    private final MediaRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<MovieDetail> movieDetail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private LiveData<Boolean> isInWatchlist;
    private int currentMovieId = -1;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaRepository(application);
    }

    public void loadMovieDetails(int movieId) {
        if (movieId == currentMovieId && movieDetail.getValue() != null) {
            return; // Already loaded
        }

        currentMovieId = movieId;
        isInWatchlist = repository.isInWatchlist(movieId);

        isLoading.setValue(true);
        errorMessage.setValue(null);

        disposables.add(repository.getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> isLoading.setValue(false))
                .subscribe(
                        detail -> movieDetail.setValue(detail),
                        error -> errorMessage.setValue("Failed to load movie details")));
    }

    public void removeFromWatchlist() {
        MovieDetail detail = movieDetail.getValue();
        if (detail == null)
            return;
        repository.removeFromWatchlistById(detail.getId());
    }

    public void addToWatchlistWithStatus(String status) {
        MovieDetail detail = movieDetail.getValue();
        if (detail == null)
            return;

        WatchlistItem item = new WatchlistItem(
                detail.getId(),
                detail.getTitle(),
                detail.getPosterPath(),
                detail.getOverview(),
                "movie",
                detail.getVoteAverage(),
                detail.getReleaseDate(),
                status);
        repository.addToWatchlist(item);
    }

    public LiveData<MovieDetail> getMovieDetail() {
        return movieDetail;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsInWatchlist() {
        return isInWatchlist;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
