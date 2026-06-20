package com.example.bridge;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bridge.model.Contract;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Card;
import com.example.bridge.model.Player;
import com.example.bridge.model.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SharedPref {
    public int getChangeScore() {
        return changeScore;
    }

    final int changeScore = -50;
    static final String PREFS_NAME = "BridgePrefs";
    static final String KEY_CAREER_SCORE = "careerScore";
    static final String KEY_GAMES_PLAYED = "gamesPlayed";
    static final String KEY_HAS_SAVED_DEAL = "hasSavedDeal";

    GameActivityTop gameActivityTop;
    GameActivity gameActivity;

    public SharedPref(GameActivity gameActivity, GameActivityTop gameActivityTop) {
        this.gameActivityTop = gameActivityTop;
        this.gameActivity = gameActivity;
    }

    public void saveDeal(Map<String, com.example.bridge.model.Player> players) {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (Map.Entry<String, com.example.bridge.model.Player> entry : players.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (com.example.bridge.model.Card card : entry.getValue().getHand()) {
                sb.append(card.getSuit().name()).append(":").append(card.getRank().name()).append(",");
            }
            editor.putString("hand_" + entry.getKey(), sb.toString());
        }
        editor.putBoolean(KEY_HAS_SAVED_DEAL, true);
        editor.apply();
    }

    public Map<String, List<com.example.bridge.model.Card>> loadSavedDeal() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (!prefs.getBoolean(KEY_HAS_SAVED_DEAL, false)) return null;

        Map<String, List<com.example.bridge.model.Card>> hands = new java.util.HashMap<>();
        String[] directions = {"North", "East", "South", "West"};
        for (String dir : directions) {
            String saved = prefs.getString("hand_" + dir, "");
            List<com.example.bridge.model.Card> cards = new ArrayList<>();
            if (!saved.isEmpty()) {
                String[] parts = saved.split(",");
                for (String p : parts) {
                    if (p.contains(":")) {
                        String[] sr = p.split(":");
                        cards.add(new com.example.bridge.model.Card(com.example.bridge.model.Suit.valueOf(sr[0]), com.example.bridge.model.Rank.valueOf(sr[1])));
                    }
                }
            }
            hands.put(dir, cards);
        }
        return hands;
    }

    public void clearSavedDeal() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_HAS_SAVED_DEAL, false).apply();
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
