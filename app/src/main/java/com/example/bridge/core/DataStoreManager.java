package com.example.bridge.core;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class DataStoreManager {
    private static DataStoreManager instance;
    private final RxDataStore<Preferences> dataStore;

    // Keys
    public static final Preferences.Key<String> SELECTED_LANGUAGE = PreferencesKeys.stringKey("selected_language");
    public static final Preferences.Key<Boolean> CARD_COLORS_COLORFUL = PreferencesKeys.booleanKey("card_colors_colorful");
    public static final Preferences.Key<String> QUICK_GAME_DIFFICULTY = PreferencesKeys.stringKey("quick_game_difficulty");
    public static final Preferences.Key<String> BIDDING_SYSTEM = PreferencesKeys.stringKey("bidding_system");
    public static final Preferences.Key<Integer> GAMES_PLAYED = PreferencesKeys.intKey("games_played");
    public static final Preferences.Key<Double> CAREER_IMP_SINGLE = PreferencesKeys.doubleKey("career_imp_single");
    public static final Preferences.Key<Double> CAREER_IMP_QUICK = PreferencesKeys.doubleKey("career_imp_quick");

    // Session Statistics Keys (Just Declare, Singleplayer, Multiplayer)
    public static final Preferences.Key<Integer> STAT_SESSION_GAMES_JD = PreferencesKeys.intKey("stat_session_games_jd");
    public static final Preferences.Key<Integer> STAT_SESSION_GAMES_SP = PreferencesKeys.intKey("stat_session_games_sp");
    public static final Preferences.Key<Integer> STAT_SESSION_GAMES_MP = PreferencesKeys.intKey("stat_session_games_mp");

    public static final Preferences.Key<Integer> STAT_SESSION_DEALS_JD = PreferencesKeys.intKey("stat_session_deals_jd");
    public static final Preferences.Key<Integer> STAT_SESSION_DEALS_SP = PreferencesKeys.intKey("stat_session_deals_sp");
    public static final Preferences.Key<Integer> STAT_SESSION_DEALS_MP = PreferencesKeys.intKey("stat_session_deals_mp");

    public static final Preferences.Key<Integer> STAT_SESSION_CONCEDES_JD = PreferencesKeys.intKey("stat_session_concedes_jd");
    public static final Preferences.Key<Integer> STAT_SESSION_CONCEDES_SP = PreferencesKeys.intKey("stat_session_concedes_sp");
    public static final Preferences.Key<Integer> STAT_SESSION_CONCEDES_MP = PreferencesKeys.intKey("stat_session_concedes_mp");

    public static final Preferences.Key<Double> STAT_SESSION_IMP_JD = PreferencesKeys.doubleKey("stat_session_imp_jd_dbl");
    public static final Preferences.Key<Double> STAT_SESSION_IMP_SP = PreferencesKeys.doubleKey("stat_session_imp_sp_dbl");
    public static final Preferences.Key<Double> STAT_SESSION_IMP_MP = PreferencesKeys.doubleKey("stat_session_imp_mp_dbl");

    public static final Preferences.Key<Double> STAT_SESSION_MAX_IMP_JD = PreferencesKeys.doubleKey("stat_session_max_imp_jd_dbl");
    public static final Preferences.Key<Double> STAT_SESSION_MAX_IMP_SP = PreferencesKeys.doubleKey("stat_session_max_imp_sp_dbl");
    public static final Preferences.Key<Double> STAT_SESSION_MAX_IMP_MP = PreferencesKeys.doubleKey("stat_session_max_imp_mp_dbl");

    public static final Preferences.Key<Integer> STAT_SESSION_WINS_JD = PreferencesKeys.intKey("stat_session_wins_jd");
    public static final Preferences.Key<Integer> STAT_SESSION_WINS_SP = PreferencesKeys.intKey("stat_session_wins_sp");
    public static final Preferences.Key<Integer> STAT_SESSION_WINS_MP = PreferencesKeys.intKey("stat_session_wins_mp");

    // Global Statistics Keys (Just Declare, Singleplayer, Multiplayer)
    public static final Preferences.Key<Integer> STAT_GLOBAL_GAMES_JD = PreferencesKeys.intKey("stat_global_games_jd");
    public static final Preferences.Key<Integer> STAT_GLOBAL_GAMES_SP = PreferencesKeys.intKey("stat_global_games_sp");
    public static final Preferences.Key<Integer> STAT_GLOBAL_GAMES_MP = PreferencesKeys.intKey("stat_global_games_mp");

    public static final Preferences.Key<Integer> STAT_GLOBAL_DEALS_JD = PreferencesKeys.intKey("stat_global_deals_jd");
    public static final Preferences.Key<Integer> STAT_GLOBAL_DEALS_SP = PreferencesKeys.intKey("stat_global_deals_sp");
    public static final Preferences.Key<Integer> STAT_GLOBAL_DEALS_MP = PreferencesKeys.intKey("stat_global_deals_mp");

    public static final Preferences.Key<Integer> STAT_GLOBAL_CONCEDES_JD = PreferencesKeys.intKey("stat_global_concedes_jd");
    public static final Preferences.Key<Integer> STAT_GLOBAL_CONCEDES_SP = PreferencesKeys.intKey("stat_global_concedes_sp");
    public static final Preferences.Key<Integer> STAT_GLOBAL_CONCEDES_MP = PreferencesKeys.intKey("stat_global_concedes_mp");

    public static final Preferences.Key<Double> STAT_GLOBAL_IMP_JD = PreferencesKeys.doubleKey("stat_global_imp_jd_dbl");
    public static final Preferences.Key<Double> STAT_GLOBAL_IMP_SP = PreferencesKeys.doubleKey("stat_global_imp_sp_dbl");
    public static final Preferences.Key<Double> STAT_GLOBAL_IMP_MP = PreferencesKeys.doubleKey("stat_global_imp_mp_dbl");

    public static final Preferences.Key<Double> STAT_GLOBAL_MAX_IMP_JD = PreferencesKeys.doubleKey("stat_global_max_imp_jd_dbl");
    public static final Preferences.Key<Double> STAT_GLOBAL_MAX_IMP_SP = PreferencesKeys.doubleKey("stat_global_max_imp_sp_dbl");
    public static final Preferences.Key<Double> STAT_GLOBAL_MAX_IMP_MP = PreferencesKeys.doubleKey("stat_global_max_imp_mp_dbl");

    public static final Preferences.Key<Integer> STAT_GLOBAL_WINS_JD = PreferencesKeys.intKey("stat_global_wins_jd");
    public static final Preferences.Key<Integer> STAT_GLOBAL_WINS_SP = PreferencesKeys.intKey("stat_global_wins_sp");
    public static final Preferences.Key<Integer> STAT_GLOBAL_WINS_MP = PreferencesKeys.intKey("stat_global_wins_mp");

    private DataStoreManager(Context context) {
        dataStore = new RxPreferenceDataStoreBuilder(context.getApplicationContext(), "BridgePrefs").build();
    }

    public static synchronized DataStoreManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataStoreManager(context);
        }
        return instance;
    }

    public <T> Flowable<T> getPreference(Preferences.Key<T> key, T defaultValue) {
        return dataStore.data().map(prefs -> {
            T value = prefs.get(key);
            return value != null ? value : defaultValue;
        });
    }

    public <T> Single<Preferences> setPreference(Preferences.Key<T> key, T value) {
        return dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(key, value);
            return Single.just(mutablePreferences);
        });
    }
}
