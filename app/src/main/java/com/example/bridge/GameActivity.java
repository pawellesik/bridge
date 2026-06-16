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
import com.example.bridge.model.Trick;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final Map<String, List<Card>> initialPlayerHands = new LinkedHashMap<>();
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
    private View btnAutoReplay;

    // Simulation Views
    private Button btnFirstSim, btnPrevSim, btnNextSim, btnLastSim;
    private TextView tvSimInfo;
    private int currentSimTrickIndex;

    private List<Trick> playHistoryTrick = new ArrayList<>();
    private int simClaimCount = 0;

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

        // Simulation Init
        btnFirstSim = findViewById(R.id.btn_first_trick);
        btnPrevSim = findViewById(R.id.btn_prev_trick);
        btnNextSim = findViewById(R.id.btn_next_trick);
        btnLastSim = findViewById(R.id.btn_last_trick);
        tvSimInfo = findViewById(R.id.tv_trick_info);

        btnFirstSim.setOnClickListener(v -> jumpSimTrick(-1));
        btnPrevSim.setOnClickListener(v -> changeSimTrick(-1));
        btnNextSim.setOnClickListener(v -> changeSimTrick(1));
        btnLastSim.setOnClickListener(v -> jumpSimTrick(1));

        btnAutoReplay = findViewById(R.id.btn_auto_replay);
        btnNewDeal.setOnClickListener(v -> {
            resultsOverlay.setVisibility(View.GONE);
            dealNewCards();
        });

        btnAutoReplay.setOnClickListener(v -> {
            resultsOverlay.setVisibility(View.GONE);
            autoReplay();
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


    public void autoReplay() {
        //int currentSimTrickIndex
        //List<Trick> playHistoryTrick
        //displayHistory(List<Trick> history, int claim);

        updateSimTrickUI(true);
    }


    private void changeSimTrick(int i) {
        if (i < 0 && currentSimTrickIndex > 0) {
            currentSimTrickIndex -= 1;
        } else if (i > 0 && currentSimTrickIndex < this.playHistoryTrick.size()) {
            currentSimTrickIndex += 1;
        }
        updateSimTrickUI(true);
    }

    private void jumpSimTrick(int direction) {
        if (direction < 0) {
            currentSimTrickIndex = 0;
        } else {
            currentSimTrickIndex = this.playHistoryTrick.size();
        }
        updateSimTrickUI(true);
    }

    private void updateSimTrickUI(boolean shouldScroll) {
        tvSimInfo.setText(String.valueOf(currentSimTrickIndex));

        List<Card> previousTricksCards = new ArrayList<>();
        List<Card> currentTrickCards = null;
        Map<String, Card> currentTrickMap = null;

        int simSnScore = 0;
        int simWeScore = 0;

        for (int trickIdx = 0; trickIdx < currentSimTrickIndex; trickIdx++) {
            Trick trick = this.playHistoryTrick.get(trickIdx);
            
            // Punktacja - uwzględniamy wszystkie lewy do obecnego indeksu włącznie
            String winner = trick.getWinnerTrick();
            if ("North".equals(winner) || "South".equals(winner)) {
                simSnScore++;
            } else if ("East".equals(winner) || "West".equals(winner)) {
                simWeScore++;
            }

            if (trickIdx < currentSimTrickIndex - 1) {
                // Karty z poprzednich lew (do wyszarzenia)
                previousTricksCards.addAll(trick.getCardsOnTable());
            } else {
                // Karty z obecnej lewy (do pokazania na stole i na czerwono)
                currentTrickMap = trick.getCardsOnTableMap();
                currentTrickCards = trick.getCardsOnTable();
            }
        }

        // Dodaj claim (jeśli jesteśmy na końcu historii)
        if (currentSimTrickIndex == this.playHistoryTrick.size() && simClaimCount > 0) {
            simSnScore += simClaimCount;
        }

        // Aktualizacja wyników w symulacji
        if (tvScoreSN != null) tvScoreSN.setText(getString(R.string.sn_label, simSnScore));
        if (tvScoreWE != null) tvScoreWE.setText(getString(R.string.we_label, simWeScore));

        // Wyczyść stół i wskaźniki "ostatnich kart"
        onTableCleared(currentTrickMap);

        // Pokaż karty obecnej lewy na środku stołu
        if (currentTrickMap != null) {
            for (Map.Entry<String, Card> entry : currentTrickMap.entrySet()) {
                Player p = players.get(entry.getKey());
                if (p != null) {
                    showPlayedCard(entry.getValue(), p.getPlayedCardContainer());
                }
            }
        }

        // Odśwież widok rąk
        displayHand(tvNorthRes, formatHandToHtmlForSim(initialPlayerHands.get("North"), previousTricksCards, currentTrickCards));
        displayHand(tvSouthRes, formatHandToHtmlForSim(initialPlayerHands.get("South"), previousTricksCards, currentTrickCards));
        displayHand(tvEastRes, formatHandToHtmlForSim(initialPlayerHands.get("East"), previousTricksCards, currentTrickCards));
        displayHand(tvWestRes, formatHandToHtmlForSim(initialPlayerHands.get("West"), previousTricksCards, currentTrickCards));

        // Aktualizacja podświetlenia w historii i przewijanie
        updateHistoryHighlightAndScroll(shouldScroll);
    }

    private void updateHistoryHighlightAndScroll(boolean shouldScroll) {
        if (tableHistoryRes == null) return;

        int rowCount = tableHistoryRes.getChildCount();
        for (int i = 1; i < rowCount; i++) {
            View row = tableHistoryRes.getChildAt(i);
            if (i <= currentSimTrickIndex) {
                row.setBackgroundColor(Color.parseColor("#A5D6A7"));
            } else {
                row.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        // Przewijanie do aktualnego wiersza tylko gdy shouldScroll jest true
        if (shouldScroll && currentSimTrickIndex >= 0 && currentSimTrickIndex < rowCount) {
            View targetRow = tableHistoryRes.getChildAt(currentSimTrickIndex);
            if (targetRow != null) {
                findViewById(R.id.scroll_history).post(() -> {
                    androidx.core.widget.NestedScrollView scrollView = findViewById(R.id.scroll_history);
                    scrollView.smoothScrollTo(0, targetRow.getTop());
                });
            }
        }
    }

    private String formatHandToHtmlForSim(List<Card> hand, List<Card> previousTricksCards, List<Card> currentTrickCards) {
        StringBuilder sb = new StringBuilder();
        com.example.bridge.model.Suit[] suits = {
                com.example.bridge.model.Suit.SPADES,
                com.example.bridge.model.Suit.HEARTS,
                com.example.bridge.model.Suit.DIAMONDS,
                com.example.bridge.model.Suit.CLUBS
        };

        for (int i = 0; i < suits.length; i++) {
            com.example.bridge.model.Suit suit = suits[i];
            String color = suit.isRed ? "red" : "black";
            sb.append("<b><font color='").append(color).append("'>")
                    .append(suit.symbol).append("</font></b>&nbsp;");

            sb.append("<b>");
            boolean first = true;
            for (Card card : hand) {
                if (card.getSuit() == suit) {
                    if (!first) sb.append("&nbsp;");

                    String cardColor = "black"; // Nie rzucone
                    if (previousTricksCards != null && previousTricksCards.contains(card)) {
                        cardColor = "#999999"; // Rzucone w poprzednich lewach (szare)
                    } else if (currentTrickCards != null && currentTrickCards.contains(card)) {
                        cardColor = "red"; // Obecnie rzucona (czerwona)
                    }

                    sb.append("<font color='").append(cardColor).append("'>")
                            .append(card.getRank().display).append("</font>");
                    first = false;
                }
            }
            sb.append("</b>");
            if (i < suits.length - 1) {
                sb.append("<br/>");
            }
        }
        return sb.toString();
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
        if (changeScore > 0) {
            tvMiddle3.setText("(+" + changeScore + ")");
            tvMiddle3.setTextColor(Color.parseColor("#C8E6C9"));
        } else {
            tvMiddle3.setText("(" + changeScore + ")");
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
    public void onGameEnded(int snScore, int weScore, String contract, List<Trick> history, int claim) {
        this.playHistoryTrick = history;
        this.simClaimCount = claim;
        this.currentSimTrickIndex = history.size();
        tvSimInfo.setText(String.valueOf(currentSimTrickIndex));
        setScore(contract, snScore);
        findViewById(R.id.main).postDelayed(() -> {
            displayResults(history, claim);
        }, 500);
    }

    private void setScore(String contract, int snScore) {
        int level = 0;
        try {
            level = Integer.parseInt(contract.split(" ")[0].trim());
        } catch (Exception e) {
        }

        int requiredTricks = level + 6;
        int handScore = 0;
        if (level > 0) {
            if (snScore >= requiredTricks) {
                handScore = level + (snScore - requiredTricks);
            } else {
                handScore = -level - (requiredTricks - snScore);
            }
        }
        setPrefChangeTotalScore(handScore);
        setTotalScore(getPrefTotalScore(), handScore);
    }

    private void displayResults(List<Trick> history, int claim) {
        displayHistory(history, claim);
        updateSimTrickUI(true);
        resultsOverlay.setVisibility(View.VISIBLE);
    }

    private void displayHand(TextView tv, String handHtml) {
        if (tv != null && handHtml != null) {
            Spanned formatted = Html.fromHtml(handHtml, Html.FROM_HTML_MODE_LEGACY);
            tv.setText(formatted);
        }
    }

    private void displayHistory(List<Trick> history, int claim) {
        if (tableHistoryRes == null) return;

        // Nagłówek (index 0)
        View headerRow = tableHistoryRes.getChildAt(0);
        if (headerRow != null) {
            headerRow.setOnClickListener(v -> {
                currentSimTrickIndex = 0;
                updateSimTrickUI(false);
            });
        }

        int childCount = tableHistoryRes.getChildCount();
        if (childCount > 1) {
            tableHistoryRes.removeViews(1, childCount - 1);
        }

        if (history == null) return;

        for (int i = 0; i < history.size(); i++) {
            final int trickNum = i + 1;
            Trick trick = history.get(i);
            TableRow row = new TableRow(this);
            row.setOnClickListener(v -> {
                currentSimTrickIndex = trickNum;
                updateSimTrickUI(false);
            });

            String[] trickData = new String[4]; // W, N, E, S (matches XML)
            int winnerCol = getPlayerColumn(trick.getWinnerTrick());

            for (Map.Entry<String, Card> entry : trick.getCardsOnTableMap().entrySet()) {
                String name = entry.getKey();
                Card card = entry.getValue();
                int col = getPlayerColumn(name);
                if (col != -1) {
                    trickData[col] = card.getRank().display + " " + card.getSuit().symbol;
                }
            }
            for (int c = 0; c < 4; c++) {
                TextView tv = new TextView(this);
                tv.setText(trickData[c] != null ? trickData[c] : "-");
                tv.setTextColor(Color.BLACK);
                tv.setTypeface(null, android.graphics.Typeface.BOLD);
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(8, 16, 8, 16);
                if (c == winnerCol) {
                    tv.setBackgroundResource(R.drawable.white_frame_in_bright_green);
                }
                row.addView(tv);
            }
            tableHistoryRes.addView(row);
        }
        if (claim > 0) {
            TextView claimTv = new TextView(this);
            claimTv.setText(getString(R.string.claimed_tricks, claim));
            claimTv.setTextColor(Color.RED);
            claimTv.setPadding(16, 8, 16, 8);
            claimTv.setOnClickListener(v -> {
                currentSimTrickIndex = history.size();
                updateSimTrickUI(false);
            });
            tableHistoryRes.addView(claimTv);
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
        simClaimCount = 0;
        gameController.dealCards();
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
        initialPlayerHands.clear();
        for (Player player : players.values()) {
            initialPlayerHands.put(player.getName(), new ArrayList<>(player.getHand()));
        }
    }

    @Override
    public void onInitialHandsHtmlClear() {
        initialPlayerHands.clear();
        simClaimCount = 0;
    }

    private String formatHandToHtml(List<Card> hand, List<Card> playedCards) {
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

            sb.append("<b>");
            boolean first = true;
            for (Card card : hand) {
                if (card.getSuit() == suit) {
                    if (!first) sb.append("&nbsp;");
                    
                    boolean isPlayed = playedCards != null && playedCards.contains(card);
                    String cardColor = isPlayed ? "#999999" : "white";
                    
                    sb.append("<font color='").append(cardColor).append("'>")
                            .append(card.getRank().display).append("</font>");
                    first = false;
                }
            }
            sb.append("</b>");
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
