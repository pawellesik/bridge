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
    public static final Preferences.Key<Integer> CAREER_IMP = PreferencesKeys.intKey("career_imp");

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
