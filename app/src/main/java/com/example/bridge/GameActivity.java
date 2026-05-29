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
    private List<Card> cardsOnTable = new ArrayList<>();
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

        rvSouth.setLayoutManager(createLayoutManager(displayHandSouth));
        southAdapter = new CardAdapter(displayHandSouth, players.get(2));
        southAdapter.setOnCardClickListener(card ->
                handler.postDelayed(() -> playCard(players.get(2), card, playedCardContainerSouth), 300));
        rvSouth.setAdapter(southAdapter);

        rvNorth.setLayoutManager(createLayoutManager(displayHandNorth));
        northAdapter = new CardAdapter(displayHandNorth, players.get(0));
        northAdapter.setOnCardClickListener(card ->
                handler.postDelayed(() -> playCard(players.get(0), card, playedCardContainerNorth), 300));
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

    private void dealCards() {
        deck = new Deck();
        deck.shuffle();
        for (Player player : players) {
            player.clearHand();
            player.addCards(deck.deal(13));
            player.setCurrentMove(false);
        }
        clearTable();
        updateDisplayHandSouth();
        updateDisplayHandNorth();
        handler.postDelayed(() -> playCardOponent(players.get(3), playedCardContainerWest), 600);
        players.get(0).setCurrentMove(true);
        southAdapter.notifyDataSetChanged();
        northAdapter.notifyDataSetChanged();
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

    private void playCard(Player player, Card card, FrameLayout playedCardContainer) {
        player.removeCard(card);
        player.setCurrentMove(false);
        if ("North".equals(player.getName())) {
            updateDisplayHandNorth();
            handler.postDelayed(() -> playCardOponent(players.get(1), playedCardContainerEast), 600);
        } else if ("South".equals(player.getName())) {
            updateDisplayHandSouth();
            handler.postDelayed(() -> playCardOponent(players.get(3), playedCardContainerWest), 600);
        }
        showPlayedCard(card, player, playedCardContainer);
        southAdapter.notifyDataSetChanged();
        northAdapter.notifyDataSetChanged();

    }

    private void playCardOponent(Player player, FrameLayout playedCardContainer) {
        List<Card> hand = player.getHand();
        if (!hand.isEmpty()) {
            Card randomCard = hand.get((int) (Math.random() * hand.size()));
            //TODO choose the best card to throu
            playCard(player, randomCard, playedCardContainer);
        }
    }

    private void showPlayedCard(Card card, Player player, FrameLayout playedCardContainer) {
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

        cardsOnTable.add(card);
        if (this.cardsOnTable.size() == 4) {
            //todo chose currentplayer
            handler.postDelayed(this::clearTable, 1000);
        } else {
            players.get(getNextPlayerIndex(players.indexOf(player))).setCurrentMove(true);
        }
    }

    private int getNextPlayerIndex(int current) {
        if (current == 2) return 3;
        if (current == 3) return 0;
        if (current == 0) return 1;
        return 2;
    }

    private void clearTable() {
        playedCardContainerSouth.removeAllViews();
        playedCardContainerNorth.removeAllViews();
        playedCardContainerWest.removeAllViews();
        playedCardContainerEast.removeAllViews();
        cardsOnTable.clear();
    }

}
