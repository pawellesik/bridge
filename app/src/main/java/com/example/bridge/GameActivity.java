package com.example.bridge;

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

    public static final Card GHOST_CARD = new Card(null, null);
    private static final String PREFS_NAME = "BridgePrefs";
    private static final String KEY_CAREER_SCORE = "careerScore";
    private static final int REQUEST_RESULT =1;

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
    private TextView tvScoreSN, tvScoreWE, tvTotalTricks;
    private final Map<String, String> initialHandsHtml = new LinkedHashMap<>();
    private TextView nameNorth, nameSouth, nameEast, nameWest;
    private TextView tvContract;
    private ImageView ivContractSuit;
    private View contractContainer;
    private View startBar;
    private View btnClaim;
    private boolean isProcessingMove = false;

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
        tvTotalTricks = findViewById(R.id.tv_total_tricks);

        tvContract = findViewById(R.id.game_contract);
        ivContractSuit = findViewById(R.id.iv_contract_suit);
        contractContainer = findViewById(R.id.game_contract_container);
        startBar = findViewById(R.id.start_bar);
        btnClaim = findViewById(R.id.btn_claim);

        initGame();
        setupRecyclerView();
        gameController.dealCards();
        findViewById(R.id.btn_deal).setOnClickListener(v -> {
            if (isProcessingMove) return;
            isProcessingMove = true;
            if (startBar != null) startBar.setVisibility(View.VISIBLE);
            gameController.dealCards();
        });
        findViewById(R.id.btn_start).setOnClickListener(v -> {
            if (isProcessingMove) return;
            isProcessingMove = true;
            startBar.setVisibility(View.GONE);
            gameController.startGame();
        });
        btnClaim.setOnClickListener(v -> {
            if (isProcessingMove) return;
            isProcessingMove = true;
            btnClaim.setVisibility(View.GONE);
            gameController.claimRest();
        });
    }

    @Override
    public void onClaimButtonVisibilityChanged(boolean visible) {
        if (btnClaim != null) {
            btnClaim.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onScoreUpdated(int snScore, int weScore) {
        if (tvScoreSN != null) tvScoreSN.setText("SN: " + snScore);
        if (tvScoreWE != null) tvScoreWE.setText("WE: " + weScore);
    }

    @Override
    public void onGameEnded(int snScore, int weScore, String contract, List<String> history, List<String> historyWinTrick) {
        int level = 0;
        try {
            level = Integer.parseInt(contract.split(" ")[0].trim());
        } catch (Exception e) {
            return;
        }

        int requiredTricks = level + 6;
        int handScore;

        if (snScore >= requiredTricks) {
            handScore = level + (snScore - requiredTricks);
        } else {
            handScore = -level + (snScore - requiredTricks);
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int careerScore = prefs.getInt(KEY_CAREER_SCORE, 0);
        careerScore += handScore;

        prefs.edit().putInt(KEY_CAREER_SCORE, careerScore).apply();

        if (tvTotalTricks != null) {
            tvTotalTricks.setText("score: " + careerScore);
        }

        final int finalCareerScore = careerScore;
        findViewById(R.id.main).postDelayed(() -> {
            Intent intent = new Intent(GameActivity.this, ResultActivity.class);
            for (Map.Entry<String, String> entry : initialHandsHtml.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
            intent.putExtra("snScore", snScore);
            intent.putExtra("weScore", weScore);
            intent.putExtra("contract", contract);
            intent.putExtra("careerScore", finalCareerScore);
            intent.putStringArrayListExtra("history", new ArrayList<>(history));
            intent.putStringArrayListExtra("historyWinTrick", new ArrayList<>(historyWinTrick));

            // Pass last trick cards
            intent.putExtra("last_n", tvLastNorth.getText().toString());
            intent.putExtra("last_s", tvLastSouth.getText().toString());
            intent.putExtra("last_e", tvLastEast.getText().toString());
            intent.putExtra("last_w", tvLastWest.getText().toString());

            startActivityForResult(intent, REQUEST_RESULT);
        }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESULT) {
            // Check for DEAL_AGAIN action or if the result was cancelled (system back button)
            if (resultCode == RESULT_OK && data != null && "DEAL_AGAIN".equals(data.getStringExtra("action"))) {
                dealNewCards();
            } else {
                // If they just pressed back, we still might want a new deal 
                // since the game ended.
                dealNewCards();
            }
        }
    }

    private void dealNewCards() {
        if (startBar != null) startBar.setVisibility(View.VISIBLE);
        gameController.dealCards();
    }
    private void loadAndRestoreScores() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int careerScore = prefs.getInt(KEY_CAREER_SCORE, 0);
        if (tvTotalTricks != null) {
            tvTotalTricks.setText("score: " + careerScore);
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
        if (contractContainer != null)
            contractContainer.setBackgroundResource(R.drawable.white_frame_in_bright_green);

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
                tvContract.setText(" " + contract);
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
        loadAndRestoreScores();
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
    public void onInitialHandsHtml(){
        // Capture hands AFTER swapping/sorting is complete
        initialHandsHtml.clear();
        for (Player player : players.values()) {
            initialHandsHtml.put(player.getName(), formatHandToHtml(player.getHand()));
        }
    }
    @Override
    public void onInitialHandsHtmlClear(){
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
            tv.setText(" "+card.getRank().display + " " + card.getSuit().symbol);
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
        int cardSpans = rowCards.size() * 2;
        int totalPadding = 14 - cardSpans;
        int leftPadding = totalPadding / 2;

        for (int i = 0; i < leftPadding; i++) displayHandSouth.add(null);
        displayHandSouth.addAll(rowCards);
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
