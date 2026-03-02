package com.example.myapplication.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WatchlistDao {

    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    LiveData<List<WatchlistItem>> getAllItems();

    @Query("SELECT * FROM watchlist WHERE status = :status ORDER BY addedAt DESC")
    LiveData<List<WatchlistItem>> getItemsByStatus(String status);

    // For background recommendations (non-LiveData)
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC LIMIT 10")
    List<WatchlistItem> getRecentWatchlistItemsSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WatchlistItem item);

    @Delete
    void delete(WatchlistItem item);

    @Query("DELETE FROM watchlist WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM watchlist WHERE id = :id LIMIT 1")
    LiveData<WatchlistItem> getItemById(int id);

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE id = :id)")
    LiveData<Boolean> isInWatchlist(int id);

    @Query("SELECT COUNT(*) FROM watchlist")
    LiveData<Integer> getWatchlistCount();
}
