package com.example.bridge;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bridge.model.Contract;
import com.example.bridge.model.Suit;


public class SharedPref {
    public int getChangeScore() {
        return changeScore;
    }

    final int changeScore = -50;
    static final String PREFS_NAME = "BridgePrefs";
    static final String KEY_CAREER_SCORE = "careerScore";
    static final String KEY_GAMES_PLAYED = "gamesPlayed";

    GameActivityTop gameActivityTop;
    GameActivity gameActivity;

    public SharedPref(GameActivity gameActivity, GameActivityTop gameActivityTop) {
        this.gameActivityTop = gameActivityTop;
        this.gameActivity = gameActivity;
    }

    public void setScore(Contract contract, int snScoreValue) {

        int requiredTricks = contract.getLevel() + 6;
        int handScore;

        if (snScoreValue >= requiredTricks) {
            handScore = getContractPkt(contract.getSuit(), contract.getLevel() + (snScoreValue - requiredTricks));
        } else {
            handScore = -getContractPkt(contract.getSuit(), contract.getLevel()) - getContractPkt(contract.getSuit(), (requiredTricks - snScoreValue));
        }

        setPrefChangeTotalScore(handScore);
        gameActivity.setTotalScore(getPrefTotalScore(), handScore);
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

    public void setPrefChangeTotalScore(int changeScore) {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int careerScore = getPrefTotalScore();
        careerScore += changeScore;
        if (careerScore <0){
            careerScore = 0;
        }
        prefs.edit().putInt(KEY_CAREER_SCORE, careerScore).apply();
    }

    public int getPrefTotalScore() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int careerScore = prefs.getInt(KEY_CAREER_SCORE, 0);
        return careerScore;
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

}
