package com.example.bridge;

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
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements GameController.GameCallback {

    public static final Card GHOST_CARD = new Card(null, null);
    private List<Player> players;
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

        initGame();
        setupRecyclerView();

        findViewById(R.id.btn_deal).setOnClickListener(v -> gameController.dealCards());
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initGame() {
        players = new ArrayList<>();
        players.add(new Player("North", playedCardContainerNorth));
        players.add(new Player("East", playedCardContainerEast));
        players.add(new Player("South", playedCardContainerSouth));
        players.add(new Player("West", playedCardContainerWest));
        
        gameController = new GameController(players, this);
    }

    private void setupRecyclerView() {
        RecyclerView rvSouth = findViewById(R.id.rv_hand_south);
        RecyclerView rvNorth = findViewById(R.id.rv_hand_north);

        rvSouth.setLayoutManager(createLayoutManager(displayHandSouth));
        southAdapter = new CardAdapter(displayHandSouth, players.get(2));
        southAdapter.setOnCardClickListener(card -> 
                findViewById(R.id.main).postDelayed(() -> gameController.playCard(players.get(2), card), 300));
        rvSouth.setAdapter(southAdapter);

        rvNorth.setLayoutManager(createLayoutManager(displayHandNorth));
        northAdapter = new CardAdapter(displayHandNorth, players.get(0));
        northAdapter.setOnCardClickListener(card -> 
                findViewById(R.id.main).postDelayed(() -> gameController.playCard(players.get(0), card), 300));
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
    public void onHandUpdated(int playerIndex) {
        if ("North".equals(players.get(playerIndex).getName())) {
            updateDisplayHandNorth();
        } else if ("South".equals(players.get(playerIndex).getName())) {
            updateDisplayHandSouth();
        }
    }

    @Override
    public void onCardPlayed(Player player, Card card) {
        showPlayedCard(card, player.getPlayedCardContainer());
    }

    @Override
    public void onTableCleared(Map<String, Card> trickCards) {
        playedCardContainerSouth.removeAllViews();
        playedCardContainerNorth.removeAllViews();
        playedCardContainerWest.removeAllViews();
        playedCardContainerEast.removeAllViews();

        if (trickCards.isEmpty()) {
            tvLastNorth.setText("");
            tvLastSouth.setText("");
            tvLastEast.setText("");
            tvLastWest.setText("");
        } else {
            updateLastCard(tvLastNorth, trickCards.get("North"));
            updateLastCard(tvLastSouth, trickCards.get("South"));
            updateLastCard(tvLastEast, trickCards.get("East"));
            updateLastCard(tvLastWest, trickCards.get("West"));
        }
    }

    private void updateLastCard(TextView tv, Card card) {
        if (card != null) {
            tv.setText(card.getRank().display + " " + card.getSuit().symbol);
            tv.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void updateDisplayHandSouth() {
        List<Card> actualHand = players.get(2).getHand();
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
        List<Card> actualHand = players.get(0).getHand();
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
