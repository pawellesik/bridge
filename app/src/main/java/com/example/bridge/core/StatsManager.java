package com.example.bridge.core;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;

import java.util.Locale;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class StatsManager {

    public static class ModeStats {
        public final int games;
        public final int deals;
        public final int concedes;
        public final double imp;
        public final double maxImp;
        public final int wins;

        public ModeStats(int games, int deals, int concedes, double imp, double maxImp, int wins) {
            this.games = games;
            this.deals = deals;
            this.concedes = concedes;
            this.imp = imp;
            this.maxImp = maxImp;
            this.wins = wins;
        }

        public int getWinPercentage() {
            if (games <= 0) return 0;
            return (int) Math.round((double) wins * 100.0 / (double) games);
        }

        public String getWinsFormatted() {
            return wins + " (" + getWinPercentage() + "%)";
        }

        public String getImpFormatted() {
            return formatImpWithDecimal(imp);
        }

        public String getMaxImpFormatted() {
            return formatImpWithDecimal(maxImp);
        }

        private static String formatImpWithDecimal(double val) {
            if (val > 0) {
                return String.format(Locale.US, "+%.1f", val);
            } else if (val < 0) {
                return String.format(Locale.US, "%.1f", val);
            } else {
                return "0.0";
            }
        }
    }

    private final DataStoreManager dataStoreManager;

    public StatsManager(Context context) {
        this.dataStoreManager = DataStoreManager.getInstance(context);
    }

    // --- INCREMENTATION METHODS ---
    public void incrementGames(String gameMode) {
        incrementGamesPlayed();
        if ("single".equalsIgnoreCase(gameMode) || "Singleplayer".equalsIgnoreCase(gameMode) || "SP".equalsIgnoreCase(gameMode)) {
            incrementSingleValue(DataStoreManager.STAT_SESSION_GAMES_SP);
            incrementSingleValue(DataStoreManager.STAT_GLOBAL_GAMES_SP);
        } else if ("quick".equalsIgnoreCase(gameMode) || "Just Declare".equalsIgnoreCase(gameMode) || "JD".equalsIgnoreCase(gameMode)) {
            incrementSingleValue(DataStoreManager.STAT_SESSION_GAMES_JD);
            incrementSingleValue(DataStoreManager.STAT_GLOBAL_GAMES_JD);
        } else if ("multi".equalsIgnoreCase(gameMode) || "Multiplayer".equalsIgnoreCase(gameMode) || "MP".equalsIgnoreCase(gameMode)) {
            incrementSingleValue(DataStoreManager.STAT_SESSION_GAMES_MP);
            incrementSingleValue(DataStoreManager.STAT_GLOBAL_GAMES_MP);
        }
    }

    public void incrementDeals(String gameMode) {
        if ("single".equalsIgnoreCase(gameMode) || "Singleplayer".equalsIgnoreCase(gameMode) || "SP".equalsIgnoreCase(gameMode)) {
            incrementSingleValue(DataStoreManager.STAT_SESSION_DEALS_SP);
            incrementSingleValue(DataStoreManager.STAT_GLOBAL_DEALS_SP);
        } else if ("quick".equalsIgnoreCase(gameMode) || "Just Declare".equalsIgnoreCase(gameMode) || "JD".equalsIgnoreCase(gameMode)) {
            incrementSingleValue(DataStoreManager.STAT_SESSION_DEALS_JD);
            incrementSingleValue(DataStoreManager.STAT_GLOBAL_DEALS_JD);
        } else if ("multi".equalsIgnoreCase(gameMode) || "Multiplayer".equalsIgnoreCase(gameMode) || "MP".equalsIgnoreCase(gameMode)) {
            incrementSingleValue(DataStoreManager.STAT_SESSION_DEALS_MP);
            incrementSingleValue(DataStoreManager.STAT_GLOBAL_DEALS_MP);
        }
    }

    private void incrementSingleValue(Preferences.Key<Integer> key) {
        int current = dataStoreManager.getPreference(key, 0).blockingFirst(0);
        dataStoreManager.setPreference(key, current + 1)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    // --- CAREER IMP AND GAMES PLAYED ---
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
        Preferences.Key<Double> key =
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
        Preferences.Key<Double> key =
                "single".equals(gameMode) ? DataStoreManager.CAREER_IMP_SINGLE : DataStoreManager.CAREER_IMP_QUICK;

        double current = getCareerImp(gameMode);
        double newValue = Math.round((current + impToAdd) * 10.0) / 10.0;
        dataStoreManager.setPreference(key, newValue)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    // --- SESSION STATS ---
    public ModeStats getSessionStatsJustDeclare() {
        return getStats(
                DataStoreManager.STAT_SESSION_GAMES_JD,
                DataStoreManager.STAT_SESSION_DEALS_JD,
                DataStoreManager.STAT_SESSION_CONCEDES_JD,
                DataStoreManager.STAT_SESSION_IMP_JD,
                DataStoreManager.STAT_SESSION_MAX_IMP_JD,
                DataStoreManager.STAT_SESSION_WINS_JD
        );
    }

    public ModeStats getSessionStatsSingleplayer() {
        return getStats(
                DataStoreManager.STAT_SESSION_GAMES_SP,
                DataStoreManager.STAT_SESSION_DEALS_SP,
                DataStoreManager.STAT_SESSION_CONCEDES_SP,
                DataStoreManager.STAT_SESSION_IMP_SP,
                DataStoreManager.STAT_SESSION_MAX_IMP_SP,
                DataStoreManager.STAT_SESSION_WINS_SP
        );
    }

    public ModeStats getSessionStatsMultiplayer() {
        return getStats(
                DataStoreManager.STAT_SESSION_GAMES_MP,
                DataStoreManager.STAT_SESSION_DEALS_MP,
                DataStoreManager.STAT_SESSION_CONCEDES_MP,
                DataStoreManager.STAT_SESSION_IMP_MP,
                DataStoreManager.STAT_SESSION_MAX_IMP_MP,
                DataStoreManager.STAT_SESSION_WINS_MP
        );
    }

    // --- GLOBAL STATS ---
    public ModeStats getGlobalStatsJustDeclare() {
        return getStats(
                DataStoreManager.STAT_GLOBAL_GAMES_JD,
                DataStoreManager.STAT_GLOBAL_DEALS_JD,
                DataStoreManager.STAT_GLOBAL_CONCEDES_JD,
                DataStoreManager.STAT_GLOBAL_IMP_JD,
                DataStoreManager.STAT_GLOBAL_MAX_IMP_JD,
                DataStoreManager.STAT_GLOBAL_WINS_JD
        );
    }

    public ModeStats getGlobalStatsSingleplayer() {
        return getStats(
                DataStoreManager.STAT_GLOBAL_GAMES_SP,
                DataStoreManager.STAT_GLOBAL_DEALS_SP,
                DataStoreManager.STAT_GLOBAL_CONCEDES_SP,
                DataStoreManager.STAT_GLOBAL_IMP_SP,
                DataStoreManager.STAT_GLOBAL_MAX_IMP_SP,
                DataStoreManager.STAT_GLOBAL_WINS_SP
        );
    }

    public ModeStats getGlobalStatsMultiplayer() {
        return getStats(
                DataStoreManager.STAT_GLOBAL_GAMES_MP,
                DataStoreManager.STAT_GLOBAL_DEALS_MP,
                DataStoreManager.STAT_GLOBAL_CONCEDES_MP,
                DataStoreManager.STAT_GLOBAL_IMP_MP,
                DataStoreManager.STAT_GLOBAL_MAX_IMP_MP,
                DataStoreManager.STAT_GLOBAL_WINS_MP
        );
    }

    private ModeStats getStats(
            Preferences.Key<Integer> keyGames,
            Preferences.Key<Integer> keyDeals,
            Preferences.Key<Integer> keyConcedes,
            Preferences.Key<Double> keyImp,
            Preferences.Key<Double> keyMaxImp,
            Preferences.Key<Integer> keyWins
    ) {
        try {
            int games = dataStoreManager.getPreference(keyGames, 0).blockingFirst(0);
            int deals = dataStoreManager.getPreference(keyDeals, 0).blockingFirst(0);
            int concedes = dataStoreManager.getPreference(keyConcedes, 0).blockingFirst(0);
            double imp = dataStoreManager.getPreference(keyImp, 0.0).blockingFirst(0.0);
            double maxImp = dataStoreManager.getPreference(keyMaxImp, 0.0).blockingFirst(0.0);
            int wins = dataStoreManager.getPreference(keyWins, 0).blockingFirst(0);
            return new ModeStats(games, deals, concedes, imp, maxImp, wins);
        } catch (Exception e) {
            return new ModeStats(0, 0, 0, 0.0, 0.0, 0);
        }
    }

    // --- CLEAR SESSION STATS ---
    public void clearSessionStats(String mode) {
        if ("Just Declare".equalsIgnoreCase(mode) || "JD".equalsIgnoreCase(mode) || "quick".equalsIgnoreCase(mode)) {
            clearIntKeys(
                    DataStoreManager.STAT_SESSION_GAMES_JD,
                    DataStoreManager.STAT_SESSION_DEALS_JD,
                    DataStoreManager.STAT_SESSION_CONCEDES_JD,
                    DataStoreManager.STAT_SESSION_WINS_JD
            );
            clearDblKeys(
                    DataStoreManager.STAT_SESSION_IMP_JD,
                    DataStoreManager.STAT_SESSION_MAX_IMP_JD,
                    DataStoreManager.CAREER_IMP_QUICK
            );
        } else if ("Singleplayer".equalsIgnoreCase(mode) || "SP".equalsIgnoreCase(mode) || "Single".equalsIgnoreCase(mode) || "single".equalsIgnoreCase(mode)) {
            clearIntKeys(
                    DataStoreManager.STAT_SESSION_GAMES_SP,
                    DataStoreManager.STAT_SESSION_DEALS_SP,
                    DataStoreManager.STAT_SESSION_CONCEDES_SP,
                    DataStoreManager.STAT_SESSION_WINS_SP
            );
            clearDblKeys(
                    DataStoreManager.STAT_SESSION_IMP_SP,
                    DataStoreManager.STAT_SESSION_MAX_IMP_SP,
                    DataStoreManager.CAREER_IMP_SINGLE
            );
        } else if ("Multiplayer".equalsIgnoreCase(mode) || "MP".equalsIgnoreCase(mode) || "Multi".equalsIgnoreCase(mode)) {
            clearIntKeys(
                    DataStoreManager.STAT_SESSION_GAMES_MP,
                    DataStoreManager.STAT_SESSION_DEALS_MP,
                    DataStoreManager.STAT_SESSION_CONCEDES_MP,
                    DataStoreManager.STAT_SESSION_WINS_MP
            );
            clearDblKeys(
                    DataStoreManager.STAT_SESSION_IMP_MP,
                    DataStoreManager.STAT_SESSION_MAX_IMP_MP
            );
        }
    }

    @SafeVarargs
    private final void clearIntKeys(Preferences.Key<Integer>... keys) {
        for (Preferences.Key<Integer> key : keys) {
            dataStoreManager.setPreference(key, 0)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        }
    }

    @SafeVarargs
    private final void clearDblKeys(Preferences.Key<Double>... keys) {
        for (Preferences.Key<Double> key : keys) {
            dataStoreManager.setPreference(key, 0.0)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        }
    }

    // --- RECORD GAME RESULT ---
    public void recordGame(String mode, int dealsCount, int concedeCount, double gameImp, boolean isWin) {
        if ("Just Declare".equalsIgnoreCase(mode) || "JD".equalsIgnoreCase(mode) || "quick".equalsIgnoreCase(mode)) {
            recordModeStats(
                    DataStoreManager.STAT_SESSION_DEALS_JD, DataStoreManager.STAT_SESSION_CONCEDES_JD, DataStoreManager.STAT_SESSION_IMP_JD, DataStoreManager.STAT_SESSION_MAX_IMP_JD, DataStoreManager.STAT_SESSION_WINS_JD,
                    DataStoreManager.STAT_GLOBAL_DEALS_JD, DataStoreManager.STAT_GLOBAL_CONCEDES_JD, DataStoreManager.STAT_GLOBAL_IMP_JD, DataStoreManager.STAT_GLOBAL_MAX_IMP_JD, DataStoreManager.STAT_GLOBAL_WINS_JD,
                    dealsCount, concedeCount, gameImp, isWin
            );
        } else if ("Singleplayer".equalsIgnoreCase(mode) || "SP".equalsIgnoreCase(mode) || "single".equalsIgnoreCase(mode)) {
            recordModeStats(
                    DataStoreManager.STAT_SESSION_DEALS_SP, DataStoreManager.STAT_SESSION_CONCEDES_SP, DataStoreManager.STAT_SESSION_IMP_SP, DataStoreManager.STAT_SESSION_MAX_IMP_SP, DataStoreManager.STAT_SESSION_WINS_SP,
                    DataStoreManager.STAT_GLOBAL_DEALS_SP, DataStoreManager.STAT_GLOBAL_CONCEDES_SP, DataStoreManager.STAT_GLOBAL_IMP_SP, DataStoreManager.STAT_GLOBAL_MAX_IMP_SP, DataStoreManager.STAT_GLOBAL_WINS_SP,
                    dealsCount, concedeCount, gameImp, isWin
            );
        } else {
            recordModeStats(
                    DataStoreManager.STAT_SESSION_DEALS_MP, DataStoreManager.STAT_SESSION_CONCEDES_MP, DataStoreManager.STAT_SESSION_IMP_MP, DataStoreManager.STAT_SESSION_MAX_IMP_MP, DataStoreManager.STAT_SESSION_WINS_MP,
                    DataStoreManager.STAT_GLOBAL_DEALS_MP, DataStoreManager.STAT_GLOBAL_CONCEDES_MP, DataStoreManager.STAT_GLOBAL_IMP_MP, DataStoreManager.STAT_GLOBAL_MAX_IMP_MP, DataStoreManager.STAT_GLOBAL_WINS_MP,
                    dealsCount, concedeCount, gameImp, isWin
            );
        }
    }

    private void recordModeStats(
            Preferences.Key<Integer> sessDeals, Preferences.Key<Integer> sessConcedes, Preferences.Key<Double> sessImp, Preferences.Key<Double> sessMaxImp, Preferences.Key<Integer> sessWins,
            Preferences.Key<Integer> globDeals, Preferences.Key<Integer> globConcedes, Preferences.Key<Double> globImp, Preferences.Key<Double> globMaxImp, Preferences.Key<Integer> globWins,
            int dealsCount, int concedeCount, double gameImp, boolean isWin
    ) {
        updateStatsSet(sessDeals, sessConcedes, sessImp, sessMaxImp, sessWins, dealsCount, concedeCount, gameImp, isWin);
        updateStatsSet(globDeals, globConcedes, globImp, globMaxImp, globWins, dealsCount, concedeCount, gameImp, isWin);
    }

    private void updateStatsSet(
            Preferences.Key<Integer> keyDeals, Preferences.Key<Integer> keyConcedes,
            Preferences.Key<Double> keyImp, Preferences.Key<Double> keyMaxImp, Preferences.Key<Integer> keyWins,
            int dealsCount, int concedeCount, double gameImp, boolean isWin
    ) {
        int curDeals = dataStoreManager.getPreference(keyDeals, 0).blockingFirst(0);
        int curConcedes = dataStoreManager.getPreference(keyConcedes, 0).blockingFirst(0);
        double curImp = dataStoreManager.getPreference(keyImp, 0.0).blockingFirst(0.0);
        double curMaxImp = dataStoreManager.getPreference(keyMaxImp, 0.0).blockingFirst(0.0);
        int curWins = dataStoreManager.getPreference(keyWins, 0).blockingFirst(0);

        dataStoreManager.setPreference(keyDeals, curDeals + dealsCount).subscribeOn(Schedulers.io()).subscribe();
        dataStoreManager.setPreference(keyConcedes, curConcedes + concedeCount).subscribeOn(Schedulers.io()).subscribe();
        dataStoreManager.setPreference(keyImp, curImp + gameImp).subscribeOn(Schedulers.io()).subscribe();
        if (gameImp > curMaxImp) {
            dataStoreManager.setPreference(keyMaxImp, gameImp).subscribeOn(Schedulers.io()).subscribe();
        }
        if (isWin) {
            dataStoreManager.setPreference(keyWins, curWins + 1).subscribeOn(Schedulers.io()).subscribe();
        }
    }
}
