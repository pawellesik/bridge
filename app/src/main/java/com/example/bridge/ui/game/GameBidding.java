package com.example.bridge.ui.game;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.ColorStateList;

import com.example.bridge.R;
import com.example.bridge.model.Suit;

public class GameBidding {
    private final GameActivity activity;
    private final View controlsOverlay;
    private int currentLevel = 1;
    private int selectedSuitViewId = View.NO_ID;

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

        // Suit tiles and NT listeners
        int[] suitTileIds = {R.id.bid_clubs, R.id.bid_diamonds, R.id.bid_hearts, R.id.bid_spades, R.id.bid_nt};
        for (int id : suitTileIds) {
            View tile = controlsOverlay.findViewById(id);
            if (tile != null) {
                tile.setOnClickListener(v -> toggleSuitSelection(id));
            }
        }
        
        applyColors();
        selectLevel(1);
    }

    private void toggleSuitSelection(int viewId) {
        if (selectedSuitViewId == viewId) {
            selectedSuitViewId = View.NO_ID;
        } else {
            selectedSuitViewId = viewId;
        }
        updateBiddingUI();
    }

    private void updateBiddingUI() {
        if (controlsOverlay == null) return;

        int[] suitTileIds = {R.id.bid_clubs, R.id.bid_diamonds, R.id.bid_hearts, R.id.bid_spades, R.id.bid_nt};
        for (int id : suitTileIds) {
            View tile = controlsOverlay.findViewById(id);
            if (tile != null) {
                tile.setSelected(id == selectedSuitViewId);
            }
        }

        com.google.android.material.button.MaterialButton btnPass = controlsOverlay.findViewById(R.id.btn_bid_pass);
        if (btnPass != null) {
            if (selectedSuitViewId != View.NO_ID) {
                btnPass.setText("BID");
            } else {
                btnPass.setText("PASS");
            }
        }
    }

    public void selectLevel(int level) {
        this.currentLevel = level;
        selectedSuitViewId = View.NO_ID;
        
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
        updateTileText(R.id.bid_clubs_text, levelStr);
        updateTileText(R.id.bid_diamonds_text, levelStr);
        updateTileText(R.id.bid_hearts_text, levelStr);
        updateTileText(R.id.bid_spades_text, levelStr);
        
        TextView tvNt = controlsOverlay.findViewById(R.id.bid_nt);
        if (tvNt != null) tvNt.setText(levelStr + " " + activity.getString(R.string.suit_nt));
        
        updateBiddingUI();
    }

    private void updateTileText(int id, String text) {
        TextView tv = controlsOverlay.findViewById(id);
        if (tv != null) tv.setText(text);
    }

    public void applyColors() {
        if (controlsOverlay == null) return;

        updateSuitTile(R.id.bid_clubs_text, R.id.bid_clubs_icon, Suit.CLUBS);
        updateSuitTile(R.id.bid_diamonds_text, R.id.bid_diamonds_icon, Suit.DIAMONDS);
        updateSuitTile(R.id.bid_hearts_text, R.id.bid_hearts_icon, Suit.HEARTS);
        updateSuitTile(R.id.bid_spades_text, R.id.bid_spades_icon, Suit.SPADES);
    }

    private void updateSuitTile(int textId, int iconId, Suit suit) {
        TextView tv = controlsOverlay.findViewById(textId);
        ImageView iv = controlsOverlay.findViewById(iconId);
        int color = suit.getColor(activity);
        
        if (tv != null) tv.setTextColor(color);
        if (iv != null) iv.setImageTintList(ColorStateList.valueOf(color));
    }
}
