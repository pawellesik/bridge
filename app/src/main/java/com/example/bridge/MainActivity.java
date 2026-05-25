package com.example.bridge;

import android.content.Intent;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BridgeGame";
    private List<Player> players;
    private Deck deck;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initGame();

        Button btnDeal = findViewById(R.id.btn_deal);
        btnDeal.setOnClickListener(v -> dealCards());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> signOut());
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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

    private void dealCards() {
        deck = new Deck(); // New deck each deal
        deck.shuffle();
        for (Player player : players) {
            player.clearHand();
            player.addCards(deck.deal(13));
            Log.d(TAG, "Player " + player.getName() + " has " + player.getHand().size() + " cards.");
        }

        // Display South's hand
        Player south = players.get(2); // South is index 2
        android.widget.TextView handView = findViewById(R.id.hand_south);
        StringBuilder sb = new StringBuilder();
        for (com.example.bridge.model.Card card : south.getHand()) {
            sb.append(card.toString()).append("\n");
        }
        handView.setText(sb.toString());

        Toast.makeText(this, "Cards Dealt!", Toast.LENGTH_SHORT).show();
    }
}