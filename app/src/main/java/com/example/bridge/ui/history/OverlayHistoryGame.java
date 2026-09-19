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
import java.util.Locale;

public class OverlayHistoryGame {

    public interface OnUIReadyListener {
        void onReady();
    }

    private final GameActivity activity;
    private final View root;
    private final List<Pbn> reconstructedPbnList = new ArrayList<>();
    private Pbn selectedPbn = null;

    private TextView tvNorthCards, tvSouthCards, tvEastCards, tvWestCards;
    private TextView labelNorth, labelSouth, labelEast, labelWest;
    private TextView tvMiddle1History, tvMiddle2History, tvMiddle3History;
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

            labelNorth = root.findViewById(R.id.label_north);
            labelSouth = root.findViewById(R.id.label_south);
            labelEast = root.findViewById(R.id.label_east);
            labelWest = root.findViewById(R.id.label_west);

            tvMiddle1History = root.findViewById(R.id.tv_middle_1_history);
            tvMiddle2History = root.findViewById(R.id.tv_middle_2_history);
            tvMiddle3History = root.findViewById(R.id.tv_middle_3_history);

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
        if (activity.getHistoryOverlay() != null) {
            activity.getHistoryOverlay().setVisibility(View.VISIBLE);
        }
        root.setVisibility(View.GONE);
        clearViews();
    }

    private void clearViews() {
        if (tvNorthCards != null) tvNorthCards.setText("");
        if (tvSouthCards != null) tvSouthCards.setText("");
        if (tvEastCards != null) tvEastCards.setText("");
        if (tvWestCards != null) tvWestCards.setText("");
        if (tvMiddle2History != null) tvMiddle2History.setText("");
        if (tvMiddle3History != null) tvMiddle3History.setText("");
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
                        android.util.Log.d("plesik", record.gameData.toString());
                        Pbn pbn = new Pbn(activity, record.system);
                        pbn.loadFromJsonObject(new JSONObject(record.gameData));
                        loadedPbns.add(pbn);
                    }
                }

                activity.runOnUiThread(() -> {
                    reconstructedPbnList.clear();
                    reconstructedPbnList.addAll(loadedPbns);
                    reconstructedPbnList.sort((p1, p2) -> Double.compare(p2.getImp(), p1.getImp()));

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
                    root.post(() -> {
                        if (activity.getHistoryOverlay() != null) {
                            activity.getHistoryOverlay().setVisibility(View.GONE);
                        }
                    });

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
        updateTopBarImpDisplay();
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

    private Pbn getMyGamePbn() {
        if (reconstructedPbnList != null) {
            for (Pbn p : reconstructedPbnList) {
                if ("MyGame".equals(p.getBoard())) {
                    return p;
                }
            }
            if (!reconstructedPbnList.isEmpty()) {
                return reconstructedPbnList.get(0);
            }
        }
        return null;
    }

    private void updateTopBarImpDisplay() {
        if (tvMiddle1History == null || tvMiddle2History == null || tvMiddle3History == null) return;

        Pbn myGamePbn = getMyGamePbn();
        String mode = (myGamePbn != null && myGamePbn.getGameMode() != null && !myGamePbn.getGameMode().isEmpty())
                ? myGamePbn.getGameMode() : activity.getGameMode();

        double totalImp = 0.0;
        if (activity.getOverlayStatistic() != null && activity.getOverlayStatistic().getStatsManager() != null) {
            totalImp = activity.getOverlayStatistic().getStatsManager().getCareerImp(mode);
        }

        double diffImp = (myGamePbn != null) ? myGamePbn.getImp() : 0.0;

        tvMiddle1History.setText(activity.getString(R.string.imp_label));
        tvMiddle2History.setText(String.format(Locale.US, "%.1f", totalImp));

        if (diffImp != 0.0) {
            tvMiddle3History.setText(String.format(Locale.US, "(%s%.1f)", (diffImp > 0 ? "+" : ""), diffImp));
            tvMiddle3History.setTextColor(diffImp > 0 ? android.graphics.Color.parseColor("#C8E6C9") : android.graphics.Color.parseColor("#FF5252"));
            tvMiddle3History.setVisibility(View.VISIBLE);
        } else {
            tvMiddle3History.setText("");
            tvMiddle3History.setVisibility(View.GONE);
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
            double imp = pbn.getImp();
            tvImp.setText(String.format(Locale.US, "%s%.1f", (imp > 0 ? "+" : ""), imp));
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
        com.example.bridge.model.Card wistCard = getWistCard(selectedPbn);
        Map<String, List<com.example.bridge.model.Card>> hands = selectedPbn.getInitialHands();
        if (hands != null) {
            updateHandTextView(tvNorthCards, getHandFromMap(hands, "N"), wistCard);
            updateHandTextView(tvSouthCards, getHandFromMap(hands, "S"), wistCard);
            updateHandTextView(tvEastCards, getHandFromMap(hands, "E"), wistCard);
            updateHandTextView(tvWestCards, getHandFromMap(hands, "W"), wistCard);

            updatePlayerLabelsWithHcp(hands);
        }
    }

    private com.example.bridge.model.Card getWistCard(Pbn pbn) {
        if (pbn == null) return null;
        List<com.example.bridge.model.Trick> playHistory = pbn.getPlayHistory();
        if (playHistory != null && !playHistory.isEmpty()) {
            com.example.bridge.model.Trick firstTrick = playHistory.get(0);
            if (firstTrick != null && firstTrick.getCardsOnTable() != null && !firstTrick.getCardsOnTable().isEmpty()) {
                return firstTrick.getCardsOnTable().get(0);
            }
        }
        return null;
    }

    private void updatePlayerLabelsWithHcp(Map<String, List<com.example.bridge.model.Card>> hands) {
        if (hands == null) return;

        updatePlayerLabel(labelNorth, R.string.player_north, getHandFromMap(hands, "N"));
        updatePlayerLabel(labelSouth, R.string.player_south, getHandFromMap(hands, "S"));
        updatePlayerLabel(labelEast, R.string.player_east, getHandFromMap(hands, "E"));
        updatePlayerLabel(labelWest, R.string.player_west, getHandFromMap(hands, "W"));
    }

    private List<com.example.bridge.model.Card> getHandFromMap(Map<String, List<com.example.bridge.model.Card>> hands, String dir) {
        if (hands == null || dir == null) return null;
        List<com.example.bridge.model.Card> hand = hands.get(dir);
        if (hand != null) return hand;
        switch (dir.toUpperCase()) {
            case "N": case "NORTH": return hands.get("North");
            case "S": case "SOUTH": return hands.get("South");
            case "E": case "EAST": return hands.get("East");
            case "W": case "WEST": return hands.get("West");
            default: return null;
        }
    }

    private void updatePlayerLabel(TextView tv, int stringResId, List<com.example.bridge.model.Card> hand) {
        if (tv == null) return;
        String baseName = activity.getString(stringResId);
        int hcp = calculateHandHcp(hand);
        tv.setText(String.format(Locale.US, "%s %d HCP", baseName, hcp));
    }

    private int calculateHandHcp(List<com.example.bridge.model.Card> hand) {
        if (hand == null) return 0;
        int total = 0;
        for (com.example.bridge.model.Card card : hand) {
            if (card != null && card.getRank() != null) {
                total += card.getRank().hcp;
            }
        }
        return total;
    }

    private void updateBiddingHistory(Pbn pbn) {
        if (biddingAdapter == null) return;
        biddingList.clear();
        List<String> auction = pbn.getAuction();
        boolean isSingleMyGame = "single".equalsIgnoreCase(pbn.getGameMode()) && "MyGame".equals(pbn.getBoard());
        
        List<String> descs = new ArrayList<>();
        if (isSingleMyGame && com.example.bridge.core.SettingsManager.getInstance(activity).isShowExplanation()) {
            descs = com.example.bridge.ui.biddings.AuctionExplanationHelper.generateExplanations(pbn);
        }

        List<String> adapterDescs = new ArrayList<>();
        if (auction != null && !auction.isEmpty()) {
            String dealer = pbn.toJsonObject().optString("Dealer", "W");
            int offset = 0;
            if ("North".equals(dealer) || "N".equals(dealer)) offset = 1;
            else if ("East".equals(dealer) || "E".equals(dealer)) offset = 2;
            else if ("South".equals(dealer) || "S".equals(dealer)) offset = 3;

            for (int i = 0; i < offset; i++) {
                biddingList.add("-");
                adapterDescs.add("");
            }
            biddingList.addAll(auction);
            adapterDescs.addAll(descs);
        }

        biddingAdapter.setDescriptions(adapterDescs);
        biddingAdapter.setPreviewSelection("");
        biddingAdapter.notifyDataSetChanged();
    }

    private void updateHandTextView(TextView tv, List<com.example.bridge.model.Card> hand, com.example.bridge.model.Card wistCard) {
        if (tv == null) return;
        tv.setTextColor(android.graphics.Color.BLACK);
        tv.setText(formatHandForDisplay(hand, wistCard), android.widget.TextView.BufferType.SPANNABLE);
    }

    private android.text.SpannableStringBuilder formatHandForDisplay(List<com.example.bridge.model.Card> hand, com.example.bridge.model.Card wistCard) {
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

            List<com.example.bridge.model.Card> suitCards = new ArrayList<>();
            for (com.example.bridge.model.Card card : hand) {
                if (card.getSuit() == suits[i]) {
                    suitCards.add(card);
                }
            }
            suitCards.sort((c1, c2) -> Integer.compare(c2.getRank().ordinal(), c1.getRank().ordinal()));

            for (int j = 0; j < suitCards.size(); j++) {
                com.example.bridge.model.Card card = suitCards.get(j);
                int cardStart = ssb.length();
                ssb.append(formatRank(card.getRank()));
                int cardEnd = ssb.length();

                boolean isWist = wistCard != null 
                        && card.getSuit() == wistCard.getSuit() 
                        && card.getRank() == wistCard.getRank();

                if (isWist) {
                    ssb.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), cardStart, cardEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new android.text.style.ForegroundColorSpan(android.graphics.Color.BLACK), cardStart, cardEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new android.text.style.UnderlineSpan(), cardStart, cardEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    ssb.setSpan(new android.text.style.ForegroundColorSpan(android.graphics.Color.BLACK), cardStart, cardEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (j < suitCards.size() - 1) ssb.append(" ");
            }
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
