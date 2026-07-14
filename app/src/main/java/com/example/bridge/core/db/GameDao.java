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

    @Query("SELECT * FROM game_history GROUP BY timestamp ORDER BY timestamp DESC")
    List<GameRecord> getAllUniqueGames();

    @Query("SELECT * FROM game_history WHERE timestamp = :ts AND system = :sys LIMIT 1")
    GameRecord getSpecificSystem(long ts, String sys);

    @Update
    void update(GameRecord record);

    @Delete
    void delete(GameRecord record);

    @Query("DELETE FROM game_history WHERE id = :gameId")
    void deleteById(int gameId);

    @Query("UPDATE game_history SET isFavorite = :favorite WHERE id = :gameId")
    void updateSaveStatus(int gameId, boolean favorite);
}
