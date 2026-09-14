package com.example.bridge.core.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface GameDao {
    @Insert
    long insert(GameRecord record);

    @Query("SELECT * FROM game_history WHERE timestamp IN (SELECT timestamp FROM game_history WHERE id = :gameId)")
    List<GameRecord> getGamesByDealId(int gameId);

    @Query("SELECT * FROM game_history where system ='MyGame' GROUP BY timestamp ORDER BY timestamp DESC")
    List<GameRecord> getAllUniqueGames();

    @Query("DELETE from game_history where timestamp in (SELECT timestamp FROM game_history WHERE id = :gameId)")
    void deleteById(int gameId);

    @Query("UPDATE game_history SET isFavorite = :favorite WHERE timestamp IN (SELECT timestamp FROM game_history WHERE id = :gameId)")
    void updateFavoriteStatus(int gameId, boolean favorite);

    @Query("SELECT DISTINCT timestamp FROM game_history WHERE timestamp NOT IN (SELECT timestamp FROM game_history WHERE isFavorite = 1) ORDER BY timestamp ASC")
    List<Long> getNonFavoriteTimestampsAsc();

    @Query("DELETE FROM game_history WHERE timestamp = :timestamp AND isFavorite = 0")
    void deleteByTimestamp(long timestamp);
}

