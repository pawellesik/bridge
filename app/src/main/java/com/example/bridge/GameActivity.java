package com.example.bridge;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bridge.model.Card;
import com.example.bridge.model.Deck;
import com.example.bridge.model.Player;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Suit;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "BridgeGame";
    private List<Player> players;
    private Deck deck;
    private CardAdapter southAdapter;
    private List<Card> displayHand;
    private FrameLayout playedCardContainer;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int row1Count, row2Count;
    private int spacer1Index = -1, spacer2Index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initGame();

        playedCardContainer = findViewById(R.id.container_played_south);
        displayHand = new ArrayList<>();
        RecyclerView rvSouth = findViewById(R.id.rv_hand_south);
        
        androidx.recyclerview.widget.GridLayoutManager layoutManager =
            new androidx.recyclerview.widget.GridLayoutManager(this, 14);
            
        layoutManager.setSpanSizeLookup(new androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (displayHand.get(position) == null) {
                    return 1; // Spacers are always 1 span
                }
                return 2; // Cards are always 2 spans
            }
        });
        
        rvSouth.setLayoutManager(layoutManager);
        southAdapter = new CardAdapter(displayHand);
        southAdapter.setOnCardClickListener((card, position) -> {
            handler.postDelayed(() -> playCard(card, position), 500);
        });
        rvSouth.setAdapter(southAdapter);

        Button btnDeal = findViewById(R.id.btn_deal);
        btnDeal.setOnClickListener(v -> dealCards());
    }

    private void initGame() {
        players = new ArrayList<>();
        players.add(new Player("North"));
        players.add(new Player("East"));
        players.add(new Player("South"));
        players.add(new Player("West"));
        deck = new Deck();
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
        Toast.makeText(this, "Cards Dealt!", Toast.LENGTH_SHORT).show();
    }

    private void updateDisplayHand() {
        List<Card> actualHand = players.get(2).getHand();

        // Sort hand: Spades, Hearts, Diamonds, Clubs. Then High to Low rank.
        Collections.sort(actualHand, (c1, c2) -> {
            int s1 = getSuitPriority(c1.getSuit());
            int s2 = getSuitPriority(c2.getSuit());
            if (s1 != s2) {
                return s1 - s2;
            }
            return c2.getRank().ordinal() - c1.getRank().ordinal();
        });

        displayHand.clear();
        row1Count = Math.min(7, actualHand.size());
        row2Count = Math.max(0, actualHand.size() - 7);
        spacer1Index = -1;
        spacer2Index = -1;

        // If top row not full, add multiple spacers for stability
        int padding1 = (14 - row1Count * 2) / 2;
        for (int i = 0; i < padding1; i++) {
            if (i == 0) spacer1Index = displayHand.size();
            displayHand.add(null);
        }

        // Add top row cards
        for (int i = 0; i < row1Count; i++) {
            displayHand.add(actualHand.get(i));
        }

        // If bottom row has cards, add multiple spacers for stability
        if (row2Count > 0) {
            int padding2 = (14 - row2Count * 2) / 2;
            for (int i = 0; i < padding2; i++) {
                if (i == 0) spacer2Index = displayHand.size();
                displayHand.add(null);
            }
            for (int i = 0; i < row2Count; i++) {
                displayHand.add(actualHand.get(row1Count + i));
            }
        }

        southAdapter.clearSelection();
        southAdapter.notifyDataSetChanged();
    }

    private int getSuitPriority(Suit suit) {
        switch (suit) {
            case SPADES: return 0;
            case HEARTS: return 1;
            case CLUBS: return 2;
            case DIAMONDS: return 3;
            default: return 4;
        }
    }

    private void playCard(Card card, int displayPosition) {
        // Remove from player's logical hand
        players.get(2).removeCard(card);
        
        // Refresh the hand display
        updateDisplayHand();

        // Show the played card in the middle
        showPlayedCard(card);
    }

    private void showPlayedCard(Card card) {
        playedCardContainer.removeAllViews();
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_card, playedCardContainer, false);
        
        TextView tvRank = cardView.findViewById(R.id.tv_rank);
        ImageView ivSuitSmall = cardView.findViewById(R.id.iv_suit_small);
        ImageView ivSuitLarge = cardView.findViewById(R.id.iv_suit_large);

        String rankStr = getRankString(card.getRank());
        tvRank.setText(rankStr);
        int suitResId = getSuitDrawable(card.getSuit());
        ivSuitSmall.setImageResource(suitResId);
        ivSuitLarge.setImageResource(suitResId);

        int color = (card.getSuit() == Suit.HEARTS || card.getSuit() == Suit.DIAMONDS) 
                ? 0xFFFF0000 : 0xFF000000;
        tvRank.setTextColor(color);

        playedCardContainer.addView(cardView);
    }



    private String getRankString(Rank rank) {
        switch (rank) {
            case ACE: return "A";
            case KING: return "K";
            case QUEEN: return "Q";
            case JACK: return "J";
            case TEN: return "10";
            case NINE: return "9";
            case EIGHT: return "8";
            case SEVEN: return "7";
            case SIX: return "6";
            case FIVE: return "5";
            case FOUR: return "4";
            case THREE: return "3";
            case TWO: return "2";
            default: return "?";
        }
    }

    private int getSuitDrawable(Suit suit) {
        switch (suit) {
            case HEARTS: return R.drawable.suit_hearts;
            case DIAMONDS: return R.drawable.suit_diamonds;
            case SPADES: return R.drawable.suit_spades;
            case CLUBS: return R.drawable.suit_clubs;
            default: return 0;
        }
    }
}