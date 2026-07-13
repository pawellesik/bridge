package com.example.bridge.ui.game;

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
import com.example.bridge.ui.history.PbnExporter;
import com.example.bridge.core.SharedPref;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Trick;
import com.example.bridge.ui.biddings.GameBiddingHistory;
import com.example.bridge.ui.biddings.GameBidding;
import com.example.bridge.ui.biddings.GameBiddingHistoryAdapter;
import com.example.bridge.ui.settings.OverlaySettings;

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
    private CardAdapter southAdapter;
    private CardAdapter northAdapter;
    private FrameLayout playedCardContainerSouth;
    private FrameLayout playedCardContainerNorth;
    private FrameLayout playedCardContainerWest;
    private FrameLayout playedCardContainerEast;
    private TextView nameNorth, nameSouth, nameEast, nameWest;
    private Button btn_deal;
    private View startBar;
    private View btnClaim;
    private View loadingIndicator;
    private View historyOverlay;
    private View statisticOverlay;
    private View settingsOverlay;
    private View biddingOverlay;
    private View biddingControlsOverlay;
    private View topBar;
    private RecyclerView rvBiddingHistory;

    GameBiddingHistoryAdapter gameBiddingHistoryAdapter;
    private final List<Card> displayHandSouth = new ArrayList<>();
    private final List<Card> displayHandNorth = new ArrayList<>();
    private boolean isProcessingMove = false;
    private GameTop gameTop;
    private GameController gameController;
    private SharedPref sharedPref;
    private String gameMode;
    private GameBidding gameBidding;
    public PbnExporter pbnExporter, pbnExporterNatC;

    GameBiddingHistory gameBiddingHistory;
    private OverlaySettings overlaySettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }
        getWindow().setNavigationBarColor(android.graphics.Color.parseColor("#0F170D"));

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
        topBar = findViewById(R.id.top_bar_container);
        loadingIndicator = findViewById(R.id.loading_indicator);

        gameTop = new GameTop(this);
        sharedPref = new SharedPref(this);

        historyOverlay = findViewById(R.id.history_overlay);
        statisticOverlay = findViewById(R.id.statistic_overlay);
        settingsOverlay = findViewById(R.id.settings_overlay);
        biddingOverlay = findViewById(R.id.bidding_overlay);
        biddingControlsOverlay = findViewById(R.id.bidding_controls_overlay);

        gameBidding = new GameBidding(this);
        overlaySettings = new OverlaySettings(this);
        pbnExporter = new PbnExporter(this);
        pbnExporterNatC = new PbnExporter(this);

        setupRecyclerView();

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_game);
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_game) {
                    historyOverlay.setVisibility(View.GONE);
                    statisticOverlay.setVisibility(View.GONE);
                    settingsOverlay.setVisibility(View.GONE);
                    bottomNav.setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    historyOverlay.setVisibility(View.VISIBLE);
                    statisticOverlay.setVisibility(View.GONE);
                    settingsOverlay.setVisibility(View.GONE);
                    bottomNav.setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.nav_statistic) {
                    historyOverlay.setVisibility(View.GONE);
                    statisticOverlay.setVisibility(View.VISIBLE);
                    settingsOverlay.setVisibility(View.GONE);
                    bottomNav.setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.nav_settings) {
                    historyOverlay.setVisibility(View.GONE);
                    statisticOverlay.setVisibility(View.GONE);
                    settingsOverlay.setVisibility(View.VISIBLE);
                    bottomNav.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            });
        }

        btn_deal.setOnClickListener(v -> {
            onVisibleStartBar(false);
            loadingIndicator.setVisibility(View.VISIBLE);
            v.post(() -> {
                initGame();
            });
        });

        findViewById(R.id.btn_start).setOnClickListener(v -> {
            onVisibleStartBar(false);
            setBottomNavVisibility(false);
            sharedPref.incrementGamesPlayed();
            sharedPref.clearSavedDeal();

            southAdapter.setCardsEnabled(true);
            northAdapter.setCardsEnabled(true);

            if ("single".equals(gameMode) && biddingControlsOverlay != null) {
                biddingControlsOverlay.setVisibility(View.VISIBLE);
                gameBiddingHistoryAdapter.setHighlightLast(true);

            } else {
                v.post(() -> {
                    gameController.startGame();
                });
            }
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
                if ((historyOverlay != null && historyOverlay.getVisibility() == View.VISIBLE) ||
                        (statisticOverlay != null && statisticOverlay.getVisibility() == View.VISIBLE) ||
                        (settingsOverlay != null && settingsOverlay.getVisibility() == View.VISIBLE)) {

                    historyOverlay.setVisibility(View.GONE);
                    statisticOverlay.setVisibility(View.GONE);
                    settingsOverlay.setVisibility(View.GONE);

                    com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                    if (bottomNav != null) {
                        bottomNav.setVisibility(View.VISIBLE);
                        bottomNav.setSelectedItemId(R.id.nav_game);
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

        gameMode = getIntent().getStringExtra("GAME_MODE");
        initGame();
    }

    public View getSettingsOverlay() {
        return settingsOverlay;
    }

    public View getTopBar(){
        return topBar;
    }

    public String getGameMode(){
        return gameMode;
    }


    private void initGame() {
        if ("quick".equals(gameMode)) {
            initGameQiuckMode();
        } else if ("single".equals(gameMode)) {
            initGameSingleMode();
            initBiddingHistory();

            gameBiddingHistory.setFirstPlayer(gameController.getPlayers().get("East"));//todo
            gameBiddingHistory.addFakeAuction();

            gameBiddingHistory.updateBiddingHistory();
            gameBidding.applyAuctionRules(gameBiddingHistory);
        } else if ("multi".equals(gameMode)) {
            //todo
        }
    }

    private void initGameSingleMode() {
        initGameBase();

        pbnExporter.initNewGame(gameController.getHandsMap(), "W", "None");

        gameTop.hideContract();
        if (topBar != null) topBar.setVisibility(View.GONE);
        biddingOverlay.setVisibility(View.VISIBLE);
        onHandUpdated("South");
        onVisibleStartBar(true);
    }

    private void initGameQiuckMode() {
        initGameBase();
        gameController.calculateAndSetTheBestContract();

        pbnExporter.initNewGame(gameController.getHandsMap(), "W", "None");

        pbnExporterNatC.todoBiding();//todo delete in future

        onHandUpdated("North");
        onHandUpdated("South");
        onVisibleStartBar(true);
    }

    public GameController getGameController() {
        return gameController;
    }

    private void initGameBase() {
        Map<String, Player> players = new LinkedHashMap<>();
        players.put("North", new Player("North", playedCardContainerNorth));
        players.put("East", new Player("East", playedCardContainerEast));
        players.put("South", new Player("South", playedCardContainerSouth));
        players.put("West", new Player("West", playedCardContainerWest));
        gameController = new GameController(this, players, sharedPref);
        gameController.dealCards();
    }


    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> finish())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    public GameTop getGameTop() {
        return gameTop;
    }

    private void setBottomNavVisibility(boolean visible) {
        View bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }


    public RecyclerView getRvBiddingHistory() {
        return rvBiddingHistory;
    }

    public GameBiddingHistoryAdapter getGameBiddingHistoryAdapter() {
        return gameBiddingHistoryAdapter;
    }

    public View getBiddingControlsOverlay() {
        return biddingControlsOverlay;
    }
    public View getBiddingOverlay() {
        return biddingOverlay;
    }
    private void initBiddingHistory() {
        rvBiddingHistory = findViewById(R.id.rv_bidding_history);
        rvBiddingHistory.setLayoutManager(new GridLayoutManager(this, 4));
        gameBiddingHistory = new GameBiddingHistory(this);
        gameBiddingHistoryAdapter = new GameBiddingHistoryAdapter(gameBiddingHistory.getAuction());
        rvBiddingHistory.setAdapter(gameBiddingHistoryAdapter);
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
                v.setPadding(v.getPaddingLeft(), systemBars.top + (int) (4 * getResources().getDisplayMetrics().density), v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }

        View statusBarSpacer = findViewById(R.id.system_status_bar_spacer);
        if (statusBarSpacer != null) {
            ViewCompat.setOnApplyWindowInsetsListener(statusBarSpacer, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.getLayoutParams().height = systemBars.top;
                v.requestLayout();
                return insets;
            });
        }

        View filterBar = findViewById(R.id.filters_container_overlay);
        if (filterBar != null) {
            ViewCompat.setOnApplyWindowInsetsListener(filterBar, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top + (int) (12 * getResources().getDisplayMetrics().density), v.getPaddingRight(), (int) (12 * getResources().getDisplayMetrics().density));
                return insets;
            });
        }

        View statisticHeader = findViewById(R.id.statistic_header_container);
        if (statisticHeader != null) {
            ViewCompat.setOnApplyWindowInsetsListener(statisticHeader, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top + (int) (8 * getResources().getDisplayMetrics().density), v.getPaddingRight(), (int) (8 * getResources().getDisplayMetrics().density));
                return insets;
            });
        }

        View settingsHeader = findViewById(R.id.settings_header_container);
        if (settingsHeader != null) {
            ViewCompat.setOnApplyWindowInsetsListener(settingsHeader, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top + (int) (8 * getResources().getDisplayMetrics().density), v.getPaddingRight(), (int) (8 * getResources().getDisplayMetrics().density));
                return insets;
            });
        }

        View bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(0, 0, 0, systemBars.bottom);
                return insets;
            });
        }
    }

    private void setupRecyclerView() {
        RecyclerView rvSouth = findViewById(R.id.rv_hand_south);
        RecyclerView rvNorth = findViewById(R.id.rv_hand_north);

        rvSouth.setLayoutManager(createLayoutManager(displayHandSouth));
        southAdapter = new CardAdapter(displayHandSouth);
        southAdapter.setOnCardClickListener(card -> {
            if (isProcessingMove) return;
            Player south = gameController.getPlayers().get("South");
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
            Player north = gameController.getPlayers().get("North");
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

    public void refreshAllColors() {
        updateDisplayHandNorth();
        updateDisplayHandSouth();

        if (gameBidding != null) {
            gameBidding.applyColors();
        }

        if (gameBiddingHistoryAdapter != null) {
            gameBiddingHistoryAdapter.notifyDataSetChanged();
        }

        if (gameController != null && gameTop != null) {
            gameTop.setContract(gameController.getCurrentContract());
        }
    }

    private void updateDisplayHandSouth() {
        if (gameController.getPlayers().get("South") == null) return;
        List<Card> actualHand = gameController.getPlayers().get("South").getHand();
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
        if (gameController.getPlayers().get("North") == null) return;
        List<Card> actualHand = gameController.getPlayers().get("North").getHand();
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

    public void updateTurn(String playerName) {
        nameNorth.setBackgroundResource(0);
        nameSouth.setBackgroundResource(0);
        nameEast.setBackgroundResource(0);
        nameWest.setBackgroundResource(0);

        if (playerName == null) return;

        switch (playerName) {
            case "North":
                nameNorth.setBackgroundResource(R.drawable.transparent_white_frame);
                break;
            case "South":
                nameSouth.setBackgroundResource(R.drawable.transparent_white_frame);
                break;
            case "East":
                nameEast.setBackgroundResource(R.drawable.transparent_white_frame);
                break;
            case "West":
                nameWest.setBackgroundResource(R.drawable.transparent_white_frame);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameController.cleanup();
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
    public void onCardPlayed(Player player, Card card) {
        showPlayedCard(card, player.getPlayedCardContainer());
    }

    @Override
    public void onUpdateLastTrickInTop(Map<String, Card> trickCards) {
        FrameLayout[] containers = {playedCardContainerSouth, playedCardContainerNorth, playedCardContainerWest, playedCardContainerEast};
        for (FrameLayout container : containers) {
            if (container != null) container.removeAllViews();
        }
        gameTop.setLastTrickInTop(trickCards);
    }

    @Override
    public void onClearLastCards(List<Card> cardsOnTable) {
        if (cardsOnTable != null && cardsOnTable.size() > 1) {
            gameTop.clearLastCards();
        }
    }

    @Override
    public void onVisibleStartBar(Boolean isVisible) {
        if (isVisible) {
            setBottomNavVisibility(true);
            startBar.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.GONE);
        } else {
            startBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTurnChanged(String playerName) {
        if (loadingIndicator != null) loadingIndicator.setVisibility(View.GONE);
        updateTurn(playerName);
        if ("North".equals(playerName) || "South".equals(playerName)) {
            isProcessingMove = false;
        }
    }

    @Override
    public void onContractDetermined(Contract contract, Player declarer) {
        isProcessingMove = false;
        gameTop.setContract(contract);

        if (pbnExporter != null) {
            pbnExporter.setContract(contract, declarer != null ? declarer.getName() : "South");
        }

        // Architectural safety: UI updates itself in response to the state change
        if (biddingControlsOverlay != null) {
            biddingControlsOverlay.setVisibility(View.GONE);
        }
        if (biddingOverlay != null) {
            biddingOverlay.setVisibility(View.GONE);
        }
        if (topBar != null) {
            topBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClaimButtonVisibilityChanged(boolean visible) {
        if (btnClaim != null) {
            btnClaim.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onScoreUpdated(int snScore, int weScore) {
        gameTop.updateScores(snScore, weScore);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGameEnded(int snScore, int weScore, Contract contract, List<Trick> history, int claim) {
        if (pbnExporter != null) {
            String decl = pbnExporter.getDeclarer();
            if ("West".equals(decl) || "East".equals(decl)) {
                pbnExporter.setResult(weScore);
            } else {
                pbnExporter.setResult(snScore);
            }
            pbnExporter.setPlayHistory(history);
            android.util.Log.d("PBN_EXPORT", pbnExporter.generatePbn());
        }

        if ("quick".equals(gameMode)) {
            onVisibleStartBar(true);
            setBottomNavVisibility(true);
            initGameQiuckMode();
            //todo show result
        } else if ("single".equals(gameMode)) {
            onVisibleStartBar(true);
            setBottomNavVisibility(true);
            initGameSingleMode();
            initBiddingHistory();

            gameBiddingHistory.setFirstPlayer(gameController.getPlayers().get("East"));//todo
            gameBiddingHistory.addFakeAuction();

            gameBiddingHistory.updateBiddingHistory();
            gameBidding.applyAuctionRules(gameBiddingHistory);
        }
    }
}
