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
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchViewModel extends AndroidViewModel {

    private final MediaRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<String> searchSubject = PublishSubject.create();

    private final MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaRepository(application);
        setupSearch();
    }

    private void setupSearch() {
        disposables.add(searchSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(query -> !query.trim().isEmpty())
                .distinctUntilChanged()
                .switchMapSingle(query -> {
                    isLoading.postValue(true);
                    return repository.searchMovies(query, 1)
                            .subscribeOn(Schedulers.io())
                            .onErrorResumeNext(throwable -> {
                                errorMessage.postValue("Search failed");
                                isLoading.postValue(false);
                                return io.reactivex.rxjava3.core.Single.just(new ApiResponse<>());
                            });
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            isLoading.setValue(false);
                            if (response.getResults() != null) {
                                searchResults.setValue(response.getResults());
                            }
                        },
                        error -> {
                            isLoading.setValue(false);
                            errorMessage.setValue("Error performing search");
                        }));
    }

    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchResults.setValue(null); // Clear results
            return;
        }
        searchSubject.onNext(query);
    }

    public LiveData<List<Movie>> getSearchResults() {
        return searchResults;
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
