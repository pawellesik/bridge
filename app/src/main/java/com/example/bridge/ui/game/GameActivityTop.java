package com.example.bridge.ui.game;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bridge.R;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;

import java.util.Map;

public class GameActivityTop {
    private final GameActivity activity;

    private final View tvLastNorth, tvLastSouth, tvLastEast, tvLastWest;
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

        updateScores(0, 0);
    }



    public void setContract(Contract contract) {
        if (contractContainer != null)
            contractContainer.setBackgroundResource(R.drawable.bright_green_frame_black_sharp);

        if (contract == null || contract.isPass()) {
            tvContract.setText(activity.getString(R.string.contract_pass));
            tvContract.setTextColor(Color.WHITE);
            if (ivContractSuit != null) ivContractSuit.setVisibility(View.GONE);
            contractContainer.setVisibility(View.VISIBLE);
            return;
        }

        int count = contract.getLevel();
        com.example.bridge.model.Suit suit = contract.getSuit();

        tvContract.setText(" " + count);
        if (ivContractSuit != null) {
            if (contract.isNoTrump()) {
                tvContract.setText(" " + count + " " + activity.getString(R.string.suit_nt));
                ivContractSuit.setVisibility(View.GONE);
                tvContract.setTextColor(Color.BLACK);
            } else {
                ivContractSuit.setVisibility(View.VISIBLE);
                ivContractSuit.setImageResource(suit.resId);
                int suitColor = suit.getColor(activity);
                ivContractSuit.setColorFilter(suitColor);
                tvContract.setTextColor(suitColor);
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
        View[] lastCardViews = {tvLastNorth, tvLastSouth, tvLastEast, tvLastWest};
        for (View v : lastCardViews) {
            if (v instanceof android.view.ViewGroup) {
                v.setBackground(null);
                android.view.ViewGroup group = (android.view.ViewGroup) v;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (child instanceof TextView) ((TextView) child).setText("");
                    if (child instanceof ImageView) ((ImageView) child).setImageDrawable(null);
                }
            }
        }
    }

    private void updateLastCard(View container, Card card) {
        if (container instanceof android.view.ViewGroup && card != null) {
            android.view.ViewGroup group = (android.view.ViewGroup) container;
            TextView tvRank = null;
            ImageView ivSuit = null;

            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof TextView) tvRank = (TextView) child;
                else if (child instanceof ImageView) ivSuit = (ImageView) child;
            }

            if (tvRank != null) {
                tvRank.setText(card.getRank().display);
                tvRank.setTextColor(card.getSuit().getColor(container.getContext()));
            }
            if (ivSuit != null) {
                ivSuit.setImageResource(card.getSuit().resId);
                ivSuit.setColorFilter(card.getSuit().getColor(container.getContext()));
                ivSuit.setVisibility(View.VISIBLE);
            }

            container.setBackgroundResource(R.drawable.bright_green_frame_black_sharp);
        }
    }
}
