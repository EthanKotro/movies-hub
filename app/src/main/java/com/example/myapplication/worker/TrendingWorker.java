package com.example.myapplication.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.rxjava3.RxWorker;
import androidx.work.WorkerParameters;

import com.example.myapplication.data.model.ApiResponse;
import com.example.myapplication.data.model.Movie;
import com.example.myapplication.data.remote.RetrofitClient;
import com.example.myapplication.data.remote.TmdbApiService;

import io.reactivex.rxjava3.core.Single;

public class TrendingWorker extends RxWorker {

    private static final String TAG = "TrendingWorker";
    private final TmdbApiService apiService;

    public TrendingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        apiService = RetrofitClient.getInstance().getApiService();
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        Log.d(TAG, "Starting trending sync work");

        return apiService.getTrendingMovies(1)
                .map(response -> {
                    if (response.getResults() != null && !response.getResults().isEmpty()) {
                        Movie topMovie = response.getResults().get(0);
                        NotificationHelper.showTrendingNotification(
                                getApplicationContext(),
                                topMovie.getId(),
                                topMovie.getTitle(),
                                topMovie.getPosterPath());
                        return Result.success();
                    } else {
                        return Result.failure();
                    }
                })
                .onErrorReturn(throwable -> {
                    Log.e(TAG, "Error fetching trending movies: " + throwable.getMessage());
                    return Result.retry();
                });
    }
}
