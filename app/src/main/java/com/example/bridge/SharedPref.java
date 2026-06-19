package com.example.bridge;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    public int getChangeScore() {
        return changeScore;
    }

    final int changeScore = 1;
    static final String PREFS_NAME = "BridgePrefs";
    static final String KEY_CAREER_SCORE = "careerScore";

    GameActivityTop gameActivityTop;
    GameActivity gameActivity;

    public SharedPref(GameActivity gameActivity, GameActivityTop gameActivityTop) {
        this.gameActivityTop = gameActivityTop;
        this.gameActivity = gameActivity;
    }

    public void setScore(String contract, int snScoreValue) {
        int level = 0;
        try {
            String[] parts = contract.split(" ");
            if (parts.length > 0) {
                level = Integer.parseInt(parts[0].trim());
            }

        } catch (Exception e) {
            // level remains 0
        }

        int requiredTricks = level + 6;
        int handScore = 0;
        if (level > 0) {
            if (snScoreValue >= requiredTricks) {
                handScore = level + (snScoreValue - requiredTricks);
            } else {
                handScore = -level - (requiredTricks - snScoreValue);
            }
        }
        setPrefChangeTotalScore(handScore);
        gameActivity.setTotalScore(getPrefTotalScore(), handScore);
    }

    public void setPrefChangeTotalScore(int changeScore) {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int careerScore = getPrefTotalScore();
        careerScore += changeScore;
        prefs.edit().putInt(KEY_CAREER_SCORE, careerScore).apply();
    }

    public int getPrefTotalScore() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int careerScore = prefs.getInt(KEY_CAREER_SCORE, 0);
        return careerScore;
    }

}
