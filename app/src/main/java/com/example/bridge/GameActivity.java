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

    private List<Player> players;
    private Deck deck;
    private CardAdapter southAdapter;
    private final List<Card> displayHand = new ArrayList<>();
    private FrameLayout playedCardContainer;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        setupWindowInsets();
        initGame();
        setupRecyclerView();

        playedCardContainer = findViewById(R.id.container_played_south);
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
        GridLayoutManager layoutManager = new GridLayoutManager(this, 14);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (displayHand.get(position) == null) ? 1 : 2;
            }
        });

        rvSouth.setLayoutManager(layoutManager);
        southAdapter = new CardAdapter(displayHand);
        southAdapter.setOnCardClickListener(card -> handler.postDelayed(() -> playCard(card), 300));
        rvSouth.setAdapter(southAdapter);
    }

    private void dealCards() {
        deck = new Deck();
        deck.shuffle();
        for (Player player : players) {
            player.clearHand();
            player.addCards(deck.deal(13));
        }
        updateDisplayHand();
        playedCardContainer.removeAllViews();
    }

    private void updateDisplayHand() {
        List<Card> actualHand = players.get(2).getHand();
        displayHand.clear();

        int row1End = Math.min(7, actualHand.size());
        addCardsWithSpacers(actualHand.subList(0, row1End));
        if (actualHand.size() > 7) {
            addCardsWithSpacers(actualHand.subList(7, actualHand.size()));
        }

        southAdapter.clearSelection();
        southAdapter.notifyDataSetChanged();
    }

    private void addCardsWithSpacers(List<Card> rowCards) {
        int padding = (14 - rowCards.size() * 2) / 2;
        for (int i = 0; i < padding; i++) displayHand.add(null);
        displayHand.addAll(rowCards);
    }

    private void playCard(Card card) {
        players.get(2).removeCard(card);
        updateDisplayHand();
        showPlayedCard(card);
    }

    private void showPlayedCard(Card card) {
        playedCardContainer.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.item_card, playedCardContainer, false);

        TextView tvRank = view.findViewById(R.id.tv_rank);
        ImageView ivSmall = view.findViewById(R.id.iv_suit_small);
        ImageView ivLarge = view.findViewById(R.id.iv_suit_large);

        tvRank.setText(card.getRank().display);
        ivSmall.setImageResource(card.getSuit().resId);
        ivLarge.setImageResource(card.getSuit().resId);
        tvRank.setTextColor(card.getSuit().isRed ? 0xFFFF0000 : 0xFF000000);

        playedCardContainer.addView(view);
    }
}
