package com.example.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        setupWindowInsets();

        // Display player hands
        displayHand(R.id.tv_north_cards, getIntent().getStringExtra("North"));
        displayHand(R.id.tv_east_cards, getIntent().getStringExtra("East"));
        displayHand(R.id.tv_south_cards, getIntent().getStringExtra("South"));
        displayHand(R.id.tv_west_cards, getIntent().getStringExtra("West"));

        // Setup top bar data
        TextView tvContract = findViewById(R.id.game_contract);
        ImageView ivContractSuit = findViewById(R.id.iv_contract_suit);
        TextView tvWeScore = findViewById(R.id.we_score);
        TextView tvSnScore = findViewById(R.id.sn_score);
        TextView tvTotalScore = findViewById(R.id.tv_total_tricks);

        String contract = getIntent().getStringExtra("contract");
        int snScore = getIntent().getIntExtra("snScore", 0);
        int weScore = getIntent().getIntExtra("weScore", 0);
        int careerScore = getIntent().getIntExtra("careerScore", 0);

        tvSnScore.setText("SN: " + snScore);
        tvWeScore.setText("WE: " + weScore);
        tvTotalScore.setText("score: " + careerScore);

        // Display last trick cards
        ((TextView) findViewById(R.id.n_last_card)).setText(getIntent().getStringExtra("last_n"));
        ((TextView) findViewById(R.id.s_last_card)).setText(getIntent().getStringExtra("last_s"));
        ((TextView) findViewById(R.id.e_last_card)).setText(getIntent().getStringExtra("last_e"));
        ((TextView) findViewById(R.id.w_last_card)).setText(getIntent().getStringExtra("last_w"));

        displayContract(tvContract, ivContractSuit, contract);

        // Display play history
        List<String> history = getIntent().getStringArrayListExtra("history");
        List<String> historyWinTrick = getIntent().getStringArrayListExtra("historyWinTrick");

        displayHistory(history, historyWinTrick);

        // Set contract container background
        View contractContainer = findViewById(R.id.game_contract_container);
        if (contractContainer != null && contract != null && !"PASS".equals(contract)) {
            contractContainer.setBackgroundResource(R.drawable.white_frame_in_bright_green);
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("action", "DEAL_AGAIN");
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("action", "DEAL_AGAIN");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayHistory(List<String> history, List<String> historyWinTrick) {
        TableLayout table = findViewById(R.id.table_history);
        if (history == null || table == null) return;

        for (int i = 0; i < history.size(); i += 4) {
            TableRow row = new TableRow(this);
            String[] trickData = new String[4]; // N, E, S, W
            int trickIndex = i / 4;
            int winnerCol = -1;

            if (historyWinTrick != null && trickIndex < historyWinTrick.size()) {
                winnerCol = getPlayerColumn(historyWinTrick.get(trickIndex));
            }

            for (int j = 0; j < 4 && (i + j) < history.size(); j++) {
                String entry = history.get(i + j);
                if (entry.startsWith("NS claimed")) {
                    TextView claimTv = new TextView(this);
                    claimTv.setText(entry);
                    claimTv.setTextColor(Color.RED);
                    claimTv.setPadding(16, 8, 16, 8);
                    table.addView(claimTv);
                    return; 
                }
                
                String[] parts = entry.split(": ");
                if (parts.length == 2) {
                    String name = parts[0];
                    String cardStr = parts[1];
                    int col = getPlayerColumn(name);
                    if (col != -1) {
                        trickData[col] = cardStr;
                    }
                }
            }

            for (int c = 0; c < 4; c++) {
                TextView tv = new TextView(this);
                tv.setText(trickData[c] != null ? trickData[c] : "-");
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(8, 16, 8, 16); // Slightly more padding
                if (c == winnerCol) {
                    tv.setBackgroundResource(R.drawable.white_frame_in_bright_green);
                    tv.setTypeface(null, android.graphics.Typeface.BOLD);
                }
                row.addView(tv);
            }
            table.addView(row);
        }
    }

    private int getPlayerColumn(String name) {
        if ("West".equals(name)) return 0;
        if ("North".equals(name)) return 1;
        if ("East".equals(name)) return 2;
        if ("South".equals(name)) return 3;
        return -1;
    }

    private void displayHand(int viewId, String handHtml) {
        TextView tv = findViewById(viewId);
        if (handHtml != null) {
            Spanned formatted = Html.fromHtml(handHtml, Html.FROM_HTML_MODE_LEGACY);
            tv.setText(formatted);
        }
    }

    private void displayContract(TextView tvContract, ImageView ivContractSuit, String contract) {
        if (contract == null || "PASS".equals(contract)) {
            tvContract.setText(" PASS");
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
                tvContract.setText(" " + contract);
                ivContractSuit.setVisibility(View.GONE);
            } else {
                ivContractSuit.setVisibility(View.VISIBLE);
                switch (color) {
                    case "Spades": ivContractSuit.setImageResource(R.drawable.spades); break;
                    case "Hearts": ivContractSuit.setImageResource(R.drawable.heart); break;
                    case "Diamonds": ivContractSuit.setImageResource(R.drawable.diamonds); break;
                    case "Clubs": ivContractSuit.setImageResource(R.drawable.clubs); break;
                    default:
                        tvContract.setText(" " + contract);
                        ivContractSuit.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }
}
