package com.example.bridge;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bridge.model.Card;

import java.util.Map;

public class GameActivityTop {
    private final GameActivity activity;

    private final TextView tvLastNorth, tvLastSouth, tvLastEast, tvLastWest;
    private final TextView tvScoreSN, tvScoreWE, tvMiddle1, tvMiddle2, tvMiddle3;

    private final TextView tvContract;
    private final ImageView ivContractSuit;
    private final View contractContainer;

    public GameActivityTop(GameActivity activity) {
        this.activity = activity;

        tvLastNorth = activity.findViewById(R.id.n_last_card);
        tvLastSouth = activity.findViewById(R.id.s_last_card);
        tvLastEast = activity.findViewById(R.id.e_last_card);
        tvLastWest = activity.findViewById(R.id.w_last_card);

        tvScoreSN = activity.findViewById(R.id.sn_score);
        tvScoreWE = activity.findViewById(R.id.we_score);

        tvMiddle1 = activity.findViewById(R.id.tv_middle_1);
        tvMiddle2 = activity.findViewById(R.id.tv_middle_2);
        tvMiddle3 = activity.findViewById(R.id.tv_middle_3);

        tvContract = activity.findViewById(R.id.game_contract);
        ivContractSuit = activity.findViewById(R.id.iv_contract_suit);
        contractContainer = activity.findViewById(R.id.game_contract_container);
    }



    public void setContract(String contract) {
        if (contractContainer != null)
            contractContainer.setBackgroundResource(R.drawable.bright_green_frame_black_sharp);

        if (contract == null || contract.equals("PASS")) {
            tvContract.setText(activity.getString(R.string.contract_pass));
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
                tvContract.setText(" " + count + " " + activity.getString(R.string.suit_nt));
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
        contractContainer.setVisibility(View.VISIBLE);
    }

    public void hideContract() {
        if (contractContainer != null) contractContainer.setVisibility(View.GONE);
    }

    public void updateScores(int snScore, int weScore) {
        if (tvScoreSN != null) tvScoreSN.setText(activity.getString(R.string.sn_label, snScore));
        if (tvScoreWE != null) tvScoreWE.setText(activity.getString(R.string.we_label, weScore));
    }

    public void setTotalScore(int totalScore, int changeScore) {
        tvMiddle1.setText(activity.getString(R.string.score_label));
        tvMiddle2.setText(String.valueOf(totalScore));
        if (changeScore != 0) {
            tvMiddle3.setText(changeScore > 0 ? "(+" + changeScore + ")" : "(" + changeScore + ")");
            tvMiddle3.setTextColor(changeScore > 0 ? Color.parseColor("#C8E6C9") : Color.RED);
        } else {
            tvMiddle3.setText("");
        }
    }

    public void setTotalScore(int totalScore) {
        tvMiddle1.setText(activity.getString(R.string.score_label));
        tvMiddle2.setText(String.valueOf(totalScore));
        tvMiddle3.setText("");
    }

    public void onTableCleared(Map<String, Card> trickCards) {
        if (trickCards != null && !trickCards.isEmpty()) {
            updateLastCard(tvLastNorth, trickCards.get("North"));
            updateLastCard(tvLastSouth, trickCards.get("South"));
            updateLastCard(tvLastEast, trickCards.get("East"));
            updateLastCard(tvLastWest, trickCards.get("West"));
        } else {
            clearLastCards();
        }
    }

    public void clearLastCards() {
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
            tv.setText(card.getRank().display + " " + card.getSuit().symbol);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setBackgroundResource(R.drawable.bright_green_frame_black_sharp);
        }
    }
}
