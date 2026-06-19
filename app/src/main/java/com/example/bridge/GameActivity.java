package com.example.bridge;

import android.widget.Button;
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
import com.example.bridge.model.Trick;

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

    private final Map<String, List<Card>> initialPlayerHands = new LinkedHashMap<>();
    private View startBar;
    private View btnClaim;
    private View loadingIndicator;
    private boolean isProcessingMove = false;

    private View btnNewDeal;
    private Button btn_deal;
    private int snScore;
    private GameActivityHistory gameHistory;
    private GameActivityTop gameActivityTop;

    private TextView nameNorth, nameSouth, nameEast, nameWest;

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

        nameNorth = findViewById(R.id.name_north);
        nameSouth = findViewById(R.id.name_south);
        nameEast = findViewById(R.id.name_east);
        nameWest = findViewById(R.id.name_west);

        startBar = findViewById(R.id.start_bar);
        btn_deal = findViewById(R.id.btn_deal);
        btnClaim = findViewById(R.id.btn_claim);
        loadingIndicator = findViewById(R.id.loading_indicator);

        btnNewDeal = findViewById(R.id.btn_new_deal);

        initGame();
        gameActivityTop = new GameActivityTop(this);
        gameHistory = new GameActivityHistory(this, gameController);

        btnNewDeal.setOnClickListener(v -> {
            gameActivityTop.hideContract();
            gameController.resetTable();
            gameHistory.hide();
            loadingIndicator.setVisibility(View.VISIBLE);
            v.post(() -> {
                gameController.dealCards();
            });
        });

        findViewById(R.id.btn_save_game).setOnClickListener(v -> {
            // Future use: Save logic
        });

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
            loadingIndicator.setVisibility(View.VISIBLE);
            v.post(() -> {
                gameController.startGame();
            });
        });
        btnClaim.setOnClickListener(v -> {
            if (isProcessingMove) return;
            isProcessingMove = true;
            btnClaim.setVisibility(View.GONE);
            gameController.claimRest();
        });
    }

    private void setScore(String contract, int snScoreValue) {
        this.snScore = snScoreValue;
        int level = 0;
        try {
            level = Integer.parseInt(contract.split(" ")[0].trim());
        } catch (Exception e) {
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
        setTotalScore(getPrefTotalScore(), handScore);
    }

    public Map<String, List<Card>> getInitialPlayerHands() {
        return initialPlayerHands;
    }

    public void updateSimulationScores(int sn, int we) {
        gameActivityTop.updateScores(sn, we);
    }

    public void showPlayedCardInSim(Card card, String playerName) {
        Player p = players.get(playerName);
        if (p != null) {
            showPlayedCard(card, p.getPlayedCardContainer());
        }
    }


    public int getPlayerColumn(String name) {
        if ("West".equals(name)) return 0;
        if ("North".equals(name)) return 1;
        if ("East".equals(name)) return 2;
        if ("South".equals(name)) return 3;
        return -1;
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
        gameActivityTop.setTotalScore(totalScore, changeScore);
    }

    private void setTotalScore(int totalScore) {
        gameActivityTop.setTotalScore(totalScore);
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
        gameActivityTop.updateScores(snScore, weScore);
    }

    @Override
    public void onGameEnded(int snScore, int weScore, String contract, List<Trick> history, int claim) {
        setScore(contract, snScore);
        findViewById(R.id.main).postDelayed(() -> {
            gameHistory.showResults(history, claim, snScore);
        }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onVisibleStartBar(Boolean isVisible) {
        if (isVisible) {
            startBar.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.GONE);
            if (getPrefTotalScore() < 0) {
                btn_deal.setVisibility(View.GONE);
            } else {
                btn_deal.setVisibility(View.VISIBLE);
            }
        } else {
            startBar.setVisibility(View.GONE);
        }
    }

    public void updateTurn(String playerName) {
        nameNorth.setBackgroundResource(R.drawable.green_frame);
        nameSouth.setBackgroundResource(R.drawable.green_frame);
        nameEast.setBackgroundResource(R.drawable.green_frame);
        nameWest.setBackgroundResource(R.drawable.green_frame);

        if (playerName == null) return;

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
    public void onTurnChanged(String playerName) {
        if (loadingIndicator != null) loadingIndicator.setVisibility(View.GONE);
        updateTurn(playerName);

        if (playerName == null) return;

        // Unlock interaction ONLY if it's a human player's turn
        if ("North".equals(playerName) || "South".equals(playerName)) {
            isProcessingMove = false;
        }
    }

    @Override
    public void onContractDetermined(String contract) {
        isProcessingMove = false;
        gameActivityTop.setContract(contract);
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
        initialPlayerHands.clear();
        for (Player player : players.values()) {
            initialPlayerHands.put(player.getName(), new ArrayList<>(player.getHand()));
        }
    }

    @Override
    public void onInitialHandsHtmlClear() {
        initialPlayerHands.clear();
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
        gameActivityTop.onTableCleared(trickCards);
    }

    @Override
    public void onClearLastCards(List<Card> cardsOnTable) {
        if (cardsOnTable != null && cardsOnTable.size() > 1) {
            gameActivityTop.clearLastCards();
        }
    }

    private void updateDisplayHandSouth() {
        if (players.get("South") == null) return;
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
        if (players.get("North") == null) return;
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
