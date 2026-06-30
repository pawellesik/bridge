package com.example.bridge.ui.game;

import android.view.View;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import android.content.res.ColorStateList;

import com.example.bridge.R;
import com.example.bridge.model.Suit;

public class GameBidding {
    private final GameActivity activity;
    private final View controlsOverlay;
    private int currentLevel = 1;

    public GameBidding(GameActivity activity, View controlsOverlay) {
        this.activity = activity;
        this.controlsOverlay = controlsOverlay;
        setupListeners();
    }

    private void setupListeners() {
        if (controlsOverlay == null) return;

        int[] levelBtnIds = {
                R.id.btn_level_1, R.id.btn_level_2, R.id.btn_level_3,
                R.id.btn_level_4, R.id.btn_level_5, R.id.btn_level_6, R.id.btn_level_7
        };

        for (int i = 0; i < levelBtnIds.length; i++) {
            final int level = i + 1;
            View btn = controlsOverlay.findViewById(levelBtnIds[i]);
            if (btn != null) {
                btn.setOnClickListener(v -> selectLevel(level));
            }
        }
        
        applyColors();
        selectLevel(1);
    }

    public void selectLevel(int level) {
        this.currentLevel = level;
        if (controlsOverlay == null) return;

        int[] levelBtnIds = {
                R.id.btn_level_1, R.id.btn_level_2, R.id.btn_level_3,
                R.id.btn_level_4, R.id.btn_level_5, R.id.btn_level_6, R.id.btn_level_7
        };

        for (int i = 0; i < levelBtnIds.length; i++) {
            View btn = controlsOverlay.findViewById(levelBtnIds[i]);
            if (btn != null) {
                btn.setSelected((i + 1) == level);
            }
        }

        String levelStr = String.valueOf(level);
        updateButtonText(R.id.bid_clubs, levelStr);
        updateButtonText(R.id.bid_diamonds, levelStr);
        updateButtonText(R.id.bid_hearts, levelStr);
        updateButtonText(R.id.bid_spades, levelStr);
        updateButtonText(R.id.bid_nt, levelStr + " " + activity.getString(R.string.suit_nt));
    }

    private void updateButtonText(int id, String text) {
        TextView tv = controlsOverlay.findViewById(id);
        if (tv != null) tv.setText(text);
    }

    public void applyColors() {
        if (controlsOverlay == null) return;

        updateSuitButton(R.id.bid_clubs, Suit.CLUBS);
        updateSuitButton(R.id.bid_diamonds, Suit.DIAMONDS);
        updateSuitButton(R.id.bid_hearts, Suit.HEARTS);
        updateSuitButton(R.id.bid_spades, Suit.SPADES);
    }

    private void updateSuitButton(int id, Suit suit) {
        TextView tv = controlsOverlay.findViewById(id);
        if (tv == null) return;
        int color = suit.getColor(activity);
        tv.setTextColor(color);
        TextViewCompat.setCompoundDrawableTintList(tv, ColorStateList.valueOf(color));
    }
}
