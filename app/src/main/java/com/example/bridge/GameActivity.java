package com.example.bridge;

import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.model.Card;
import com.example.bridge.model.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements GameController.GameCallback {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    public static final Card GHOST_CARD = new Card(null, null);
    private static final String PREFS_NAME = "BridgePrefs";
    private static final String KEY_CAREER_SCORE = "careerScore";

    private Map<String, Player> players;
    private GameController gameController;

    private CardAdapter southAdapter;
    private CardAdapter northAdapter;
    private final List<Card> displayHandSouth = new ArrayList<>();
    private final List<Card> displayHandNorth = new ArrayList<>();

    private FrameLayout playedCardContainerSouth;
    private FrameLayout playedCardContainerNorth;
    private FrameLayout playedCardContainerWest;
    private FrameLayout playedCardContainerEast;

    private TextView tvLastNorth, tvLastSouth, tvLastEast, tvLastWest;
    private TextView tvScoreSN, tvScoreWE, tvMiddle1, tvMiddle2, tvMiddle3;
    private final Map<String, String> initialHandsHtml = new LinkedHashMap<>();
    private TextView nameNorth, nameSouth, nameEast, nameWest;
    private TextView tvContract;
    private ImageView ivContractSuit;
    private View contractContainer;
    private View startBar;
    private View btnClaim;
    private View loadingIndicator;
    private boolean isProcessingMove = false;

    // Results Overlay Views
    private View resultsOverlay;
    private TextView tvNorthRes, tvSouthRes, tvEastRes, tvWestRes;
    private TableLayout tableHistoryRes;
    private View btnNewDeal;

    private Button btn_deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        setupWindowInsets();

        playedCardContainerSouth = findViewById(R.id.container_played_south);
        playedCardContainerNorth = findViewById(R.id.container_played_north);
        playedCardContainerWest = findViewById(R.id.container_played_west);
        playedCardContainerEast = findViewById(R.id.container_played_east);

        tvLastNorth = findViewById(R.id.n_last_card);
        tvLastSouth = findViewById(R.id.s_last_card);
        tvLastEast = findViewById(R.id.e_last_card);
        tvLastWest = findViewById(R.id.w_last_card);

        nameNorth = findViewById(R.id.name_north);
        nameSouth = findViewById(R.id.name_south);
        nameEast = findViewById(R.id.name_east);
        nameWest = findViewById(R.id.name_west);

        tvScoreSN = findViewById(R.id.sn_score);
        tvScoreWE = findViewById(R.id.we_score);

        tvMiddle1 = findViewById(R.id.tv_middle_1);
        tvMiddle2 = findViewById(R.id.tv_middle_2);
        tvMiddle3 = findViewById(R.id.tv_middle_3);

        tvContract = findViewById(R.id.game_contract);
        ivContractSuit = findViewById(R.id.iv_contract_suit);
        contractContainer = findViewById(R.id.game_contract_container);
        startBar = findViewById(R.id.start_bar);
        btn_deal = findViewById(R.id.btn_deal);
        btnClaim = findViewById(R.id.btn_claim);
        loadingIndicator = findViewById(R.id.loading_indicator);

        // Results Overlay Init
        resultsOverlay = findViewById(R.id.results_overlay);
        tvNorthRes = findViewById(R.id.tv_north_cards_res);
        tvSouthRes = findViewById(R.id.tv_south_cards_res);
        tvEastRes = findViewById(R.id.tv_east_cards_res);
        tvWestRes = findViewById(R.id.tv_west_cards_res);
        tableHistoryRes = findViewById(R.id.table_history_res);
        btnNewDeal = findViewById(R.id.btn_new_deal);

        btnNewDeal.setOnClickListener(v -> {
            resultsOverlay.setVisibility(View.GONE);
            dealNewCards();
        });

        initGame();
        setupRecyclerView();
        gameController.dealCards();

        btn_deal.setOnClickListener(v -> {
            onVisibleStartBar(false);
            loadingIndicator.setVisibility(View.VISIBLE);
            setPrefChangeTotalScore(-1);
            setTotalScore(getPrefTotalScore());
            v.post(() -> {
                gameController.dealCards();
            });
        });
        findViewById(R.id.btn_start).setOnClickListener(v -> {
            onVisibleStartBar(false);
            gameController.startGame();
        });
        btnClaim.setOnClickListener(v -> {
            if (isProcessingMove) return;
            isProcessingMove = true;
            btnClaim.setVisibility(View.GONE);
            gameController.claimRest();
        });
    }

    private void setPrefChangeTotalScore(int changeScore) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int careerScore = getPrefTotalScore();
        careerScore += changeScore;
        prefs.edit().putInt(KEY_CAREER_SCORE, careerScore).apply();
    }

    private int getPrefTotalScore() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int careerScore = prefs.getInt(KEY_CAREER_SCORE, 0);
        return careerScore;
    }

    private void setTotalScore(int totalScore, int changeScore) {
        tvMiddle1.setText(getString(R.string.score_label));
        tvMiddle2.setText(String.valueOf(totalScore));
        if (changeScore > 0){
            tvMiddle3.setText("(+"+ changeScore +")");
            tvMiddle3.setTextColor(Color.parseColor("#C8E6C9"));
        } else {
            tvMiddle3.setText("("+ changeScore +")");
            tvMiddle3.setTextColor(0xFFFF0000);
        }
    }

    private void setTotalScore(int totalScore) {
        tvMiddle1.setText(getString(R.string.score_label));
        tvMiddle2.setText(String.valueOf(totalScore));
        tvMiddle3.setText("");
    }


    @Override
    public void onTotalScore() {
        setTotalScore(getPrefTotalScore());
    }

    @Override
    public void onClaimButtonVisibilityChanged(boolean visible) {
        if (btnClaim != null) {
            btnClaim.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onScoreUpdated(int snScore, int weScore) {
        if (tvScoreSN != null) tvScoreSN.setText(getString(R.string.sn_label, snScore));
        if (tvScoreWE != null) tvScoreWE.setText(getString(R.string.we_label, weScore));
    }

    @Override
    public void onGameEnded(int snScore, int weScore, String contract, List<String> history, List<String> historyWinTrick) {
        int level = 0;
        try {
            level = Integer.parseInt(contract.split(" ")[0].trim());
        } catch (Exception e) {
            // malformed
        }

        int requiredTricks = level + 6;
        int handScore = 0;
        if (level > 0) {
            if (snScore >= requiredTricks) {
                handScore = level + (snScore - requiredTricks);
            } else {
                handScore = -level -(requiredTricks - snScore);
            }
        }
        setPrefChangeTotalScore(handScore);
        setTotalScore(getPrefTotalScore(), handScore);

        findViewById(R.id.main).postDelayed(() -> {
            displayResults(history, historyWinTrick);
        }, 500);
    }

    private void displayResults(List<String> history, List<String> historyWinTrick) {
        // Populate hands
        displayHand(tvNorthRes, initialHandsHtml.get("North"));
        displayHand(tvSouthRes, initialHandsHtml.get("South"));
        displayHand(tvEastRes, initialHandsHtml.get("East"));
        displayHand(tvWestRes, initialHandsHtml.get("West"));

        // Populate history
        displayHistory(history, historyWinTrick);

        resultsOverlay.setVisibility(View.VISIBLE);
    }

    private void displayHand(TextView tv, String handHtml) {
        if (tv != null && handHtml != null) {
            Spanned formatted = Html.fromHtml(handHtml, Html.FROM_HTML_MODE_LEGACY);
            tv.setText(formatted);
        }
    }

    private void displayHistory(List<String> history, List<String> historyWinTrick) {
        if (tableHistoryRes == null) return;
        
        // Clear previous entries (keep header row)
        int childCount = tableHistoryRes.getChildCount();
        if (childCount > 1) {
            tableHistoryRes.removeViews(1, childCount - 1);
        }

        if (history == null) return;

        for (int i = 0; i < history.size(); i += 4) {
            TableRow row = new TableRow(this);
            String[] trickData = new String[4]; // W, N, E, S (matches XML)
            int trickIndex = i / 4;
            int winnerCol = -1;

            if (historyWinTrick != null && trickIndex < historyWinTrick.size()) {
                winnerCol = getPlayerColumn(historyWinTrick.get(trickIndex));
            }

            for (int j = 0; j < 4 && (i + j) < history.size(); j++) {
                String entry = history.get(i + j);
                if (entry.startsWith("CLAIM: ")) {
                    int num = 0;
                    try {
                        num = Integer.parseInt(entry.replace("CLAIM: ", ""));
                    } catch (Exception ignored) {}
                    
                    TextView claimTv = new TextView(this);
                    claimTv.setText(getString(R.string.claimed_tricks, num));
                    claimTv.setTextColor(Color.RED);
                    claimTv.setPadding(16, 8, 16, 8);
                    tableHistoryRes.addView(claimTv);
                    return;
                }

                String[] parts = entry.split(": ");
                if (parts.length == 2) {
                    String name = parts[0];
                    String cardStr = parts[1];
                    int col = getPlayerColumn(name);
                    if (col != -1) {
                        trickData[col] = cardStr;
                    }
                }
            }

            for (int c = 0; c < 4; c++) {
                TextView tv = new TextView(this);
                tv.setText(trickData[c] != null ? trickData[c] : "-");
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(8, 16, 8, 16);
                if (c == winnerCol) {
                    tv.setBackgroundResource(R.drawable.white_frame_in_bright_green);
                    tv.setTypeface(null, android.graphics.Typeface.BOLD);
                }
                row.addView(tv);
            }
            tableHistoryRes.addView(row);
        }
    }

    private int getPlayerColumn(String name) {
        if ("West".equals(name)) return 0;
        if ("North".equals(name)) return 1;
        if ("East".equals(name)) return 2;
        if ("South".equals(name)) return 3;
        return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dealNewCards() {
        if (startBar != null) startBar.setVisibility(View.VISIBLE);
        gameController.dealCards();
    }

    @Override
    public void onVisibleStartBar(Boolean isVisible){
        if (isVisible) {
            startBar.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.GONE);
            if (getPrefTotalScore() <0 ){
                btn_deal.setVisibility(View.GONE);
            } else {
                btn_deal.setVisibility(View.VISIBLE);
            }
        } else {
            startBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onTurnChanged(String playerName) {
        // Reset all to a frame with no border (just the green background)
        nameNorth.setBackgroundResource(R.drawable.green_frame);
        nameSouth.setBackgroundResource(R.drawable.green_frame);
        nameEast.setBackgroundResource(R.drawable.green_frame);
        nameWest.setBackgroundResource(R.drawable.green_frame);

        if (playerName == null) return;

        // Unlock interaction ONLY if it's a human player's turn
        if ("North".equals(playerName) || "South".equals(playerName)) {
            isProcessingMove = false;
        }

        // Apply white_frame (which has the white border) to current player's name
        switch (playerName) {
            case "North":
                nameNorth.setBackgroundResource(R.drawable.white_frame);
                break;
            case "South":
                nameSouth.setBackgroundResource(R.drawable.white_frame);
                break;
            case "East":
                nameEast.setBackgroundResource(R.drawable.white_frame);
                break;
            case "West":
                nameWest.setBackgroundResource(R.drawable.white_frame);
                break;
        }
    }

    @Override
    public void onContractDetermined(String contract) {
        isProcessingMove = false; // Allow Start button to be clicked
        loadingIndicator.setVisibility(View.GONE);
        if (contractContainer != null)
            contractContainer.setBackgroundResource(R.drawable.white_frame_in_bright_green);

        if (contract == null || contract.equals("PASS")) {
            tvContract.setText(getString(R.string.contract_pass));
            if (ivContractSuit != null) ivContractSuit.setVisibility(View.GONE);
            return;
        }

        String[] parts = contract.split(" ");
        if (parts.length < 2) {
            tvContract.setText(contract);
            if (ivContractSuit != null) ivContractSuit.setVisibility(View.GONE);
            return;
        }

        String count = parts[0];
        String color = parts[1];

        tvContract.setText(" " + count);
        if (ivContractSuit != null) {
            if ("NT".equals(color)) {
                tvContract.setText(" " + count + " " + getString(R.string.suit_nt));
                ivContractSuit.setVisibility(View.GONE);
            } else {
                ivContractSuit.setVisibility(View.VISIBLE);
                switch (color) {
                    case "Spades":
                        ivContractSuit.setImageResource(R.drawable.spades);
                        break;
                    case "Hearts":
                        ivContractSuit.setImageResource(R.drawable.heart);
                        break;
                    case "Diamonds":
                        ivContractSuit.setImageResource(R.drawable.diamonds);
                        break;
                    case "Clubs":
                        ivContractSuit.setImageResource(R.drawable.clubs);
                        break;
                    default:
                        tvContract.setText(" " + contract);
                        ivContractSuit.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initGame() {
        players = new LinkedHashMap<>();
        players.put("North", new Player("North", playedCardContainerNorth));
        players.put("East", new Player("East", playedCardContainerEast));
        players.put("South", new Player("South", playedCardContainerSouth));
        players.put("West", new Player("West", playedCardContainerWest));
        gameController = new GameController(players, this);
    }

    private void setupRecyclerView() {
        RecyclerView rvSouth = findViewById(R.id.rv_hand_south);
        RecyclerView rvNorth = findViewById(R.id.rv_hand_north);

        rvSouth.setLayoutManager(createLayoutManager(displayHandSouth));
        southAdapter = new CardAdapter(displayHandSouth);
        southAdapter.setOnCardClickListener(card -> {
            if (isProcessingMove) return;
            Player south = players.get("South");
            if (south.isCurrentMove() && gameController.isLegalMove(south, card)) {
                isProcessingMove = true;
                onClaimButtonVisibilityChanged(false);
                gameController.playCard(south, card);
            } else {
                southAdapter.clearSelection();
            }
        });
        rvSouth.setAdapter(southAdapter);

        rvNorth.setLayoutManager(createLayoutManager(displayHandNorth));
        northAdapter = new CardAdapter(displayHandNorth);
        northAdapter.setOnCardClickListener(card -> {
            if (isProcessingMove) return;
            Player north = players.get("North");
            if (north.isCurrentMove() && gameController.isLegalMove(north, card)) {
                isProcessingMove = true;
                onClaimButtonVisibilityChanged(false);
                gameController.playCard(north, card);
            } else {
                northAdapter.clearSelection();
            }
        });
        rvNorth.setAdapter(northAdapter);
    }

    private GridLayoutManager createLayoutManager(List<Card> displayList) {
        GridLayoutManager lm = new GridLayoutManager(this, 14);
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (displayList.get(position) == null) ? 1 : 2;
            }
        });
        return lm;
    }

    @Override
    public void onHandUpdated(String playerName) {
        if ("North".equals(playerName)) {
            updateDisplayHandNorth();
        } else if ("South".equals(playerName)) {
            updateDisplayHandSouth();
        }
    }

    @Override
    public void onInitialHandsHtml() {
        // Capture hands AFTER swapping/sorting is complete
        initialHandsHtml.clear();
        for (Player player : players.values()) {
            initialHandsHtml.put(player.getName(), formatHandToHtml(player.getHand()));
        }
    }

    @Override
    public void onInitialHandsHtmlClear() {
        initialHandsHtml.clear();
    }

    private String formatHandToHtml(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        com.example.bridge.model.Suit[] suits = {
                com.example.bridge.model.Suit.SPADES,
                com.example.bridge.model.Suit.HEARTS,
                com.example.bridge.model.Suit.DIAMONDS,
                com.example.bridge.model.Suit.CLUBS
        };

        for (int i = 0; i < suits.length; i++) {
            com.example.bridge.model.Suit suit = suits[i];
            String color = suit.isRed ? "red" : "white";
            sb.append("<b><font color='").append(color).append("'>")
                    .append(suit.symbol).append("</font></b>&nbsp;");

            sb.append("<b><font color='white'>");
            boolean first = true;
            for (Card card : hand) {
                if (card.getSuit() == suit) {
                    if (!first) sb.append("&nbsp;");
                    sb.append(card.getRank().display);
                    first = false;
                }
            }
            sb.append("</font></b>");
            if (i < suits.length - 1) {
                sb.append("<br/>");
            }
        }
        return sb.toString();
    }

    @Override
    public void onCardPlayed(Player player, Card card) {
        showPlayedCard(card, player.getPlayedCardContainer());
    }

    @Override
    public void onTableCleared(Map<String, Card> trickCards) {
        FrameLayout[] containers = {playedCardContainerSouth, playedCardContainerNorth, playedCardContainerWest, playedCardContainerEast};
        for (FrameLayout container : containers) {
            if (container != null) container.removeAllViews();
        }

        if (trickCards != null && !trickCards.isEmpty()) {
            updateLastCard(tvLastNorth, trickCards.get("North"));
            updateLastCard(tvLastSouth, trickCards.get("South"));
            updateLastCard(tvLastEast, trickCards.get("East"));
            updateLastCard(tvLastWest, trickCards.get("West"));
        } else {
            clearLastCards();
        }
    }

    @Override
    public void onClearLastCards(List<Card> cardsOnTable) {
        if (cardsOnTable != null && cardsOnTable.size() > 1) {
            clearLastCards();
        }
    }

    private void clearLastCards() {
        TextView[] lastCardTVs = {tvLastNorth, tvLastSouth, tvLastEast, tvLastWest};
        for (TextView tv : lastCardTVs) {
            if (tv != null) {
                tv.setText("");
                tv.setBackground(null);
            }
        }
    }

    private void updateLastCard(TextView tv, Card card) {
        if (card != null) {
            tv.setText(" " + card.getRank().display + " " + card.getSuit().symbol);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setBackgroundResource(R.drawable.white_frame_in_bright_green);
        }
    }

    private void updateDisplayHandSouth() {
        List<Card> actualHand = players.get("South").getHand();
        displayHandSouth.clear();

        int row1End = Math.min(7, actualHand.size());
        addCardsWithSpacersSouth(actualHand.subList(0, row1End));
        if (actualHand.size() > 7) {
            addCardsWithSpacersSouth(actualHand.subList(7, actualHand.size()));
        }
        southAdapter.clearSelection();
        southAdapter.notifyDataSetChanged();
    }

    private void updateDisplayHandNorth() {
        List<Card> actualHand = players.get("North").getHand();
        displayHandNorth.clear();

        int total = actualHand.size();
        int row2Count = Math.min(7, total);
        addCardsWithSpacersNorth(displayHandNorth, actualHand.subList(row2Count, total));
        addCardsWithSpacersNorth(displayHandNorth, actualHand.subList(0, row2Count));
        northAdapter.clearSelection();
        northAdapter.notifyDataSetChanged();
    }

    private void addCardsWithSpacersSouth(List<Card> rowCards) {
        if (rowCards.isEmpty()) {
            for (int i = 0; i < 7; i++) displayHandSouth.add(GHOST_CARD);
            return;
        }
        int cardSpans = rowCards.size() * 2;
        int totalPadding = 14 - cardSpans;
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        for (int i = 0; i < leftPadding; i++) displayHandSouth.add(null);
        displayHandSouth.addAll(rowCards);
        for (int i = 0; i < rightPadding; i++) displayHandSouth.add(null);
    }

    private void addCardsWithSpacersNorth(List<Card> displayList, List<Card> rowCards) {
        if (rowCards.isEmpty()) {
            for (int i = 0; i < 7; i++) displayList.add(GHOST_CARD);
            return;
        }
        int cardSpans = rowCards.size() * 2;
        int totalPadding = 14 - cardSpans;
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        for (int i = 0; i < leftPadding; i++) displayList.add(null);
        displayList.addAll(rowCards);
        for (int i = 0; i < rightPadding; i++) displayList.add(null);
    }

    private void showPlayedCard(Card card, FrameLayout container) {
        container.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.item_card, container, false);

        TextView tvRank = view.findViewById(R.id.tv_rank);
        ImageView ivSmall = view.findViewById(R.id.iv_suit_small);
        ImageView ivLarge = view.findViewById(R.id.iv_suit_large);

        tvRank.setText(card.getRank().display);
        ivSmall.setImageResource(card.getSuit().resId);
        ivLarge.setImageResource(card.getSuit().resId);
        tvRank.setTextColor(card.getSuit().isRed ? 0xFFFF0000 : 0xFF000000);

        container.addView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameController.cleanup();
    }
}
