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

    @Query("SELECT * FROM game_history where system ='MyGame' GROUP BY timestamp ORDER BY timestamp DESC")
    List<GameRecord> getAllUniqueGames();

    @Query("DELETE from game_history where timestamp in (SELECT timestamp FROM game_history WHERE id = :gameId)")
    void deleteById(int gameId);

    @Query("UPDATE game_history SET isFavorite = :favorite WHERE id = :gameId")
    void updateFavoriteStatus(int gameId, boolean favorite);
}

