package com.example.bridge;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.bridge.model.Deck;
import com.example.bridge.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    public static final Card GHOST_CARD = new Card(null, null);
    private List<Player> players;
    private Deck deck;
    private CardAdapter southAdapter;
    private CardAdapter northAdapter;
    private final List<Card> displayHandSouth = new ArrayList<>();
    private final List<Card> displayHandNorth = new ArrayList<>();
    private FrameLayout playedCardContainerSouth;
    private FrameLayout playedCardContainerNorth;
    private FrameLayout playedCardContainerWest;
    private FrameLayout playedCardContainerEast;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        setupWindowInsets();
        initGame();
        setupRecyclerView();

        playedCardContainerSouth = findViewById(R.id.container_played_south);
        playedCardContainerNorth = findViewById(R.id.container_played_north);
        playedCardContainerWest = findViewById(R.id.container_played_west);
        playedCardContainerEast = findViewById(R.id.container_played_east);
        findViewById(R.id.btn_deal).setOnClickListener(v -> dealCards());
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
        players.add(new Player("North"));
        players.add(new Player("East"));
        players.add(new Player("South"));
        players.add(new Player("West"));
        deck = new Deck();
    }

    private void setupRecyclerView() {

        RecyclerView rvSouth = findViewById(R.id.rv_hand_south);
        RecyclerView rvNorth = findViewById(R.id.rv_hand_north);

        GridLayoutManager southLayoutManager = new GridLayoutManager(this, 14);
        southLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (displayHandSouth.get(position) == null) ? 1 : 2;
            }
        });

        GridLayoutManager northLayoutManager = new GridLayoutManager(this, 14);
        northLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (displayHandNorth.get(position) == null) ? 1 : 2;
            }
        });

        rvSouth.setLayoutManager(southLayoutManager);
        southAdapter = new CardAdapter(displayHandSouth);
        southAdapter.setOnCardClickListener(card ->
                handler.postDelayed(() -> playCardSouth(card), 300));
        rvSouth.setAdapter(southAdapter);

        rvNorth.setLayoutManager(northLayoutManager);
        northAdapter = new CardAdapter(displayHandNorth);
        northAdapter.setOnCardClickListener(card ->
                handler.postDelayed(() -> playCardNorth(card), 300));
        rvNorth.setAdapter(northAdapter);
    }

    private void dealCards() {
        deck = new Deck();
        deck.shuffle();
        for (Player player : players) {
            player.clearHand();
            player.addCards(deck.deal(13));
        }
        updateDisplayHandSouth();
        updateDisplayHandNorth();
        playedCardContainerSouth.removeAllViews();
        playedCardContainerNorth.removeAllViews();
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
        int row1Count = total - row2Count;

        addCardsWithSpacersNorth(actualHand.subList(0, row1Count));
        addCardsWithSpacersNorth(actualHand.subList(row1Count, total));

        northAdapter.clearSelection();
        northAdapter.notifyDataSetChanged();
    }

    private void addCardsWithSpacersSouth(List<Card> rowCards) {
        int cardSpans = rowCards.size() * 2;
        int totalPadding = 14 - cardSpans;
        int leftPadding = totalPadding / 2;

        for (int i = 0; i < leftPadding; i++) displayHandSouth.add(null);
        displayHandSouth.addAll(rowCards);    }

    private void addCardsWithSpacersNorth(List<Card> rowCards) {
        if (rowCards.isEmpty()) {
            for (int i = 0; i < 7; i++) displayHandNorth.add(GHOST_CARD);
            return;
        }
        int cardSpans = rowCards.size() * 2;
        int totalPadding = 14 - cardSpans;
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        for (int i = 0; i < leftPadding; i++) displayHandNorth.add(null);
        displayHandNorth.addAll(rowCards);
        for (int i = 0; i < rightPadding; i++) displayHandNorth.add(null);
    }

    private void playCardSouth(Card card) {
        players.get(2).removeCard(card);
        updateDisplayHandSouth();
        showPlayedCardSouth(card);
    }
    private void playCardNorth(Card card) {
        players.get(0).removeCard(card);
        updateDisplayHandNorth();
        showPlayedCardNorth(card);
    }
    private void showPlayedCardSouth(Card card) {
        playedCardContainerSouth.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.item_card, playedCardContainerSouth, false);

        TextView tvRank = view.findViewById(R.id.tv_rank);
        ImageView ivSmall = view.findViewById(R.id.iv_suit_small);
        ImageView ivLarge = view.findViewById(R.id.iv_suit_large);

        tvRank.setText(card.getRank().display);
        ivSmall.setImageResource(card.getSuit().resId);
        ivLarge.setImageResource(card.getSuit().resId);
        tvRank.setTextColor(card.getSuit().isRed ? 0xFFFF0000 : 0xFF000000);

        playedCardContainerSouth.addView(view);
    }
    private void showPlayedCardNorth(Card card) {
        playedCardContainerNorth.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.item_card, playedCardContainerNorth, false);

        TextView tvRank = view.findViewById(R.id.tv_rank);
        ImageView ivSmall = view.findViewById(R.id.iv_suit_small);
        ImageView ivLarge = view.findViewById(R.id.iv_suit_large);

        tvRank.setText(card.getRank().display);
        ivSmall.setImageResource(card.getSuit().resId);
        ivLarge.setImageResource(card.getSuit().resId);
        tvRank.setTextColor(card.getSuit().isRed ? 0xFFFF0000 : 0xFF000000);

        playedCardContainerNorth.addView(view);
    }

}
