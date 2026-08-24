package com.example.bridge.core;

import android.content.Context;
import com.example.bridge.ui.game.GameActivity;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SharedPref {
    private final DataStoreManager dataStoreManager;

    public SharedPref(Context context) {
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

    public int getCareerImp() {
        try {
            return dataStoreManager.getPreference(DataStoreManager.CAREER_IMP, 0)
                    .firstOrError()
                    .onErrorReturnItem(0)
                    .blockingGet();
        } catch (Exception e) {
            return 0;
        }
    }

    public void setCareerImp(int imp) {
        dataStoreManager.setPreference(DataStoreManager.CAREER_IMP, imp)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
