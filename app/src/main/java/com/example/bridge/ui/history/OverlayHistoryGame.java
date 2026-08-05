package com.example.bridge.ui.history;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bridge.R;
import com.example.bridge.core.db.GameRecord;
import com.example.bridge.ui.game.GameActivity;
import org.json.JSONObject;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class OverlayHistoryGame {

    private final GameActivity activity;
    private final View root;
    private final java.util.List<Pbn> reconstructedPbnList = new java.util.ArrayList<>();
    private Pbn selectedPbn = null;

    private TextView tvNorthCards, tvSouthCards, tvEastCards, tvWestCards;
    private androidx.recyclerview.widget.RecyclerView rvBiddingHistory;
    private com.example.bridge.ui.biddings.GameBiddingHistoryAdapter biddingAdapter;
    private final List<String> biddingList = new ArrayList<>();

    public OverlayHistoryGame(GameActivity activity) {
        this.activity = activity;
        this.root = activity.findViewById(R.id.history_game_overlay);
        if (root != null) {
            tvNorthCards = root.findViewById(R.id.tv_north_cards_history);
            tvSouthCards = root.findViewById(R.id.tv_south_cards_history);
            tvEastCards = root.findViewById(R.id.tv_east_cards_history);
            tvWestCards = root.findViewById(R.id.tv_west_cards_history);
            
            rvBiddingHistory = root.findViewById(R.id.rv_bidding_history_history);
            if (rvBiddingHistory != null) {
                rvBiddingHistory.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(activity, 4));
                biddingAdapter = new com.example.bridge.ui.biddings.GameBiddingHistoryAdapter(biddingList, R.layout.item_bid_tile_compact);
                biddingAdapter.setShowPreviewTile(false);
                rvBiddingHistory.setAdapter(biddingAdapter);
            }
        }
    }

    public void showGame(int dbId) {
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

                        android.util.Log.e("plesik",  record.gameData.toString());
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
        
        updateSelectedGameDetails();

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

    private void updateSelectedGameDetails() {
        if (selectedPbn == null) return;

        // Bidding history
        updateBiddingHistory(selectedPbn);

        // Hands
        Map<String, List<com.example.bridge.model.Card>> hands = selectedPbn.getInitialHands();
        if (hands != null) {
            if (tvNorthCards != null) tvNorthCards.setText(formatHandForDisplay(hands.get("North")));
            if (tvSouthCards != null) tvSouthCards.setText(formatHandForDisplay(hands.get("South")));
            if (tvEastCards != null) tvEastCards.setText(formatHandForDisplay(hands.get("East")));
            if (tvWestCards != null) tvWestCards.setText(formatHandForDisplay(hands.get("West")));
        }
    }

    private void updateBiddingHistory(Pbn pbn) {
        if (biddingAdapter == null) return;
        
        biddingList.clear();
        List<String> auction = pbn.getAuction();
        if (auction != null && !auction.isEmpty()) {
            String dealer = pbn.toJsonObject().optString("Dealer", "W");
            int offset = 0;
            if ("N".equals(dealer)) offset = 1;
            else if ("E".equals(dealer)) offset = 2;
            else if ("S".equals(dealer)) offset = 3;

            for (int i = 0; i < offset; i++) {
                biddingList.add("-");
            }
            biddingList.addAll(auction);
        }
        biddingAdapter.setPreviewSelection(""); // Hide preview in history
        biddingAdapter.notifyDataSetChanged();
    }


    private String formatHandForDisplay(java.util.List<com.example.bridge.model.Card> hand) {
        if (hand == null) return "";
        StringBuilder sb = new StringBuilder();
        com.example.bridge.model.Suit[] suits = {
                com.example.bridge.model.Suit.SPADES,
                com.example.bridge.model.Suit.HEARTS,
                com.example.bridge.model.Suit.DIAMONDS,
                com.example.bridge.model.Suit.CLUBS
        };
        String[] suitSymbols = {"♠", "♥", "♦", "♣"};

        for (int i = 0; i < 4; i++) {
            sb.append(suitSymbols[i]).append(" ");
            com.example.bridge.model.Suit currentSuit = suits[i];
            java.util.List<com.example.bridge.model.Card> suitCards = new java.util.ArrayList<>();
            for (com.example.bridge.model.Card card : hand) {
                if (card.getSuit() == currentSuit) {
                    suitCards.add(card);
                }
            }
            // Sort ranks descending
            suitCards.sort((c1, c2) -> Integer.compare(c2.getRank().ordinal(), c1.getRank().ordinal()));

            for (int j = 0; j < suitCards.size(); j++) {
                sb.append(formatRank(suitCards.get(j).getRank()));
                if (j < suitCards.size() - 1) sb.append(" ");
            }
            if (i < 3) sb.append("\n");
        }
        return sb.toString();
    }

    private String formatRank(com.example.bridge.model.Rank rank) {
        if (rank == com.example.bridge.model.Rank.TEN) return "10";
        return rank.display;
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
