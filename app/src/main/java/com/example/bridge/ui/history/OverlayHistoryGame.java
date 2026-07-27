package com.example.bridge.ui.history;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bridge.R;
import com.example.bridge.core.db.GameRecord;
import com.example.bridge.ui.game.GameActivity;
import org.json.JSONObject;

public class OverlayHistoryGame {

    private final GameActivity activity;
    private final View root;
    private int currentDbId = -1;
    private final java.util.List<Pbn> reconstructedPbnList = new java.util.ArrayList<>();
    private Pbn selectedPbn = null;

    public OverlayHistoryGame(GameActivity activity) {
        this.activity = activity;
        this.root = activity.findViewById(R.id.history_game_overlay);
    }

    public void showGame(int dbId) {
        this.currentDbId = dbId;
        if (root != null) {
            root.setVisibility(View.VISIBLE);
            loadGameData(dbId);
        }
    }
    private void loadGameData(int dbId) {
        new Thread(() -> {
            try {
                java.util.List<GameRecord> records = com.example.bridge.core.db.AppDatabase.getInstance(activity).gameDao().getGamesByDealId(dbId);
                activity.runOnUiThread(() -> reconstructedPbnList.clear());

                if (records != null && !records.isEmpty()) {
                    java.util.List<Pbn> loadedPbns = new java.util.ArrayList<>();
                    for (GameRecord record : records) {
                        Pbn pbn = new Pbn(activity, record.system);
                        pbn.loadFromJsonObject(new JSONObject(record.gameData));
                        loadedPbns.add(pbn);
                    }
                    
                    activity.runOnUiThread(() -> {
                        reconstructedPbnList.addAll(loadedPbns);
                        // Domyślnie zaznaczamy MyGame (Current)
                        for (Pbn p : reconstructedPbnList) {
                            if ("MyGame".equals(p.getBoard())) {
                                selectedPbn = p;
                                break;
                            }
                        }
                        if (selectedPbn == null && !reconstructedPbnList.isEmpty()) {
                            selectedPbn = reconstructedPbnList.get(0);
                        }
                        updateUi();
                    });
                }
            } catch (Exception e) {
                android.util.Log.e("plesik", "Error loading game data", e);
            }
        }).start();
    }

    private void updateUi() {
        if (root == null) return;
        android.widget.LinearLayout tableContent = root.findViewById(R.id.table_history_content_history);
        if (tableContent != null) {
            tableContent.removeAllViews();
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(activity);

            for (Pbn pbn : reconstructedPbnList) {
                View row = inflater.inflate(R.layout.item_history_row, tableContent, false);
                
                TextView tvName = row.findViewById(R.id.tv_row_name);
                TextView tvContractLevel = row.findViewById(R.id.tv_row_contract);
                android.widget.ImageView ivSuit = row.findViewById(R.id.iv_row_suit);
                TextView tvResultSymbol = row.findViewById(R.id.tv_row_result_symbol);

                TextView tvMy1 = row.findViewById(R.id.tv_row_my1);
                TextView tvOpps1 = row.findViewById(R.id.tv_row_opps1);
                TextView tvMy2 = row.findViewById(R.id.tv_row_my2);
                TextView tvOpps2 = row.findViewById(R.id.tv_row_opps2);

                if (tvName != null) tvName.setText(pbn.getBoard());
                
                // Zaznaczanie wiersza
                boolean isSelected = (pbn == selectedPbn);
                row.setActivated(isSelected);
                
                // Ustawiamy biały kolor tekstu dla zaznaczonego wiersza
                int textColor = isSelected ? android.graphics.Color.WHITE : android.graphics.Color.BLACK;
                if (tvName != null) tvName.setTextColor(textColor);
                if (tvMy1 != null) tvMy1.setTextColor(textColor);
                if (tvOpps1 != null) tvOpps1.setTextColor(textColor);
                if (tvMy2 != null) tvMy2.setTextColor(textColor);
                if (tvOpps2 != null) tvOpps2.setTextColor(textColor);

                row.setOnClickListener(v -> {
                    selectedPbn = pbn;
                    updateUi(); // Odśwież widok, aby zmienić tło wiersza
                    // Tutaj możesz dodać wywołanie odświeżania kart na górze
                });

                if (pbn.getContract() != null) {
                    com.example.bridge.model.Contract c = pbn.getContract();
                    if (c.isPass()) {
                        if (tvContractLevel != null) {
                            tvContractLevel.setText(R.string.contract_pass);
                            tvContractLevel.setTextColor(android.graphics.Color.BLACK);
                        }
                        if (ivSuit != null) ivSuit.setVisibility(View.GONE);
                    } else {
                        if (tvContractLevel != null) {
                            tvContractLevel.setText(String.valueOf(c.getLevel()));
                        }
                        
                        if (ivSuit != null) {
                            if (c.isNoTrump()) {
                                ivSuit.setVisibility(View.GONE);
                                tvContractLevel.setText(c.getLevel() + "NT");
                                tvContractLevel.setTextColor(android.graphics.Color.BLACK);
                            } else {
                                ivSuit.setVisibility(View.VISIBLE);
                                ivSuit.setImageResource(c.getSuit().resId);
                                int suitColor = c.getSuit().getColor(activity);
                                ivSuit.setColorFilter(suitColor);
                                tvContractLevel.setTextColor(suitColor);
                            }
                        }

                        if (tvResultSymbol != null) {
                            String declChar = (pbn.getDeclarer() != null && !pbn.getDeclarer().isEmpty()) 
                                    ? String.valueOf(pbn.getDeclarer().charAt(0)) : "";
                            
                            int diff = pbn.getResultTricks() - (c.getLevel() + 6);
                            String resultStr = " " + declChar;
                            if (diff == 0) resultStr += "=";
                            else if (diff > 0) resultStr += "+" + diff;
                            else resultStr += diff;
                            
                            tvResultSymbol.setText(resultStr);
                            tvResultSymbol.setTextColor(android.graphics.Color.BLACK);
                        }
                    }
                }

                // Temporary placeholder for scores until logic is added to Pbn
                if (tvMy1 != null) tvMy1.setText("-");
                if (tvOpps1 != null) tvOpps1.setText("-");
                if (tvMy2 != null) tvMy2.setText("-");
                if (tvOpps2 != null) tvOpps2.setText("-");

                tableContent.addView(row);
            }
        }
    }

    public java.util.List<Pbn> getReconstructedPbnList() {
        return reconstructedPbnList;
    }

    public void hide() {
        if (root != null) {
            root.setVisibility(View.GONE);
        }
    }

    public boolean isVisible() {
        return root != null && root.getVisibility() == View.VISIBLE;
    }
}
