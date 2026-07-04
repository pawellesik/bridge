package com.example.bridge.ui.biddings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.ColorStateList;

import com.example.bridge.R;
import com.example.bridge.model.Suit;
import com.example.bridge.ui.game.GameActivity;

import java.util.List;

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

        // Suit tiles, NT and Double (X) listeners
        int[] interactiveTileIds = {R.id.bid_clubs, R.id.bid_diamonds, R.id.bid_hearts, R.id.bid_spades, R.id.bid_nt, R.id.btn_bid_double_toggle};
        for (int id : interactiveTileIds) {
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

        int[] interactiveTileIds = {R.id.bid_clubs, R.id.bid_diamonds, R.id.bid_hearts, R.id.bid_spades, R.id.bid_nt, R.id.btn_bid_double_toggle};
        for (int id : interactiveTileIds) {
            View tile = controlsOverlay.findViewById(id);
            if (tile != null) {
                tile.setSelected(id == selectedSuitViewId);
            }
        }

        com.google.android.material.button.MaterialButton btnPass = controlsOverlay.findViewById(R.id.btn_bid_pass);
        if (btnPass != null) {
            if (selectedSuitViewId != View.NO_ID) {
                btnPass.setText("BID");
                btnPass.setBackgroundTintList(ColorStateList.valueOf(0xFF2E5A88)); // Professional Blue for bidding
            } else {
                btnPass.setText("PASS");
                btnPass.setBackgroundTintList(ColorStateList.valueOf(0xFF4D7C4F)); // Traditional Green for pass
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

        updateLevelUI(level);
        updateBiddingUI();
    }

    private void updateLevelUI(int level) {
        String levelStr = String.valueOf(level);
        updateTileText(R.id.bid_clubs_text, levelStr);
        updateTileText(R.id.bid_diamonds_text, levelStr);
        updateTileText(R.id.bid_hearts_text, levelStr);
        updateTileText(R.id.bid_spades_text, levelStr);

        TextView tvNt = controlsOverlay.findViewById(R.id.bid_nt);
        if (tvNt != null) tvNt.setText(levelStr + activity.getString(R.string.suit_nt));

        // Sync tab selection states
        int[] levelBtnIds = {
                R.id.btn_level_1, R.id.btn_level_2, R.id.btn_level_3,
                R.id.btn_level_4, R.id.btn_level_5, R.id.btn_level_6, R.id.btn_level_7
        };
        for (int i = 0; i < levelBtnIds.length; i++) {
            View btn = controlsOverlay.findViewById(levelBtnIds[i]);
            if (btn != null) btn.setSelected((i + 1) == level);
        }
    }

    public void applyAuctionRules(BiddingHistory history) {
        if (controlsOverlay == null || history == null) return;

        List<String> auction = history.getAuction();

        // 1. Level Tabs visibility
        int[] levelBtnIds = {
                R.id.btn_level_1, R.id.btn_level_2, R.id.btn_level_3,
                R.id.btn_level_4, R.id.btn_level_5, R.id.btn_level_6, R.id.btn_level_7
        };
        int firstVisibleLevel = -1;
        for (int i = 0; i < levelBtnIds.length; i++) {
            int lv = i + 1;
            boolean anyLegal = isLegalBid(lv, "NT", auction);
            View btn = controlsOverlay.findViewById(levelBtnIds[i]);
            if (btn != null) {
                btn.setVisibility(anyLegal ? View.VISIBLE : View.GONE);
                if (anyLegal && firstVisibleLevel == -1) firstVisibleLevel = lv;
            }
        }

        // If current level is hidden or numerically too low, jump to first visible
        if (firstVisibleLevel != -1 && (currentLevel < firstVisibleLevel || controlsOverlay.findViewById(levelBtnIds[currentLevel - 1]).getVisibility() == View.GONE)) {
            this.currentLevel = firstVisibleLevel;
            updateLevelUI(currentLevel);
        }

        // 2. Suit Tiles visibility for the CURRENT selected level
        updateTileVisibility(R.id.bid_clubs, isLegalBid(currentLevel, "C", auction), false);
        updateTileVisibility(R.id.bid_diamonds, isLegalBid(currentLevel, "D", auction), false);
        updateTileVisibility(R.id.bid_hearts, isLegalBid(currentLevel, "H", auction), false);
        updateTileVisibility(R.id.bid_spades, isLegalBid(currentLevel, "S", auction), false);
        updateTileVisibility(R.id.bid_nt, isLegalBid(currentLevel, "NT", auction), false);

        // 3. Double (X) button visibility
        // Rule: Visible only if the bid immediately before was not a "Pass"
        boolean canDouble = false;
        if (auction.size() > 1) { // Index 0 is often leading dashes, the last real bid is at size-2
            String lastBid = "";
            for (int i = auction.size() - 2; i >= 0; i--) {
                String b = auction.get(i);
                if (b != null && !b.isEmpty() && !"-".equals(b)) {
                    lastBid = b;
                    break;
                }
            }
            if (!lastBid.isEmpty() && !lastBid.equalsIgnoreCase("Pass") && !lastBid.equalsIgnoreCase("X") && !lastBid.equalsIgnoreCase("XX")) {
                canDouble = true;
            }
        }
        updateTileVisibility(R.id.btn_bid_double_toggle, canDouble, true);
    }

    private void updateTileVisibility(int id, boolean visible, boolean useGone) {
        View tile = controlsOverlay.findViewById(id);
        if (tile != null) {
            if (visible) {
                tile.setVisibility(View.VISIBLE);
            } else {
                tile.setVisibility(useGone ? View.GONE : View.INVISIBLE);
            }
        }
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

    public boolean isLegalBid(int level, String suitCode, List<String> auction) {
        String highestBid = null;
        for (int i = auction.size() - 1; i >= 0; i--) {
            String b = auction.get(i);
            if (b != null && !b.isEmpty() && !b.equalsIgnoreCase("Pass") && !b.equalsIgnoreCase("X") && !b.equalsIgnoreCase("XX") && !b.equals("-")) {
                highestBid = b;
                break;
            }
        }

        if (highestBid == null) return true; // No bids yet, any 1-7 is fine

        int currentLevel = Integer.parseInt(highestBid.substring(0, 1));
        String currentSuitCode = highestBid.substring(1).toUpperCase();

        int currentWeight = (currentLevel * 10) + getSuitWeight(currentSuitCode);
        int candidateWeight = (level * 10) + getSuitWeight(suitCode.toUpperCase());

        return candidateWeight > currentWeight;
    }

    private int getSuitWeight(String code) {
        switch (code) {
            case "C":
                return 1;
            case "D":
                return 2;
            case "H":
                return 3;
            case "S":
                return 4;
            case "NT":
                return 5;
            default:
                return 0;
        }
    }
}
