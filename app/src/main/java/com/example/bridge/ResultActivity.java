package com.example.bridge;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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

        displayContract(tvContract, ivContractSuit, contract);

        // Set contract container background
        View contractContainer = findViewById(R.id.game_contract_container);
        if (contractContainer != null && contract != null && !"PASS".equals(contract)) {
            contractContainer.setBackgroundResource(R.drawable.white_frame);
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
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
