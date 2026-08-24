package com.example.bridge.core;

import android.content.Context;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataStore {
    private final DataStoreManager dataStoreManager;

    public DataStore(Context context) {
        this.dataStoreManager = DataStoreManager.getInstance(context);
    }

    public int getGamesPlayed() {
        try {
            return dataStoreManager.getPreference(DataStoreManager.GAMES_PLAYED, 0)
                    .firstOrError()
                    .onErrorReturnItem(0)
                    .blockingGet();
        } catch (Exception e) {
            return 0;
        }
    }

    public void incrementGamesPlayed() {
        int current = getGamesPlayed();
        dataStoreManager.setPreference(DataStoreManager.GAMES_PLAYED, current + 1)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public double getCareerImp(String gameMode) {
        androidx.datastore.preferences.core.Preferences.Key<Double> key =
                "single".equals(gameMode) ? DataStoreManager.CAREER_IMP_SINGLE : DataStoreManager.CAREER_IMP_QUICK;
        try {
            return dataStoreManager.getPreference(key, 0.0)
                    .firstOrError()
                    .onErrorReturnItem(0.0)
                    .blockingGet();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void addCareerImp(String gameMode, double impToAdd) {
        androidx.datastore.preferences.core.Preferences.Key<Double> key =
                "single".equals(gameMode) ? DataStoreManager.CAREER_IMP_SINGLE : DataStoreManager.CAREER_IMP_QUICK;
        
        double current = getCareerImp(gameMode);
        double newValue = Math.round((current + impToAdd) * 10.0) / 10.0;
        dataStoreManager.setPreference(key, newValue)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
