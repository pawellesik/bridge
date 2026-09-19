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

    // --- QUICK GAME WINNING ONLY ---
    private Boolean cachedQuickGameWinningOnly = null;

    public boolean isQuickGameWinningOnly() {
        if (cachedQuickGameWinningOnly != null) {
            return cachedQuickGameWinningOnly;
        }
        try {
            boolean val = dataStoreManager.getPreference(DataStoreManager.QUICK_GAME_WINNING_ONLY, true)
                    .firstOrError()
                    .onErrorReturnItem(true)
                    .blockingGet();
            cachedQuickGameWinningOnly = val;
            return val;
        } catch (Exception e) {
            return true;
        }
    }

    public void setQuickGameWinningOnly(boolean winningOnly) {
        cachedQuickGameWinningOnly = winningOnly;
        try {
            dataStoreManager.setPreference(DataStoreManager.QUICK_GAME_WINNING_ONLY, winningOnly)
                    .ignoreElement()
                    .onErrorComplete()
                    .blockingAwait();
        } catch (Exception ignored) {}
    }

    // --- HISTORY SIZE ---
    private Integer cachedHistorySize = null;

    public int getHistorySize() {
        if (cachedHistorySize != null) {
            return cachedHistorySize;
        }
        try {
            int val = dataStoreManager.getPreference(DataStoreManager.HISTORY_SIZE, 100)
                    .firstOrError()
                    .onErrorReturnItem(100)
                    .blockingGet();
            cachedHistorySize = val;
            return val;
        } catch (Exception e) {
            return 100;
        }
    }

    public void setHistorySize(int size) {
        cachedHistorySize = size;
        try {
            dataStoreManager.setPreference(DataStoreManager.HISTORY_SIZE, size)
                    .ignoreElement()
                    .onErrorComplete()
                    .blockingAwait();
        } catch (Exception ignored) {}
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

    // --- LOAD FROM TEST.PBN ---
    private Boolean cachedLoadFromTestPbn = null;

    public boolean isLoadFromTestPbn() {
        if (cachedLoadFromTestPbn != null) {
            return cachedLoadFromTestPbn;
        }
        try {
            boolean val = dataStoreManager.getPreference(DataStoreManager.LOAD_FROM_TEST_PBN, false)
                    .firstOrError()
                    .onErrorReturnItem(false)
                    .blockingGet();
            cachedLoadFromTestPbn = val;
            return val;
        } catch (Exception e) {
            return false;
        }
    }

    public void setLoadFromTestPbn(boolean load) {
        cachedLoadFromTestPbn = load;
        try {
            dataStoreManager.setPreference(DataStoreManager.LOAD_FROM_TEST_PBN, load)
                    .ignoreElement()
                    .onErrorComplete()
                    .blockingAwait();
        } catch (Exception ignored) {}
    }

    // --- SHOW EXPLANATION ---
    private Boolean cachedShowExplanation = null;

    public boolean isShowExplanation() {
        if (cachedShowExplanation != null) {
            return cachedShowExplanation;
        }
        try {
            boolean val = dataStoreManager.getPreference(DataStoreManager.SHOW_EXPLANATION, true)
                    .firstOrError()
                    .onErrorReturnItem(true)
                    .blockingGet();
            cachedShowExplanation = val;
            return val;
        } catch (Exception e) {
            return true;
        }
    }

    public void setShowExplanation(boolean show) {
        cachedShowExplanation = show;
        try {
            dataStoreManager.setPreference(DataStoreManager.SHOW_EXPLANATION, show)
                    .ignoreElement()
                    .onErrorComplete()
                    .blockingAwait();
        } catch (Exception ignored) {}
    }
}
