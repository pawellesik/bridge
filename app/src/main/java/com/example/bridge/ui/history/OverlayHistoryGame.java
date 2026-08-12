package com.example.bridge.ui.history;

import android.view.View;
import android.widget.TextView;
import com.example.bridge.R;
import com.example.bridge.core.db.GameRecord;
import com.example.bridge.ui.game.GameActivity;
import org.json.JSONObject;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class OverlayHistoryGame {

    public interface OnUIReadyListener {
        void onReady();
    }

    private final GameActivity activity;
    private final View root;
    private final List<Pbn> reconstructedPbnList = new ArrayList<>();
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
        showGame(dbId, null);
    }

    public void showGame(int dbId, OnUIReadyListener listener) {
        if (root == null) return;
        loadGameData(dbId, listener);
    }

    public void hide() {
        if (root == null) return;
        root.setVisibility(View.GONE);
        clearViews();
    }

    private void clearViews() {
        if (tvNorthCards != null) tvNorthCards.setText("");
        if (tvSouthCards != null) tvSouthCards.setText("");
        if (tvEastCards != null) tvEastCards.setText("");
        if (tvWestCards != null) tvWestCards.setText("");
        biddingList.clear();
        if (biddingAdapter != null) biddingAdapter.notifyDataSetChanged();
        selectedPbn = null;
        reconstructedPbnList.clear();
        android.widget.LinearLayout tableContent = root.findViewById(R.id.table_history_content_history);
        if (tableContent != null) tableContent.removeAllViews();
    }

    private void loadGameData(int dbId, OnUIReadyListener listener) {
        new Thread(() -> {
            try {
                List<GameRecord> records = com.example.bridge.core.db.AppDatabase.getInstance(activity).gameDao().getGamesByDealId(dbId);
                List<Pbn> loadedPbns = new ArrayList<>();
                if (records != null) {
                    for (GameRecord record : records) {
                        Pbn pbn = new Pbn(activity, record.system);
                        pbn.loadFromJsonObject(new JSONObject(record.gameData));
                        loadedPbns.add(pbn);
                    }
                }
                
                activity.runOnUiThread(() -> {
                    reconstructedPbnList.clear();
                    reconstructedPbnList.addAll(loadedPbns);
                    reconstructedPbnList.sort((p1, p2) -> Integer.compare(p2.getImp(), p1.getImp()));

                    selectedPbn = null;
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
                    root.setVisibility(View.VISIBLE);
                    
                    if (listener != null) {
                        listener.onReady();
                    }
                });
            } catch (Exception e) {
                android.util.Log.e("plesik", "Error loading game data", e);
            }
        }).start();
    }

    public void updateUi() {
        if (root == null) return;
        updateSelectedGameDetails();
        android.widget.LinearLayout tableContent = root.findViewById(R.id.table_history_content_history);
        if (tableContent != null) {
            tableContent.removeAllViews();
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(activity);

            int index = 0;
            View selectedRowView = null;
            for (Pbn pbn : reconstructedPbnList) {
                View row = inflater.inflate(R.layout.item_history_row, tableContent, false);
                if (!pbn.equals(selectedPbn)) row.setSelected(index % 2 != 0);
                index++;
                setupRow(row, pbn);
                if (pbn == selectedPbn) {
                    row.setActivated(true);
                    selectedRowView = row;
                }
                row.setOnClickListener(v -> {
                    selectedPbn = pbn;
                    updateUi();
                });
                tableContent.addView(row);
            }
            if (selectedRowView != null) {
                View finalRow = selectedRowView;
                androidx.core.widget.NestedScrollView scrollView = root.findViewById(R.id.scroll_history_history);
                if (scrollView != null) {
                    scrollView.post(() -> {
                        int scrollTo = finalRow.getTop() - (scrollView.getHeight() / 2) + (finalRow.getHeight() / 2);
                        scrollView.smoothScrollTo(0, Math.max(0, scrollTo));
                    });
                }
            }
        }
    }

    private void setupRow(View row, Pbn pbn) {
        TextView tvNameNorth = row.findViewById(R.id.tv_row_name_north);
        TextView tvNameSouth = row.findViewById(R.id.tv_row_name_south);
        TextView tvContractLevel = row.findViewById(R.id.tv_row_contract);
        android.widget.ImageView ivSuit = row.findViewById(R.id.iv_row_suit);
        TextView tvResultSymbol = row.findViewById(R.id.tv_row_result_symbol);
        TextView tvMy1 = row.findViewById(R.id.tv_row_my1);
        TextView tvOpps1 = row.findViewById(R.id.tv_row_opps1);
        TextView tvImp = row.findViewById(R.id.tv_row_imp);

        if (tvNameNorth != null) tvNameNorth.setText(pbn.getNorth());
        if (tvNameSouth != null) tvNameSouth.setText(pbn.getSouth());
        
        boolean isMyGame = "MyGame".equals(pbn.getBoard());
        boolean isSelected = (pbn == selectedPbn);
        int textColor = isSelected ? android.graphics.Color.WHITE : (isMyGame ? android.graphics.Color.parseColor("#C62828") : android.graphics.Color.BLACK);
        
        if (tvNameNorth != null) {
            tvNameNorth.setTextColor(textColor);
            tvNameNorth.setTypeface(null, isMyGame ? android.graphics.Typeface.BOLD_ITALIC : android.graphics.Typeface.BOLD);
        }
        if (tvNameSouth != null) {
            tvNameSouth.setTextColor(textColor);
            tvNameSouth.setTypeface(null, isMyGame ? android.graphics.Typeface.BOLD_ITALIC : android.graphics.Typeface.BOLD);
        }

        if (tvMy1 != null) {
            int score = pbn.getScore();
            if (score > 0) { tvMy1.setText(String.valueOf(score)); tvOpps1.setText(""); }
            else if (score < 0) { tvMy1.setText(""); tvOpps1.setText(String.valueOf(Math.abs(score))); }
            else { tvMy1.setText("0"); tvOpps1.setText(""); }
            tvMy1.setTextColor(textColor);
        }
        if (tvOpps1 != null) tvOpps1.setTextColor(textColor);
        if (tvImp != null) {
            int imp = pbn.getImp();
            tvImp.setText((imp > 0 ? "+" : "") + imp);
            tvImp.setTextColor(textColor);
        }

        if (pbn.getContract() != null) {
            com.example.bridge.model.Contract c = pbn.getContract();
            if (c.isPass()) {
                if (tvContractLevel != null) { tvContractLevel.setText(R.string.contract_pass); tvContractLevel.setTextColor(android.graphics.Color.BLACK); }
                if (ivSuit != null) ivSuit.setVisibility(View.GONE);
            } else {
                if (tvContractLevel != null) tvContractLevel.setText(String.valueOf(c.getLevel()));
                if (ivSuit != null) {
                    if (c.isNoTrump()) { ivSuit.setVisibility(View.GONE); tvContractLevel.setText(c.getLevel() + "NT"); tvContractLevel.setTextColor(android.graphics.Color.BLACK); }
                    else { ivSuit.setVisibility(View.VISIBLE); ivSuit.setImageResource(c.getSuit().resId); int suitColor = c.getSuit().getColor(activity); ivSuit.setColorFilter(suitColor); tvContractLevel.setTextColor(suitColor); }
                }
                if (tvResultSymbol != null) {
                    String declChar = (pbn.getDeclarer() != null && !pbn.getDeclarer().isEmpty()) ? String.valueOf(pbn.getDeclarer().charAt(0)) : "";
                    int diff = pbn.getResultTricks() - (c.getLevel() + 6);
                    String res = " " + declChar + (diff == 0 ? "=" : (diff > 0 ? "+" + diff : String.valueOf(diff)));
                    tvResultSymbol.setText(res);
                    tvResultSymbol.setTextColor(android.graphics.Color.BLACK);
                }
            }
        }
    }

    private void updateSelectedGameDetails() {
        if (selectedPbn == null) return;
        updateBiddingHistory(selectedPbn);
        Map<String, List<com.example.bridge.model.Card>> hands = selectedPbn.getInitialHands();
        if (hands != null) {
            updateHandTextView(tvNorthCards, hands.get("North"));
            updateHandTextView(tvSouthCards, hands.get("South"));
            updateHandTextView(tvEastCards, hands.get("East"));
            updateHandTextView(tvWestCards, hands.get("West"));
        }
    }

    private void updateBiddingHistory(Pbn pbn) {
        if (biddingAdapter == null) return;
        biddingList.clear();
        List<String> auction = pbn.getAuction();
        if (auction != null && !auction.isEmpty()) {
            String dealer = pbn.toJsonObject().optString("Dealer", "W");
            int offset = 0;
            if ("North".equals(dealer) || "N".equals(dealer)) offset = 1;
            else if ("East".equals(dealer) || "E".equals(dealer)) offset = 2;
            else if ("South".equals(dealer) || "S".equals(dealer)) offset = 3;
            for (int i = 0; i < offset; i++) biddingList.add("-");
            biddingList.addAll(auction);
        }
        biddingAdapter.setPreviewSelection("");
        biddingAdapter.notifyDataSetChanged();
    }

    private void updateHandTextView(TextView tv, List<com.example.bridge.model.Card> hand) {
        if (tv == null) return;
        tv.setTextColor(android.graphics.Color.BLACK);
        tv.setText(formatHandForDisplay(hand), android.widget.TextView.BufferType.SPANNABLE);
    }

    private android.text.SpannableStringBuilder formatHandForDisplay(java.util.List<com.example.bridge.model.Card> hand) {
        android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder();
        if (hand == null) return ssb;
        com.example.bridge.model.Suit[] suits = {
                com.example.bridge.model.Suit.SPADES,
                com.example.bridge.model.Suit.HEARTS,
                com.example.bridge.model.Suit.DIAMONDS,
                com.example.bridge.model.Suit.CLUBS
        };
        String[] suitSymbols = {"♠\uFE0E", "♥\uFE0E", "♦\uFE0E", "♣\uFE0E"};
        float tabOffset = 20 * activity.getResources().getDisplayMetrics().density;
        ssb.setSpan(new android.text.style.TabStopSpan.Standard((int) tabOffset), 0, 0, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        for (int i = 0; i < 4; i++) {
            int symbolStart = ssb.length();
            ssb.append(suitSymbols[i]);
            int suitColor = suits[i].getColor(activity);
            ssb.setSpan(new android.text.style.ForegroundColorSpan(suitColor), symbolStart, ssb.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append("\t");
            int cardsStart = ssb.length();
            List<com.example.bridge.model.Card> suitCards = new ArrayList<>();
            for (com.example.bridge.model.Card card : hand) if (card.getSuit() == suits[i]) suitCards.add(card);
            suitCards.sort((c1, c2) -> Integer.compare(c2.getRank().ordinal(), c1.getRank().ordinal()));
            for (int j = 0; j < suitCards.size(); j++) {
                ssb.append(formatRank(suitCards.get(j).getRank()));
                if (j < suitCards.size() - 1) ssb.append(" ");
            }
            ssb.setSpan(new android.text.style.ForegroundColorSpan(android.graphics.Color.BLACK), cardsStart, ssb.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (i < 3) ssb.append("\n");
        }
        return ssb;
    }

    private String formatRank(com.example.bridge.model.Rank rank) {
        if (rank == com.example.bridge.model.Rank.TEN) return "10";
        return rank.display;
    }

    public boolean isVisible() {
        return root != null && root.getVisibility() == View.VISIBLE;
    }
}
