package com.example.bridge.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bridge.R;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Card;
import com.example.bridge.model.Trick;
import com.example.bridge.ui.game.GameActivity;
import com.example.bridge.ui.game.GameTop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SharedPref {
    public static final String PREFS_NAME = "BridgePrefs";
    static final String KEY_CAREER_IMP = "careerImp";
    static final String KEY_GAMES_PLAYED = "gamesPlayed";
    static final String KEY_HAS_SAVED_DEAL = "hasSavedDeal";
    public static final String KEY_HISTORY = "gameHistory";
    GameActivity gameActivity;

    public SharedPref(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }


    private int getContractPkt(Suit suit, int cnt) {
        if (suit == null) {
            return (cnt > 0) ? (cnt * 30 + 10) : 0;
        }
        switch (suit) {
            case SPADES:
            case HEARTS:
                return cnt * 30;
            case CLUBS:
            case DIAMONDS:
                return cnt * 20;
            default:
                return 0;
        }
    }

    public int getGamesPlayed() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_GAMES_PLAYED, 0);
    }

    public void incrementGamesPlayed() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int count = getGamesPlayed() + 1;
        prefs.edit().putInt(KEY_GAMES_PLAYED, count).apply();
    }

    public void saveGameHistory(String historyJson) {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_HISTORY, historyJson).apply();
    }

    public String getGameHistory() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_HISTORY, "");
    }


}
