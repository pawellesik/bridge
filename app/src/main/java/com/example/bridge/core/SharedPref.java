package com.example.bridge.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bridge.R;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Card;
import com.example.bridge.model.Player;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Trick;
import com.example.bridge.ui.game.GameActivity;
import com.example.bridge.ui.game.GameActivityTop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SharedPref {
    public int getChangeScore() {
        return changeScore;
    }

    final int changeScore = -50;
    public static final String PREFS_NAME = "BridgePrefs";
    static final String KEY_CAREER_SCORE = "careerScore";
    static final String KEY_GAMES_PLAYED = "gamesPlayed";
    static final String KEY_HAS_SAVED_DEAL = "hasSavedDeal";
    public static final String KEY_HISTORY = "gameHistory";

    GameActivityTop gameActivityTop;
    GameActivity gameActivity;

    public SharedPref(GameActivity gameActivity, GameActivityTop gameActivityTop) {
        this.gameActivityTop = gameActivityTop;
        this.gameActivity = gameActivity;
    }

    public void addGameToHistory(Contract contract, int snScore, List<Trick> playHistory, int claim, Map<String, List<Card>> hands, int autoSnScore, List<Trick> autoPlayHistory) {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        try {
            String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
            String result = gameActivity.getString(R.string.result_label, snScore);

            // Calculate points
            int requiredTricks = contract.isPass() ? 0 : contract.getLevel() + 6;
            int points = 0;
            if (!contract.isPass()) {
                if (snScore >= requiredTricks) {
                    points = getContractPkt(contract.getSuit(), contract.getLevel() + (snScore - requiredTricks));
                } else {
                    points = -getContractPkt(contract.getSuit(), contract.getLevel()) - getContractPkt(contract.getSuit(), (requiredTricks - snScore));
                }
            }

            org.json.JSONArray history;
            String existingHistory = prefs.getString(KEY_HISTORY, "[]");
            history = new org.json.JSONArray(existingHistory);

            org.json.JSONObject game = new org.json.JSONObject();
            game.put("contract", contract.toString());
            game.put("result", result);
            game.put("date", date);
            game.put("isSaved", false);
            game.put("claim", claim);
            game.put("snScore", snScore);
            game.put("autoSnScore", autoSnScore);
            game.put("points", points);

            // Zapisz początkowe ręce (do replayu)
            org.json.JSONObject handsJson = new org.json.JSONObject();
            for (Map.Entry<String, List<Card>> entry : hands.entrySet()) {
                org.json.JSONArray handArray = new org.json.JSONArray();
                for (Card card : entry.getValue()) {
                    handArray.put(card.getSuit().name() + ":" + card.getRank().name());
                }
                handsJson.put(entry.getKey(), handArray);
            }
            game.put("hands", handsJson);

            // Zapisz historię zagrywek użytkownika
            game.put("playHistory", serializeTricks(playHistory));

            // Zapisz historię zagrywek robota (alternatywną)
            game.put("autoPlayHistory", serializeTricks(autoPlayHistory));

            // Limit do 10 wpisów z ochroną wyróżnionych
            org.json.JSONArray newHistory = new org.json.JSONArray();
            newHistory.put(game);
            
            for (int i = 0; i < history.length(); i++) {
                if (newHistory.length() >= 10) {
                    if (!history.getJSONObject(i).optBoolean("isSaved", false)) continue;
                }
                newHistory.put(history.get(i));
            }

            prefs.edit().putString(KEY_HISTORY, newHistory.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private org.json.JSONArray serializeTricks(List<Trick> tricks) throws org.json.JSONException {
        org.json.JSONArray tricksArray = new org.json.JSONArray();
        if (tricks == null) return tricksArray;
        for (Trick trick : tricks) {
            org.json.JSONObject trickJson = new org.json.JSONObject();
            trickJson.put("winner", trick.getWinnerTrick());
            org.json.JSONObject cardsMap = new org.json.JSONObject();
            for (Map.Entry<String, Card> e : trick.getCardsOnTableMap().entrySet()) {
                cardsMap.put(e.getKey(), e.getValue().getSuit().name() + ":" + e.getValue().getRank().name());
            }
            trickJson.put("cards", cardsMap);
            tricksArray.put(trickJson);
        }
        return tricksArray;
    }

    public void markLatestGameAsSaved() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        try {
            String existingHistory = prefs.getString(KEY_HISTORY, "[]");
            org.json.JSONArray history = new org.json.JSONArray(existingHistory);
            if (history.length() > 0) {
                org.json.JSONObject latest = history.getJSONObject(0);
                latest.put("isSaved", true);
                prefs.edit().putString(KEY_HISTORY, history.toString()).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHistoryJson() {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_HISTORY, "[]");
    }

    public void saveDeal(Map<String, com.example.bridge.model.Player> players, Contract contract) {
        SharedPreferences prefs = gameActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (Map.Entry<String, com.example.bridge.model.Player> entry : players.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (com.example.bridge.model.Card card : entry.getValue().getHand()) {
                sb.append(card.getSuit().name()).append(":").append(card.getRank().name()).append(",");
            }
            editor.putString("hand_" + entry.getKey(), sb.toString());
        }

        if (contract != null) {
            editor.putInt("saved_contract_level", contract.getLevel());
            editor.putString("saved_contract_suit", contract.getSuit() != null ? contract.getSuit().name() : "NT");
            editor.putBoolean("saved_contract_pass", contract.isPass());
        }

        editor.putBoolean(KEY_HAS_SAVED_DEAL, true);
        editor.apply();
    }

    public static class SavedState {
        public final Map<String, List<Card>> hands;
        public final Contract contract;

        public SavedState(Map<String, List<Card>> hands, Contract contract) {
            this.hands = hands;
            this.contract = contract;
        }
    }

    public SavedState loadSavedDeal() {
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

        Contract contract = null;
        if (prefs.contains("saved_contract_level")) {
            if (prefs.getBoolean("saved_contract_pass", false)) {
                contract = new Contract(true);
            } else {
                int level = prefs.getInt("saved_contract_level", 1);
                String suitStr = prefs.getString("saved_contract_suit", "NT");
                Suit suit = suitStr.equals("NT") ? null : Suit.valueOf(suitStr);
                contract = new Contract(level, suit);
            }
        }

        return new SavedState(hands, contract);
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
