package com.example.bridge;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.Button;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Suit;
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

    private int lastSnScore = 0;
    private int lastWeScore = 0;

    private GameActivityHistory gameHistory;
    private GameActivityTop gameActivityTop;

    private SharedPref sharedPref;
    private boolean isReplayingFromHistory = false;

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

        startBar = findViewById(R.id.start_bar);
        btn_deal = findViewById(R.id.btn_deal);
        btnClaim = findViewById(R.id.btn_claim);
        loadingIndicator = findViewById(R.id.loading_indicator);

        btnNewDeal = findViewById(R.id.btn_new_deal);

        initGame();
        gameActivityTop = new GameActivityTop(this);
        sharedPref = new SharedPref(this, gameActivityTop);
        gameHistory = new GameActivityHistory(this, gameController);

        // Check if we are replaying a game from history
        String replayedGameJson = getIntent().getStringExtra("replayedGameJson");
        if (replayedGameJson != null) {
            isReplayingFromHistory = true;
            startBar.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
            findViewById(R.id.main).postDelayed(() -> loadGameFromHistory(replayedGameJson), 300);
        } else {
            setupRecyclerView();

            Map<String, List<Card>> savedDeal = sharedPref.loadSavedDeal();
            if (savedDeal != null) {
                gameController.restoreCards(savedDeal);
            } else {
                gameController.dealCards();
            }
        }

        btnNewDeal.setOnClickListener(v -> {
            gameActivityTop.hideContract();
            gameController.resetTable();
            gameHistory.hide();

            if (isReplayingFromHistory) {
                // Play again with same hands
                gameController.restoreCards(new LinkedHashMap<>(initialPlayerHands));
                onVisibleStartBar(true);
            } else {
                loadingIndicator.setVisibility(View.VISIBLE);
                setTotalScore(sharedPref.getPrefTotalScore());
                setupRecyclerView();
                v.post(() -> {
                    gameController.dealCards();
                });
            }
        });

        findViewById(R.id.btn_save_game).setOnClickListener(v -> {
            markGameAsSaved();
        });

        btn_deal.setOnClickListener(v -> {
            onVisibleStartBar(false);
            loadingIndicator.setVisibility(View.VISIBLE);

            showTemporaryNotification(R.string.score_deducted);
            sharedPref.setPrefChangeTotalScore(sharedPref.getChangeScore());
            setTotalScore(sharedPref.getPrefTotalScore());
            v.post(() -> {
                gameController.dealCards();
            });
        });
        findViewById(R.id.btn_start).setOnClickListener(v -> {
            onVisibleStartBar(false);
            loadingIndicator.setVisibility(View.VISIBLE);
            sharedPref.incrementGamesPlayed();
            sharedPref.clearSavedDeal(); // Clear the deal once the game starts
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

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (gameHistory.isVisible()) {
                    gameHistory.hide();
                    if (isReplayingFromHistory) {
                        finish();
                    }
                    return;
                }

                if (startBar != null && startBar.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    showExitConfirmationDialog();
                }
            }
        });
    }

    private void showTemporaryNotification(int resId) {
        View notifyView = findViewById(R.id.notification_text);
        if (notifyView != null) {
            ((TextView) notifyView).setText(resId);
            notifyView.setVisibility(View.VISIBLE);
            notifyView.postDelayed(() -> notifyView.setVisibility(View.GONE), 1000);
        }
    }

    private void markGameAsSaved() {
        sharedPref.markLatestGameAsSaved();
        View btnSave = findViewById(R.id.btn_save_game);
        if (btnSave != null) btnSave.setVisibility(View.GONE);
        showTemporaryNotification(R.string.save_success);
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> finish())
                .setNegativeButton(R.string.no, null)
                .show();
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

    @Override
    public void onTotalScore() {
        setTotalScore(sharedPref.getPrefTotalScore());
    }

    void setTotalScore(int totalScore, int changeScore) {
        gameActivityTop.setTotalScore(totalScore, changeScore);
    }

    void setTotalScore(int totalScore) {
        gameActivityTop.setTotalScore(totalScore);
    }

    @Override
    public void onClaimButtonVisibilityChanged(boolean visible) {
        if (btnClaim != null) {
            btnClaim.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onScoreUpdated(int snScore, int weScore) {
        this.lastSnScore = snScore;
        this.lastWeScore = weScore;
        gameActivityTop.updateScores(snScore, weScore);
    }

    @Override
    public void onGameEnded(int snScore, int weScore, Contract contract, List<Trick> history, int claim) {
        this.lastSnScore = snScore;
        this.lastWeScore = weScore;
        
        if (!isReplayingFromHistory) {
            sharedPref.setScore(contract, snScore);
            sharedPref.addGameToHistory(contract, snScore, history, claim, initialPlayerHands);
            
            View btnSave = findViewById(R.id.btn_save_game);
            if (btnSave != null) btnSave.setVisibility(View.VISIBLE);
        } else {
            // Replay mode adjustments
            View btnSave = findViewById(R.id.btn_save_game);
            if (btnSave != null) btnSave.setVisibility(View.GONE);
            if (btnNewDeal instanceof Button) {
                ((Button) btnNewDeal).setText(R.string.play_again);
            }
        }

        findViewById(R.id.main).postDelayed(() -> {
            gameHistory.showResults(history, claim, snScore);
        }, 500);
    }

    private void loadGameFromHistory(String json) {
        try {
            org.json.JSONObject game = new org.json.JSONObject(json);
            Contract contract = Contract.fromString(game.getString("contract"));
            int snScoreFromGame = game.optInt("snScore", 0);
            int claim = game.optInt("claim", 0);

            org.json.JSONObject handsJson = game.getJSONObject("hands");
            initialPlayerHands.clear();
            for (String direction : new String[]{"North", "East", "South", "West"}) {
                org.json.JSONArray handArray = handsJson.getJSONArray(direction);
                List<Card> cards = new ArrayList<>();
                for (int i = 0; i < handArray.length(); i++) {
                    String[] parts = handArray.getString(i).split(":");
                    cards.add(new Card(Suit.valueOf(parts[0]), Rank.valueOf(parts[1])));
                }
                initialPlayerHands.put(direction, cards);
            }

            List<Trick> history = new ArrayList<>();
            org.json.JSONArray tricksArray = game.optJSONArray("playHistory");
            if (tricksArray != null) {
                for (int i = 0; i < tricksArray.length(); i++) {
                    org.json.JSONObject trickJson = tricksArray.getJSONObject(i);
                    Trick trick = new Trick();
                    trick.setWinnerTrick(trickJson.getString("winner"));
                    org.json.JSONObject cardsMap = trickJson.getJSONObject("cards");
                    java.util.Iterator<String> keys = cardsMap.keys();
                    while (keys.hasNext()) {
                        String pName = keys.next();
                        String[] parts = cardsMap.getString(pName).split(":");
                        trick.addCard(pName, new Card(Suit.valueOf(parts[0]), Rank.valueOf(parts[1])));
                    }
                    history.add(trick);
                }
            }

            // Prepare UI
            loadingIndicator.setVisibility(View.GONE);
            gameController.setCurrentContract(contract);
            gameActivityTop.setContract(contract);

            // UI adjustments for Replay mode
            View btnSave = findViewById(R.id.btn_save_game);
            if (btnSave != null) btnSave.setVisibility(View.GONE);
            if (btnNewDeal instanceof com.google.android.material.button.MaterialButton) {
                com.google.android.material.button.MaterialButton mBtn = (com.google.android.material.button.MaterialButton) btnNewDeal;
                mBtn.setText(R.string.play_again);
                mBtn.setIconResource(R.drawable.ic_arrow);
            }

            gameHistory.showResults(history, claim, snScoreFromGame);
        } catch (Exception e) {
            e.printStackTrace();
            loadingIndicator.setVisibility(View.GONE);
            gameController.dealCards();
        }
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

            Button btnStart = findViewById(R.id.btn_start);
            if (btnStart != null) {
                int gamesCount = sharedPref.getGamesPlayed();
                String baseText = getString(R.string.Start);
                String countText = getString(R.string.games_count_format, gamesCount);
                
                SpannableStringBuilder ssb = new SpannableStringBuilder(baseText + "\n" + countText);
                ssb.setSpan(new RelativeSizeSpan(0.6f), baseText.length() + 1, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                
                btnStart.setText(ssb);
                btnStart.setLineSpacing(0f, 0.8f);
            }

            if (btn_deal != null) {
                String dealText = getString(R.string.deal);
                String costText = getString(R.string.deal_cost);

                SpannableStringBuilder ssb = new SpannableStringBuilder(dealText + "\n" + costText);
                ssb.setSpan(new RelativeSizeSpan(0.6f), dealText.length() + 1, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                btn_deal.setText(ssb);
                btn_deal.setLineSpacing(0f, 0.8f);

                if (sharedPref.getPrefTotalScore() <= 0) {
                    btn_deal.setVisibility(View.GONE);
                } else {
                    btn_deal.setVisibility(View.VISIBLE);
                }
            }
        } else {
            startBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTurnChanged(String playerName) {
        if (loadingIndicator != null) loadingIndicator.setVisibility(View.GONE);
        gameActivityTop.updateTurn(playerName);

        if ("North".equals(playerName) || "South".equals(playerName)) {
            isProcessingMove = false;
        }
    }

    @Override
    public void onContractDetermined(Contract contract) {
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
        sharedPref.saveDeal(players);
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
