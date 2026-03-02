package com.example.myapplication.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.myapplication.data.local.WatchlistItem;
import com.example.myapplication.data.repository.MediaRepository;

import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {

    private final MediaRepository repository;
    private final MutableLiveData<String> currentStatus = new MutableLiveData<>("All");
    private final LiveData<List<WatchlistItem>> watchlistItems;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaRepository(application);
        watchlistItems = Transformations.switchMap(currentStatus, status -> {
            if ("All".equals(status)) {
                return repository.getAllWatchlistItems();
            } else {
                return repository.getWatchlistItemsByStatus(status);
            }
        });
    }

    public void setStatusFilter(String status) {
        if (!status.equals(currentStatus.getValue())) {
            currentStatus.setValue(status);
        }
    }

    public LiveData<List<WatchlistItem>> getWatchlistItems() {
        return watchlistItems;
    }

    public void removeFromWatchlist(WatchlistItem item) {
        repository.removeFromWatchlist(item);
    }

    public void removeFromWatchlistById(int id) {
        repository.removeFromWatchlistById(id);
    }
}
