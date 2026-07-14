package com.example.bridge.core.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_history")
public class GameRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public long timestamp;   // To allow grouping systems from the same deal
    public String system;    // "QuckGame", "NatC", etc.
    public String gameData;  // The game data JSON as String
    public boolean isFavorite;  // Is it marked as favorite
}
