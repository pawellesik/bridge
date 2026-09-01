package com.example.bridge.core;

import android.content.Context;

public class DataStore {
    private final StatsManager statsManager;
    private final SettingsManager settingsManager;

    public DataStore(Context context) {
        this.statsManager = new StatsManager(context);
        this.settingsManager = SettingsManager.getInstance(context);
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public int getGamesPlayed() {
        return statsManager.getGamesPlayed();
    }

    public void incrementGamesPlayed() {
        statsManager.incrementGamesPlayed();
    }

    public double getCareerImp(String gameMode) {
        return statsManager.getCareerImp(gameMode);
    }

    public void addCareerImp(String gameMode, double impToAdd) {
        statsManager.addCareerImp(gameMode, impToAdd);
    }
}
