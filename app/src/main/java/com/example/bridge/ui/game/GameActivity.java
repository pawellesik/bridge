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
import com.example.bridge.core.PbnExporter;
import com.example.bridge.core.SharedPref;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;
import com.example.bridge.model.Trick;
import com.example.bridge.ui.settings.SettingsActivity;

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
    private View biddingOverlay;
    private View biddingControlsOverlay;
    private final List<Card> displayHandSouth = new ArrayList<>();
    private final List<String> biddingBids = new ArrayList<>();
    private BiddingHistoryAdapter biddingHistoryAdapter;
    private final List<Card> displayHandNorth = new ArrayList<>();
    private boolean isProcessingMove = false;
    private GameActivityTop gameActivityTop;
    private GameController gameController;
    private SharedPref sharedPref;
    private String gameMode;
    private GameBidding gameBidding;

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
        loadingIndicator = findViewById(R.id.loading_indicator);

        gameActivityTop = new GameActivityTop(this);
        sharedPref = new SharedPref(this, gameActivityTop);

        historyOverlay = findViewById(R.id.history_overlay);
        biddingOverlay = findViewById(R.id.bidding_overlay);
        biddingControlsOverlay = findViewById(R.id.bidding_controls_overlay);
        gameBidding = new GameBidding(this, biddingControlsOverlay);

        setupRecyclerView();
        setupBiddingHistory();

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
                    return true;
                } else if (itemId == R.id.nav_settings) {
                    startActivity(new Intent(this, SettingsActivity.class));
                    return false;
                }
                return false;
            });
        }

        btn_deal.setOnClickListener(v -> {
            onVisibleStartBar(false);
            loadingIndicator.setVisibility(View.VISIBLE);
            v.post(() -> {
                initGame();
                //gameController.dealCards();
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
                gameBidding.selectLevel(1);
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
                if (historyOverlay != null && historyOverlay.getVisibility() == View.VISIBLE) {
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

    private void initGame(){
        if ("quick".equals(gameMode)) {
            initGameQiuckMode();
        } else if ("single".equals(gameMode)) {
            initGameSingleMode();
        } else if ("multi".equals(gameMode)) {
            //todo
        }
    }
    private void initGameSingleMode() {
        initGameBase();
        gameActivityTop.hideContract();
        View topBar = findViewById(R.id.top_bar_container);
        if (topBar != null) topBar.setVisibility(View.GONE);
        biddingOverlay.setVisibility(View.VISIBLE);
        onHandUpdated("South");
        onVisibleStartBar(true);
    }
    private void initGameQiuckMode() {
        initGameBase();
        gameController.calculateAndSetTheBestContract();
        onHandUpdated("North");
        onHandUpdated("South");
        onVisibleStartBar(true);
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
        gameActivityTop.updateScores(snScore, weScore);
    }

    @Override
    public void onGameEnded(int snScore, int weScore, Contract contract, List<Trick> history, int claim) {
        gameActivityTop.updateScores(snScore, weScore);

        if (southAdapter != null) southAdapter.setCardsEnabled(false);
        if (northAdapter != null) northAdapter.setCardsEnabled(false);

        View btnSave = findViewById(R.id.btn_save_game);
        if (btnSave != null) btnSave.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setBottomNavVisibility(boolean visible) {
        View bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(visible ? View.VISIBLE : View.GONE);
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
    public void onContractDetermined(Contract contract) {
        isProcessingMove = false;
        gameActivityTop.setContract(contract);
    }

    private void setupBiddingHistory() {
        RecyclerView rvBidding = findViewById(R.id.rv_bidding_history);
        if (rvBidding != null) {
            rvBidding.setLayoutManager(new GridLayoutManager(this, 4));
            biddingHistoryAdapter = new BiddingHistoryAdapter(biddingBids);
            rvBidding.setAdapter(biddingHistoryAdapter);
        }

        // Use PbnExporter to generate fake data
        PbnExporter fakeExporter = new PbnExporter();
        fakeExporter.addFakeAuction();
        
        biddingBids.clear();
        // Assume West starts as in current logic
        biddingBids.addAll(fakeExporter.getAuction());

        // Pad with dashes to fill the grid (at least 32 items / 8 rows for scrolling test)
        int minItems = 32;
        while (biddingBids.size() < minItems || biddingBids.size() % 4 != 0) {
            biddingBids.add("-");
        }

        if (biddingHistoryAdapter != null) {
            biddingHistoryAdapter.notifyDataSetChanged();
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
        gameActivityTop.setLastTrickInTop(trickCards);
    }

    @Override
    public void onClearLastCards(List<Card> cardsOnTable) {
        if (cardsOnTable != null && cardsOnTable.size() > 1) {
            gameActivityTop.clearLastCards();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameController.cleanup();
    }
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

/*public void showPlayedCardInSim(Card card, String playerName) {
        Player p = players.get(playerName);
        if (p != null) {
            showPlayedCard(card, p.getPlayedCardContainer());
        }
    }*/

  /*private List<Trick> parseTricks(org.json.JSONArray tricksArray) throws org.json.JSONException {
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
    }*/

 /*private void loadGameFromHistory(String json, boolean isInitialUiLoad) {
        try {
            org.json.JSONObject game = new org.json.JSONObject(json);
            Contract contract = Contract.fromString(game.getString("contract"));
            this.snScoreFromHistory = game.optInt("snScore", 0);
            this.autoSnScoreFromHistory = game.optInt("autoSnScore", 0);
            int claim = game.optInt("claim", 0);

            org.json.JSONObject handsJson = game.getJSONObject("hands");
            gameController.getInitialPlayerHands().clear();
            for (String direction : new String[]{"North", "East", "South", "West"}) {
                org.json.JSONArray handArray = handsJson.getJSONArray(direction);
                List<Card> cards = new ArrayList<>();
                for (int i = 0; i < handArray.length(); i++) {
                    String[] parts = handArray.getString(i).split(":");
                    cards.add(new Card(Suit.valueOf(parts[0]), Rank.valueOf(parts[1])));
                }
                gameController.getInitialPlayerHands().put(direction, cards);
            }

            List<Trick> history = parseTricks(game.optJSONArray("playHistory"));
            this.autoPlayHistoryFromHistory = parseTricks(game.optJSONArray("autoPlayHistory"));

            // Restore without bidding to prevent freeze
            gameController.restoreCardsWithContract(new LinkedHashMap<>(gameController.getInitialPlayerHands()), contract);

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
    }*/