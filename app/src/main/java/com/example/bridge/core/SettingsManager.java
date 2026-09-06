package com.example.bridge.core;

import android.content.Context;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingsManager {
    private static SettingsManager instance;
    private final DataStoreManager dataStoreManager;

    public SettingsManager(Context context) {
        this.dataStoreManager = DataStoreManager.getInstance(context);
    }

    public static synchronized SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context);
        }
        return instance;
    }

    // --- LANGUAGE ---
    public String getLanguage(String defaultLanguage) {
        try {
            return dataStoreManager.getPreference(DataStoreManager.SELECTED_LANGUAGE, defaultLanguage)
                    .firstOrError()
                    .onErrorReturnItem(defaultLanguage)
                    .blockingGet();
        } catch (Exception e) {
            return defaultLanguage;
        }
    }

    public void setLanguage(String language) {
        dataStoreManager.setPreference(DataStoreManager.SELECTED_LANGUAGE, language)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    // --- CARD COLORS ---
    private Boolean cachedCardColorsColorful = null;

    public boolean isCardColorsColorful() {
        if (cachedCardColorsColorful != null) {
            return cachedCardColorsColorful;
        }
        try {
            boolean val = dataStoreManager.getPreference(DataStoreManager.CARD_COLORS_COLORFUL, true)
                    .firstOrError()
                    .onErrorReturnItem(true)
                    .blockingGet();
            cachedCardColorsColorful = val;
            return val;
        } catch (Exception e) {
            return true;
        }
    }

    public void setCardColorsColorful(boolean colorful) {
        cachedCardColorsColorful = colorful;
        try {
            dataStoreManager.setPreference(DataStoreManager.CARD_COLORS_COLORFUL, colorful)
                    .ignoreElement()
                    .onErrorComplete()
                    .blockingAwait();
        } catch (Exception ignored) {}
    }

    // --- QUICK GAME DIFFICULTY ---
    public String getQuickGameDifficulty() {
        try {
            return dataStoreManager.getPreference(DataStoreManager.QUICK_GAME_DIFFICULTY, "Medium")
                    .firstOrError()
                    .onErrorReturnItem("Medium")
                    .blockingGet();
        } catch (Exception e) {
            return "Medium";
        }
    }

    public void setQuickGameDifficulty(String difficulty) {
        dataStoreManager.setPreference(DataStoreManager.QUICK_GAME_DIFFICULTY, difficulty)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    // --- BIDDING SYSTEM ---
    public String getBiddingSystem() {
        try {
            return dataStoreManager.getPreference(DataStoreManager.BIDDING_SYSTEM, "SAYC")
                    .firstOrError()
                    .onErrorReturnItem("SAYC")
                    .blockingGet();
        } catch (Exception e) {
            return "SAYC";
        }
    }

    public void setBiddingSystem(String system) {
        dataStoreManager.setPreference(DataStoreManager.BIDDING_SYSTEM, system)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
