package com.example.bridge;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bridge.model.Deck;
import com.example.bridge.model.Player;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "BridgeGame";
    private List<Player> players;
    private Deck deck;
    private CardAdapter southAdapter;
    private List<com.example.bridge.model.Card> displayHand;

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

        displayHand = new ArrayList<>();
        RecyclerView rvSouth = findViewById(R.id.rv_hand_south);
        
        // 14 spans allows us to center the bottom row
        // Row 1: 7 cards * 2 spans = 14
        // Row 2: 1 spacer (1 span) + 6 cards (2 spans each) + 1 spacer (1 span) = 14
        androidx.recyclerview.widget.GridLayoutManager layoutManager = 
            new androidx.recyclerview.widget.GridLayoutManager(this, 14);
            
        layoutManager.setSpanSizeLookup(new androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 7) {
                    return 1; // Spacer at start of second row
                }
                return 2; // All cards take 2 spans
            }
        });
        
        rvSouth.setLayoutManager(layoutManager);
        southAdapter = new CardAdapter(displayHand);
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
        deck = new Deck(); // New deck each deal
        deck.shuffle();
        for (Player player : players) {
            player.clearHand();
            player.addCards(deck.deal(13));
            Log.d(TAG, "Player " + player.getName() + " has " + player.getHand().size() + " cards.");
        }

        // Refresh South's hand display
        List<com.example.bridge.model.Card> actualHand = players.get(2).getHand();
        displayHand.clear();
        
        // Add first 7 cards
        for (int i = 0; i < 7 && i < actualHand.size(); i++) {
            displayHand.add(actualHand.get(i));
        }
        
        // Add spacer for centering the second row
        if (actualHand.size() > 7) {
            displayHand.add(null); 
            // Add remaining cards
            for (int i = 7; i < actualHand.size(); i++) {
                displayHand.add(actualHand.get(i));
            }
        }

        southAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Cards Dealt!", Toast.LENGTH_SHORT).show();
    }
}