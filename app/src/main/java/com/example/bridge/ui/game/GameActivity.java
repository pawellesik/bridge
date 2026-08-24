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
import com.example.bridge.core.DataStore;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Trick;
import com.example.bridge.ui.biddings.GameBiddingHistory;
import com.example.bridge.ui.biddings.GameBidding;
import com.example.bridge.ui.biddings.GameBiddingHistoryAdapter;
import com.example.bridge.ui.biddings.SingleGameBidding;
import com.example.bridge.ui.history.OverlayHistoryGame;
import com.example.bridge.ui.history.OverlayHistoryList;
import com.example.bridge.ui.history.PbnCollection;
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
    private GamePlayerLabels playerLabels;
    private Button btn_deal;
    private View startBar;
    private View btnClaim;
    private View loadingIndicator;
    private View statisticOverlay;
    private View settingsOverlay;
    private View biddingOverlay;
    private View biddingControlsOverlay;
    private View topBar;
    private RecyclerView rvBiddingHistory;

    private View historyOverlay;
    private OverlayHistoryList overlayHistoryList;
    private OverlayHistoryGame overlayHistoryGame;
    private OverlaySettings overlaySettings;
    GameBiddingHistoryAdapter gameBiddingHistoryAdapter;
    private final List<Card> displayHandSouth = new ArrayList<>();
    private final List<Card> displayHandNorth = new ArrayList<>();
    private boolean isProcessingMove = false;
    private GameTop gameTop;
    private GameController gameController;
    private DataStore dataStore;
    private String gameMode;
    private GameBidding gameBidding;
    private PbnCollection pbnCollection;
    private SingleGameBidding singleGameBidding;

    GameBiddingHistory gameBiddingHistory;


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

        playerLabels = new GamePlayerLabels(this);

        startBar = findViewById(R.id.start_bar);
        btn_deal = findViewById(R.id.btn_deal);
        btnClaim = findViewById(R.id.btn_claim);
        topBar = findViewById(R.id.top_bar_container);
        loadingIndicator = findViewById(R.id.loading_indicator);

        gameTop = new GameTop(this);
        dataStore = new DataStore(this);

        historyOverlay = findViewById(R.id.history_overlay);
        statisticOverlay = findViewById(R.id.statistic_overlay);
        settingsOverlay = findViewById(R.id.settings_overlay);
        biddingOverlay = findViewById(R.id.bidding_overlay);
        biddingControlsOverlay = findViewById(R.id.bidding_controls_overlay);

        View btnCloseBidding = findViewById(R.id.btn_close_bidding_overlay);
        if (btnCloseBidding != null) {
            btnCloseBidding.setOnClickListener(v -> hideBiddingOverlay());
        }

        gameBidding = new GameBidding(this);
        singleGameBidding = new SingleGameBidding(this);
        overlaySettings = new OverlaySettings(this);
        pbnCollection = new PbnCollection(this);
        overlayHistoryList = new OverlayHistoryList(this);
        overlayHistoryGame = new OverlayHistoryGame(this);

        setupRecyclerView();

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_game);
            bottomNav.setOnItemSelectedListener(item -> {
                if (overlayHistoryGame != null) overlayHistoryGame.hide();
                int itemId = item.getItemId();
                if (itemId == R.id.nav_game) {
                    statisticOverlay.setVisibility(View.GONE);
                    settingsOverlay.setVisibility(View.GONE);
                    historyOverlay.setVisibility(View.GONE);
                    bottomNav.setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    historyOverlay.setVisibility(View.VISIBLE);
                    overlayHistoryList.refresh();
                    statisticOverlay.setVisibility(View.GONE);
                    settingsOverlay.setVisibility(View.GONE);
                    bottomNav.setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.nav_statistic) {
                    statisticOverlay.setVisibility(View.VISIBLE);
                    settingsOverlay.setVisibility(View.GONE);
                    historyOverlay.setVisibility(View.GONE);
                    bottomNav.setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.nav_settings) {
                    statisticOverlay.setVisibility(View.GONE);
                    settingsOverlay.setVisibility(View.VISIBLE);
                    historyOverlay.setVisibility(View.GONE);
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
            dataStore.incrementGamesPlayed();

            southAdapter.setCardsEnabled(true);
            northAdapter.setCardsEnabled(true);

            if ("single".equals(gameMode) && biddingControlsOverlay != null) {
                biddingControlsOverlay.setVisibility(View.VISIBLE);
                gameBiddingHistoryAdapter.setHighlightLast(true);
                singleGameBidding.start();

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
                if (overlayHistoryGame != null && overlayHistoryGame.isVisible()) {
                    overlayHistoryGame.hide();
                    return;
                }
                if ((statisticOverlay != null && statisticOverlay.getVisibility() == View.VISIBLE) ||
                        (settingsOverlay != null && settingsOverlay.getVisibility() == View.VISIBLE) ||
                        (historyOverlay != null && historyOverlay.getVisibility() == View.VISIBLE)) {

                    statisticOverlay.setVisibility(View.GONE);
                    settingsOverlay.setVisibility(View.GONE);
                    historyOverlay.setVisibility(View.GONE);

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

    public View getTopBar() {
        return topBar;
    }

    public String getGameMode() {
        return gameMode;
    }

    private void initGame() {
        if ("quick".equals(gameMode)) {
            initGameQiuckMode();
        } else if ("single".equals(gameMode)) {
            initGameSingleMode();
        } else if ("multi".equals(gameMode)) {
            //todo
        }
    }

    private void initGameQiuckMode() {
        initGameBase();

        pbnCollection.initAllPbn();
        pbnCollection.initQiuckPbn();

        onHandUpdated("North");
        onHandUpdated("South");
        playerLabels.updateAll(gameController.getPlayers(), gameMode);
        onVisibleStartBar(true);
    }

    private void initGameSingleMode() {
        initGameBase();
        initBiddingUi();
        pbnCollection.initAllPbn();

        gameTop.hideContract();
        if (topBar != null) topBar.setVisibility(View.GONE);
        biddingOverlay.setVisibility(View.VISIBLE);
        onHandUpdated("South");
        playerLabels.updateAll(gameController.getPlayers(), gameMode);
        onVisibleStartBar(true);
    }

    public GameBidding getGameBidding() {
        return gameBidding;
    }

    public SingleGameBidding getSingleBidding() {
        return singleGameBidding;
    }

    public GameBiddingHistory getGameBiddingHistory() {
        return gameBiddingHistory;
    }

    public OverlayHistoryGame getOverlayHistoryGame() {
        return overlayHistoryGame;
    }

    public GameController getGameController() {
        return gameController;
    }

    public PbnCollection getPbnCollection() {
        return this.pbnCollection;
    }

    private void initGameBase() {
        Map<String, Player> players = new LinkedHashMap<>();
        players.put("North", new Player("North", playedCardContainerNorth));
        players.put("East", new Player("East", playedCardContainerEast));
        players.put("South", new Player("South", playedCardContainerSouth));
        players.put("West", new Player("West", playedCardContainerWest));
        gameController = new GameController(this, players, dataStore);
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

    public void showBiddingReview() {
        if (biddingOverlay == null || "quick".equals(gameMode)) return;
        
        biddingOverlay.setVisibility(View.VISIBLE);
        
        // Hide entire top bar info layouts during review to avoid overlapping
        View leftInfoLayout = findViewById(R.id.linearLayout);
        if (leftInfoLayout != null) leftInfoLayout.setVisibility(View.GONE);
        
        View lastCardsLayout = findViewById(R.id.last_cards);
        if (lastCardsLayout != null) lastCardsLayout.setVisibility(View.GONE);
        
        // Hide system selection during review
        View systemSelection = findViewById(R.id.system_selection_container);
        if (systemSelection != null) systemSelection.setVisibility(View.GONE);
        
        // Show close button
        View btnClose = findViewById(R.id.btn_close_bidding_overlay);
        if (btnClose != null) btnClose.setVisibility(View.VISIBLE);
        
        // Update public knowledge (it will show summaries based on the full auction)
        if (singleGameBidding != null) {
            singleGameBidding.updatePublicKnowledgeView();
        }

        if (gameBiddingHistoryAdapter != null) {
            gameBiddingHistoryAdapter.setShowPreviewTile(false);
        }

        // Reduce bottom spacer for review mode
        View spacer = findViewById(R.id.bidding_bottom_spacer);
        if (spacer != null) {
            spacer.getLayoutParams().height = 0;
            spacer.requestLayout();
        }

        // Ensure scroll to bottom
        gameBiddingHistory.updateBiddingHistory(null, true);
    }

    public void hideBiddingOverlay() {
        if (biddingOverlay != null) biddingOverlay.setVisibility(View.GONE);

        // Show entire top bar info layouts again when closing review
        View leftInfoLayout = findViewById(R.id.linearLayout);
        if (leftInfoLayout != null) leftInfoLayout.setVisibility(View.VISIBLE);

        View lastCardsLayout = findViewById(R.id.last_cards);
        if (lastCardsLayout != null) lastCardsLayout.setVisibility(View.VISIBLE);
    }

    private void initBiddingUi() {
        rvBiddingHistory = findViewById(R.id.rv_bidding_history);
        rvBiddingHistory.setLayoutManager(new GridLayoutManager(this, 4));
        gameBiddingHistory = new GameBiddingHistory(this);
        gameBiddingHistoryAdapter = new GameBiddingHistoryAdapter(gameBiddingHistory.getAuction());
        gameBiddingHistoryAdapter.setShowPreviewTile(false); // Ukrywamy pusty kwadrat przed startem
        rvBiddingHistory.setAdapter(gameBiddingHistoryAdapter);

        // Reset elements that might have been changed by showBiddingReview
        View btnClose = findViewById(R.id.btn_close_bidding_overlay);
        if (btnClose != null) btnClose.setVisibility(View.GONE);

        View pkContainer = findViewById(R.id.public_knowledge_container_layout);
        if (pkContainer != null) pkContainer.setVisibility(View.GONE);

        // Reset bottom spacer for active bidding mode
        View spacer = findViewById(R.id.bidding_bottom_spacer);
        if (spacer != null) {
            spacer.getLayoutParams().height = (int) (44 * getResources().getDisplayMetrics().density);
            spacer.requestLayout();
        }

        // Uwidocznienie wyboru systemu przy inicjalizacji UI licytacji
        View selectionContainer = findViewById(R.id.system_selection_container);
        if (selectionContainer != null) {
            selectionContainer.setVisibility(View.VISIBLE);
        }
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

        View historyGameHeader = findViewById(R.id.history_game_header_container);
        if (historyGameHeader != null) {
            ViewCompat.setOnApplyWindowInsetsListener(historyGameHeader, (v, insets) -> {
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

        if (singleGameBidding != null) {
            singleGameBidding.updatePublicKnowledgeView();
        }

        if (gameController != null && gameTop != null) {
            gameTop.setContract(gameController.getCurrentContract());
        }

        if (overlayHistoryGame != null && overlayHistoryGame.isVisible()) {
            overlayHistoryGame.updateUi();
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
        playerLabels.updateTurn(playerName);
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
        playerLabels.updateLabel(playerName, gameController.getPlayers().get(playerName), gameMode);
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

        if (pbnCollection.getPbn() != null) {
            pbnCollection.getPbn().setContract(contract, declarer != null ? declarer.getName() : "South");
        }

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
    public void onPlayersSwapped(boolean ns, boolean ew) {
        if (playerLabels != null) {
            playerLabels.resetViews();
            if (ns) playerLabels.swapNS();
            if (ew) playerLabels.swapEW();
            playerLabels.updateAll(gameController.getPlayers(), gameMode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGameEnded(int snScore, int weScore, Contract contract, List<Trick> history, int claim) {
        // 1. Natychmiastowe pokazanie ładowania na UI thread i zamknięcie ewentualnej licytacji
        hideBiddingOverlay();

        if (loadingIndicator != null) {
            loadingIndicator.bringToFront();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        if (pbnCollection != null) {
            String decl = pbnCollection.getPbn().getDeclarer();
            if ("West".equals(decl) || "East".equals(decl)) {
                pbnCollection.getPbn().setResult(weScore);
            } else {
                pbnCollection.getPbn().setResult(snScore);
            }
            pbnCollection.getPbn().setPlayHistory(history);
            pbnCollection.getPbn().calculateAndSetScore();
            pbnCollection.calculateAllImps();

            String jsonExport = pbnCollection.generateJsonExport();
            
            // 2. Zapis do bazy i przejście do historii
            overlayHistoryList.saveGameToHistory(this, jsonExport, firstId -> {
                if (firstId != -1 && overlayHistoryGame != null) {
                    com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                    if (bottomNav != null) {
                        bottomNav.getMenu().findItem(R.id.nav_history).setChecked(true);
                    }

                    overlayHistoryGame.showGame(firstId, () -> {
                        // Ten callback wywoła się dopiero gdy historia jest widoczna i załadowana
                        if (loadingIndicator != null) loadingIndicator.setVisibility(View.GONE);

                        // 3. Dopiero teraz (gdy historia zasłania stół) przygotowujemy nową grę
                        if ("quick".equals(gameMode)) {
                            onVisibleStartBar(true);
                            setBottomNavVisibility(true);
                            initGameQiuckMode();
                        } else if ("single".equals(gameMode)) {
                            onVisibleStartBar(true);
                            setBottomNavVisibility(true);
                            initGameSingleMode();
                        }
                    });
                }
            });
        }
    }
}
