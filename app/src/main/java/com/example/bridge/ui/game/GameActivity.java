package com.example.bridge.ui.game;

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

import com.example.bridge.R;
import com.example.bridge.core.LocaleHelper;
import com.example.bridge.core.SharedPref;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Trick;
import com.example.bridge.ui.history.GameActivityHistory;
import com.example.bridge.ui.history.HistoryListAdapter;
import com.example.bridge.ui.settings.SettingsActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
    private View historyOverlay;
    private HistoryListAdapter historyOverlayAdapter;
    private List<org.json.JSONObject> filteredHistoryList = new ArrayList<>();
    private List<org.json.JSONObject> fullHistoryList = new ArrayList<>();
    private TextView tvEmptyHistory;
    private android.widget.CheckBox cbOnlySavedHistory;
    private android.widget.Spinner spinnerLevelHistory, spinnerSuitHistory, spinnerResultHistory;

    private SharedPref sharedPref;
    private boolean isReplayingFromHistory = false;
    private int snScoreFromHistory = 0;
    private int autoSnScoreFromHistory = 0;
    private List<Trick> autoPlayHistoryFromHistory = null;

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
        historyOverlay = findViewById(R.id.history_overlay);

        // Always setup RecyclerView before loading any data to prevent NullPointerException
        setupRecyclerView();
        //setupHistoryOverlay();

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_game);
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_game) {
                    historyOverlay.setVisibility(View.GONE);
                    bottomNav.setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    historyOverlay.setVisibility(View.VISIBLE);
                    bottomNav.setVisibility(View.VISIBLE);
                    bottomNav.bringToFront(); // Force it to the front
                    //refreshHistoryList();
                    return true;
                } else if (itemId == R.id.nav_settings) {
                    startActivity(new Intent(this, SettingsActivity.class));
                    return false; // Don't select settings in game nav
                }
                return false;
            });
        }

        // Check if we are replaying a game from history
        String replayedGameJson = getIntent().getStringExtra("replayedGameJson");
        if (replayedGameJson != null) {
            isReplayingFromHistory = true;
            startBar.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
            findViewById(R.id.main).postDelayed(() -> loadGameFromHistory(replayedGameJson, true), 300);
        } else {
            SharedPref.SavedState savedState = sharedPref.loadSavedDeal();
            if (savedState != null) {
                gameController.restoreCards(savedState.hands, savedState.contract);
            } else {
                gameController.dealCards();
            }
        }

        btnNewDeal.setOnClickListener(v -> {
            gameActivityTop.hideContract();
            gameController.resetTable();
            gameHistory.hide();

            if (isReplayingFromHistory) {
                loadGameFromHistory(replayedGameJson, false);
                if (southAdapter != null) southAdapter.setCardsEnabled(true);
                if (northAdapter != null) northAdapter.setCardsEnabled(true);
                v.post(() -> {
                    gameController.startGame();
                });
            } else {
                loadingIndicator.setVisibility(View.VISIBLE);
                setTotalScore(sharedPref.getPrefTotalScore());
                setupRecyclerView();

                v.post(() -> {
                    gameController.dealCards();
                });
                onSaveDeal();
            }
        });

        /*findViewById(R.id.btn_save_game).setOnClickListener(v -> {
            markGameAsSaved();
        });*/

        btn_deal.setOnClickListener(v -> {
            onVisibleStartBar(false);
            loadingIndicator.setVisibility(View.VISIBLE);

            //showTemporaryNotification(R.string.score_deducted);
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
            
            if (southAdapter != null) southAdapter.setCardsEnabled(true);
            if (northAdapter != null) northAdapter.setCardsEnabled(true);

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
                if (historyOverlay != null && historyOverlay.getVisibility() == View.VISIBLE) {
                    historyOverlay.setVisibility(View.GONE);
                    com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                    if (bottomNav != null) {
                        bottomNav.setVisibility(View.VISIBLE);
                        bottomNav.setSelectedItemId(R.id.nav_game);
                    }
                    return;
                }

                if (gameHistory.isVisible()) {
                    gameHistory.hide();
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

    /*private void showTemporaryNotification(int resId) {
        View notifyView = findViewById(R.id.notification_text);
        if (notifyView != null) {
            ((TextView) notifyView).setText(resId);
            notifyView.setVisibility(View.VISIBLE);
            notifyView.postDelayed(() -> notifyView.setVisibility(View.GONE), 1000);
        }
    }*/

    /*private void markGameAsSaved() {
        sharedPref.markLatestGameAsSaved();
        View btnSave = findViewById(R.id.btn_save_game);
        if (btnSave != null) btnSave.setVisibility(View.GONE);
        showTemporaryNotification(R.string.save_success);
    }*/

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

    public void setTotalScore(int totalScore, int changeScore) {
        gameActivityTop.setTotalScore(totalScore, changeScore);
    }

    public void setTotalScore(int totalScore) {
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
        
        if (southAdapter != null) southAdapter.setCardsEnabled(false);
        if (northAdapter != null) northAdapter.setCardsEnabled(false);

        if (!isReplayingFromHistory) {
            sharedPref.setScore(contract, snScore);

            // Obliczamy optymalną historię robota raz przed zapisem
            List<Trick> autoPlayHistory = gameController.calculateOptimalHistory(initialPlayerHands, contract);
            int autoSnScore = 0;
            for (Trick t : autoPlayHistory) {
                String winner = t.getWinnerTrick();
                if ("North".equals(winner) || "South".equals(winner)) autoSnScore++;
            }

            sharedPref.addGameToHistory(contract, snScore, history, claim, initialPlayerHands, autoSnScore, autoPlayHistory);

            View btnSave = findViewById(R.id.btn_save_game);
            if (btnSave != null) btnSave.setVisibility(View.VISIBLE);

            final int finalAutoSnScore = autoSnScore;
            final List<Trick> finalAutoPlayHistory = autoPlayHistory;
            findViewById(R.id.main).postDelayed(() -> {
                gameHistory.showResults(history, claim, snScore, finalAutoSnScore, finalAutoPlayHistory);
            }, 500);
        } else {
            // Replay mode adjustments
            View btnSave = findViewById(R.id.btn_save_game);
            if (btnSave != null) btnSave.setVisibility(View.GONE);
            if (btnNewDeal instanceof com.google.android.material.button.MaterialButton) {
                com.google.android.material.button.MaterialButton mBtn = (com.google.android.material.button.MaterialButton) btnNewDeal;
                mBtn.setText(R.string.play_again);
                mBtn.setIconResource(R.drawable.ic_arrow);
            }

            findViewById(R.id.main).postDelayed(() -> {
                gameHistory.showResults(history, claim, snScoreFromHistory, autoSnScoreFromHistory, autoPlayHistoryFromHistory);
            }, 500);
        }
    }

    private void loadGameFromHistory(String json, boolean isInitialUiLoad) {
        try {
            org.json.JSONObject game = new org.json.JSONObject(json);
            Contract contract = Contract.fromString(game.getString("contract"));
            this.snScoreFromHistory = game.optInt("snScore", 0);
            this.autoSnScoreFromHistory = game.optInt("autoSnScore", 0);
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

            List<Trick> history = parseTricks(game.optJSONArray("playHistory"));
            this.autoPlayHistoryFromHistory = parseTricks(game.optJSONArray("autoPlayHistory"));

            // Restore without bidding to prevent freeze
            gameController.restoreCardsWithContract(new LinkedHashMap<>(initialPlayerHands), contract);

            if (isInitialUiLoad) {
                loadingIndicator.setVisibility(View.GONE);
                startBar.setVisibility(View.GONE);

                View btnSave = findViewById(R.id.btn_save_game);
                if (btnSave != null) btnSave.setVisibility(View.GONE);

                View btnAuto = findViewById(R.id.btn_auto_replay);
                if (btnAuto != null) btnAuto.setVisibility(View.GONE);

                if (btnNewDeal instanceof com.google.android.material.button.MaterialButton) {
                    com.google.android.material.button.MaterialButton mBtn = (com.google.android.material.button.MaterialButton) btnNewDeal;
                    mBtn.setText(R.string.play_again);
                    mBtn.setIconResource(R.drawable.ic_arrow);
                }

                gameHistory.showResults(history, claim, snScoreFromHistory, autoSnScoreFromHistory, autoPlayHistoryFromHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (isInitialUiLoad) {
                loadingIndicator.setVisibility(View.GONE);
                gameController.dealCards();
            }
        }
    }

    private List<Trick> parseTricks(org.json.JSONArray tricksArray) throws org.json.JSONException {
        List<Trick> tricks = new ArrayList<>();
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
                tricks.add(trick);
            }
        }
        return tricks;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onVisibleStartBar(Boolean isVisible) {
        View bottomNav = findViewById(R.id.bottom_navigation);
        if (isVisible) {
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
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

                if (sharedPref.getPrefTotalScore() < 50) {
                    btn_deal.setVisibility(View.GONE);
                } else {
                    btn_deal.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
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
            // No padding on main to allow overlays to reach the very top (under status bar)
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        View topBar = findViewById(R.id.top_bar);
        if (topBar != null) {
            ViewCompat.setOnApplyWindowInsetsListener(topBar, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top + (int)(4 * getResources().getDisplayMetrics().density), v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }

        View filterBar = findViewById(R.id.filters_container_overlay);
        if (filterBar != null) {
            ViewCompat.setOnApplyWindowInsetsListener(filterBar, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top + (int)(12 * getResources().getDisplayMetrics().density), v.getPaddingRight(), (int)(12 * getResources().getDisplayMetrics().density));
                return insets;
            });
        }

        View bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                // Nav icons are pushed up above system buttons
                v.setPadding(0, 0, 0, systemBars.bottom);
                return insets;
            });
        }
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
        southAdapter.setCardsEnabled(false);

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
        northAdapter.setCardsEnabled(false);
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
    public void onSaveDeal() {
        sharedPref.saveDeal(players, gameController.getCurrentContract());
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
        
        int suitColor = card.getSuit().getColor(this);
        tvRank.setTextColor(suitColor);
        ivSmall.setColorFilter(suitColor);
        ivLarge.setColorFilter(suitColor);

        container.addView(view);
    }

    /*private void setupHistoryOverlay() {
        RecyclerView rvHistory = findViewById(R.id.rv_history_overlay);
        tvEmptyHistory = findViewById(R.id.tv_empty_history_overlay);
        cbOnlySavedHistory = findViewById(R.id.cb_filter_saved_overlay);
        spinnerLevelHistory = findViewById(R.id.spinner_level_filter_overlay);
        spinnerSuitHistory = findViewById(R.id.spinner_suit_filter_overlay);
        spinnerResultHistory = findViewById(R.id.spinner_result_filter_overlay);

        if (rvHistory == null) return;

        rvHistory.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        historyOverlayAdapter = new HistoryListAdapter(filteredHistoryList, this::showHistoryDeleteDialog, this::toggleHistorySave, item -> {
            historyOverlay.setVisibility(View.GONE);
            com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.VISIBLE);
                bottomNav.setSelectedItemId(R.id.nav_game);
            }
            
            isReplayingFromHistory = true;
            startBar.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
            loadGameFromHistory(item.toString(), true);
        });
        rvHistory.setAdapter(historyOverlayAdapter);

        setupHistoryFilters();
    }*/

    /*private void setupHistoryFilters() {
        if (spinnerLevelHistory == null) return;
        String[] levelOptions = { getString(R.string.filter_level_all), "1", "2", "3", "4", "5", "6", "7" };
        android.widget.ArrayAdapter<String> levelAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levelOptions);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevelHistory.setAdapter(levelAdapter);

        String[] suitOptions = {
                getString(R.string.filter_suit_all),
                getString(R.string.filter_contract_nt),
                getString(R.string.filter_contract_spades),
                getString(R.string.filter_contract_hearts),
                getString(R.string.filter_contract_diamonds),
                getString(R.string.filter_contract_clubs)
        };
        android.widget.ArrayAdapter<String> suitAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, suitOptions);
        suitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSuitHistory.setAdapter(suitAdapter);

        String[] resultOptions = {
                getString(R.string.filter_result_all),
                getString(R.string.filter_result_won),
                getString(R.string.filter_result_lost)
        };
        android.widget.ArrayAdapter<String> resultAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resultOptions);
        resultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResultHistory.setAdapter(resultAdapter);

        cbOnlySavedHistory.setOnCheckedChangeListener((b, isChecked) -> applyHistoryFilters());
        android.widget.AdapterView.OnItemSelectedListener listener = new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> p, View v, int pos, long id) { applyHistoryFilters(); }
            @Override public void onNothingSelected(android.widget.AdapterView<?> p) {}
        };
        spinnerLevelHistory.setOnItemSelectedListener(listener);
        spinnerSuitHistory.setOnItemSelectedListener(listener);
        spinnerResultHistory.setOnItemSelectedListener(listener);
    }*/

    /*private void refreshHistoryList() {
        String json = getSharedPreferences("BridgePrefs", MODE_PRIVATE).getString("gameHistory", "[]");
        fullHistoryList.clear();
        try {
            org.json.JSONArray array = new org.json.JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                fullHistoryList.add(array.getJSONObject(i));
            }
        } catch (Exception e) { e.printStackTrace(); }
        applyHistoryFilters();
    }*/

    /*private void applyHistoryFilters() {
        filteredHistoryList.clear();
        boolean onlySaved = cbOnlySavedHistory.isChecked();
        int levelIdx = spinnerLevelHistory.getSelectedItemPosition();
        int suitIdx = spinnerSuitHistory.getSelectedItemPosition();
        int resultType = spinnerResultHistory.getSelectedItemPosition();

        for (org.json.JSONObject game : fullHistoryList) {
            try {
                if (onlySaved && !game.optBoolean("isSaved", false)) continue;
                String cStr = game.getString("contract");
                String rStr = game.getString("result");
                int tricks = 0;
                String[] resParts = rStr.split(" ");
                if (resParts.length >= 2) tricks = Integer.parseInt(resParts[1]);

                boolean won = true;
                int cLevel = 0;
                if (!cStr.toUpperCase().contains("PASS")) {
                    String[] cParts = cStr.split(" ");
                    if (cParts.length >= 1) {
                        cLevel = Integer.parseInt(cParts[0]);
                        if (tricks < (cLevel + 6)) won = false;
                    }
                }
                if (resultType == 1 && !won) continue;
                if (resultType == 2 && won) continue;
                if (levelIdx > 0 && cLevel != levelIdx) continue;
                if (suitIdx > 0) {
                    boolean match = false;
                    String cUpper = cStr.toUpperCase();
                    switch (suitIdx) {
                        case 1: match = cUpper.contains("NT"); break;
                        case 2: match = cUpper.contains("SPADES"); break;
                        case 3: match = cUpper.contains("HEARTS"); break;
                        case 4: match = cUpper.contains("DIAMONDS"); break;
                        case 5: match = cUpper.contains("CLUBS"); break;
                    }
                    if (!match) continue;
                }
                filteredHistoryList.add(game);
            } catch (Exception e) { e.printStackTrace(); }
        }
        historyOverlayAdapter.notifyDataSetChanged();
        tvEmptyHistory.setVisibility(filteredHistoryList.isEmpty() ? View.VISIBLE : View.GONE);
    }*/

    /*private void toggleHistorySave(int pos) {
        try {
            org.json.JSONObject item = filteredHistoryList.get(pos);
            boolean current = item.optBoolean("isSaved", false);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(current ? R.string.unhighlight_confirm_message : R.string.highlight_confirm_message)
                    .setPositiveButton(R.string.yes, (d, w) -> {
                        try {
                            item.put("isSaved", !current);
                            saveFullHistory();
                            applyHistoryFilters();
                        } catch (Exception e) { e.printStackTrace(); }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } catch (Exception e) { e.printStackTrace(); }
    }*/

    /*private void showHistoryDeleteDialog(int pos) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.yes, (d, w) -> {
                    fullHistoryList.remove(filteredHistoryList.get(pos));
                    saveFullHistory();
                    applyHistoryFilters();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }*/

    /*private void saveFullHistory() {
        org.json.JSONArray array = new org.json.JSONArray();
        for (org.json.JSONObject obj : fullHistoryList) array.put(obj);
        getSharedPreferences("BridgePrefs", MODE_PRIVATE).edit().putString("gameHistory", array.toString()).apply();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameController.cleanup();
    }
}
