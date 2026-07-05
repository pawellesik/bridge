package com.example.bridge.ui.biddings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.ColorStateList;

import com.example.bridge.R;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;
import com.example.bridge.ui.game.GameActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class GameBidding {
    private final GameActivity activity;

    private int currentLevel = 1;
    private int selectedSuitViewId = View.NO_ID;
    private GameBiddingHistory lastHistory;
    private MaterialButton btnPass;

    public GameBidding(GameActivity activity) {
        this.activity = activity;
        if (activity.getControlsOverlay() != null) {
            btnPass = activity.getControlsOverlay().findViewById(R.id.btn_bid_pass);
        }
        setupListeners();
    }

    private void setupListeners() {
        if (activity.getControlsOverlay() == null) return;

        int[] levelBtnIds = {
                R.id.btn_level_1, R.id.btn_level_2, R.id.btn_level_3,
                R.id.btn_level_4, R.id.btn_level_5, R.id.btn_level_6, R.id.btn_level_7
        };

        for (int m = 0; m < levelBtnIds.length; m++) {
            final int level = m + 1;
            View btn = activity.getControlsOverlay().findViewById(levelBtnIds[m]);
            if (btn != null) {
                btn.setOnClickListener(v -> selectLevel(level));
            }
        }

        // Suit tiles, NT and Double (X) listeners
        int[] interactiveTileIds = {R.id.bid_clubs, R.id.bid_diamonds, R.id.bid_hearts, R.id.bid_spades, R.id.bid_nt, R.id.btn_bid_double_toggle};
        for (int id : interactiveTileIds) {
            View tile = activity.getControlsOverlay().findViewById(id);
            if (tile != null) {
                tile.setOnClickListener(v -> toggleSuitSelection(id));
            }
        }

        if (btnPass != null) {
            btnPass.setOnClickListener(v -> handleBidOrPass(v));
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
        updateSelectionInHistory();
    }

    private void updateBiddingUI() {
        if (activity.getControlsOverlay() == null) return;

        int[] interactiveTileIds = {R.id.bid_clubs, R.id.bid_diamonds, R.id.bid_hearts, R.id.bid_spades, R.id.bid_nt, R.id.btn_bid_double_toggle};
        for (int id : interactiveTileIds) {
            View tile = activity.getControlsOverlay().findViewById(id);
            if (tile != null) {
                tile.setSelected(id == selectedSuitViewId);
            }
        }

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

        if (activity.getControlsOverlay() == null) return;

        updateLevelUI(level);
        refreshSuitTilesVisibility();
        updateBiddingUI();
        updateSelectionInHistory();
    }

    private void updateLevelUI(int level) {
        String levelStr = String.valueOf(level);
        updateTileText(R.id.bid_clubs_text, levelStr);
        updateTileText(R.id.bid_diamonds_text, levelStr);
        updateTileText(R.id.bid_hearts_text, levelStr);
        updateTileText(R.id.bid_spades_text, levelStr);

        TextView tvNt = activity.getControlsOverlay().findViewById(R.id.bid_nt);
        if (tvNt != null) tvNt.setText(levelStr + " " + activity.getString(R.string.suit_nt));

        // Sync tab selection states
        int[] levelBtnIds = {
                R.id.btn_level_1, R.id.btn_level_2, R.id.btn_level_3,
                R.id.btn_level_4, R.id.btn_level_5, R.id.btn_level_6, R.id.btn_level_7
        };
        for (int i = 0; i < levelBtnIds.length; i++) {
            View btn = activity.getControlsOverlay().findViewById(levelBtnIds[i]);
            if (btn != null) btn.setSelected((i + 1) == level);
        }
    }

    public void applyAuctionRules(GameBiddingHistory history) {
        if (activity.getControlsOverlay() == null || history == null) return;
        this.lastHistory = history;

        List<String> auction = history.getAuction();

        // 1. Level Tabs visibility and find the first possible level
        int[] levelBtnIds = {
                R.id.btn_level_1, R.id.btn_level_2, R.id.btn_level_3,
                R.id.btn_level_4, R.id.btn_level_5, R.id.btn_level_6, R.id.btn_level_7
        };
        int firstVisibleLevel = -1;
        int tabIdx = 0;
        for (int id : levelBtnIds) {
            int lv = ++tabIdx;
            // A level is legal if at least its highest bid (NT) is legal
            boolean anyLegal = isLegalBid(lv, "NT", auction);
            View btn = activity.getControlsOverlay().findViewById(id);
            if (btn != null) {
                btn.setVisibility(anyLegal ? View.VISIBLE : View.GONE);
                if (anyLegal && firstVisibleLevel == -1) firstVisibleLevel = lv;
            }
        }

        // Always jump to and select the FIRST possible level when rules are applied (turn starts)
        if (firstVisibleLevel != -1) {
            this.currentLevel = firstVisibleLevel;
            updateLevelUI(currentLevel);
        }

        refreshSuitTilesVisibility();

        // 3. Double (X) / Redouble (XX) logic
        boolean showDoubleBtn = false;
        String doubleText = "X";
        int doubleColor = 0xFFC62828; // Standard Red

        if (!auction.isEmpty()) {
            String lastMeaningfulBid = "";
            for (int i = auction.size() - 1; i >= 0; i--) {
                String b = auction.get(i);
                if (b != null && !b.isEmpty() && !"-".equals(b) && !b.equalsIgnoreCase("Pass")) {
                    lastMeaningfulBid = b;
                    break;
                }
            }

            if (!lastMeaningfulBid.isEmpty()) {
                if (lastMeaningfulBid.equalsIgnoreCase("X")) {
                    showDoubleBtn = true;
                    doubleText = "XX";
                    doubleColor = 0xFF1565C0; // Professional Blue for Redouble
                } else if (!lastMeaningfulBid.equalsIgnoreCase("XX")) {
                    // It's a suit bid
                    showDoubleBtn = true;
                    doubleText = "X";
                    doubleColor = 0xFFC62828;
                }
            }
        }

        TextView tvDouble = activity.getControlsOverlay().findViewById(R.id.btn_bid_double_toggle);
        if (tvDouble != null) {
            tvDouble.setText(doubleText);
            tvDouble.setTextColor(doubleColor);
            tvDouble.setVisibility(showDoubleBtn ? View.VISIBLE : View.GONE);
        }

        // Reset suit selection for the new turn/history update
        this.selectedSuitViewId = View.NO_ID;
        updateBiddingUI();
    }

    private void refreshSuitTilesVisibility() {
        if (lastHistory == null) return;
        List<String> auction = lastHistory.getAuction();

        updateTileVisibility(R.id.bid_clubs, isLegalBid(currentLevel, "C", auction), false);
        updateTileVisibility(R.id.bid_diamonds, isLegalBid(currentLevel, "D", auction), false);
        updateTileVisibility(R.id.bid_hearts, isLegalBid(currentLevel, "H", auction), false);
        updateTileVisibility(R.id.bid_spades, isLegalBid(currentLevel, "S", auction), false);
        updateTileVisibility(R.id.bid_nt, isLegalBid(currentLevel, "NT", auction), false);
    }

    private void updateTileVisibility(int id, boolean visible, boolean useGone) {
        View tile = activity.getControlsOverlay().findViewById(id);
        if (tile != null) {
            if (visible) {
                tile.setVisibility(View.VISIBLE);
            } else {
                tile.setVisibility(useGone ? View.GONE : View.INVISIBLE);
            }
        }
    }

    private void updateTileText(int id, String text) {
        TextView tv = activity.getControlsOverlay().findViewById(id);
        if (tv != null) tv.setText(text);
    }

    public void applyColors() {
        if (activity.getControlsOverlay() == null) return;

        updateSuitTile(R.id.bid_clubs_text, R.id.bid_clubs_icon, Suit.CLUBS);
        updateSuitTile(R.id.bid_diamonds_text, R.id.bid_diamonds_icon, Suit.DIAMONDS);
        updateSuitTile(R.id.bid_hearts_text, R.id.bid_hearts_icon, Suit.HEARTS);
        updateSuitTile(R.id.bid_spades_text, R.id.bid_spades_icon, Suit.SPADES);
    }

    private void updateSuitTile(int textId, int iconId, Suit suit) {
        TextView tv = activity.getControlsOverlay().findViewById(textId);
        ImageView iv = activity.getControlsOverlay().findViewById(iconId);
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

    private void updateSelectionInHistory() {
        if (lastHistory != null) {
            lastHistory.updateBiddingHistory(getCurrentSelectionString());
        }
    }

    private String getCurrentSelectionString() {
        if (selectedSuitViewId == View.NO_ID) return null;

        if (selectedSuitViewId == R.id.bid_clubs) return currentLevel + "C";
        if (selectedSuitViewId == R.id.bid_diamonds) return currentLevel + "D";
        if (selectedSuitViewId == R.id.bid_hearts) return currentLevel + "H";
        if (selectedSuitViewId == R.id.bid_spades) return currentLevel + "S";
        if (selectedSuitViewId == R.id.bid_nt) return currentLevel + "NT";
        if (selectedSuitViewId == R.id.btn_bid_double_toggle) {
            TextView tvDouble = activity.getControlsOverlay().findViewById(R.id.btn_bid_double_toggle);
            return (tvDouble != null) ? tvDouble.getText().toString() : "X";
        }
        return null;
    }

    private void handleBidOrPass(View v) {
        if (lastHistory == null) return;

        String selection = getCurrentSelectionString();
        String finalBid = (selection != null) ? selection : "Pass";

        // 1. Add to history
        lastHistory.getAuction().add(finalBid);

        // 2. Refresh history UI and scroll
        lastHistory.updateBiddingHistory(null, true);

        // 3. Reset local selection
        selectedSuitViewId = View.NO_ID;
        updateBiddingUI();

        // 4. Check for 3 passes (end of auction)
        checkEndOfAuction(v);

        // 5. Apply rules for next turn (if auction continues)
        applyAuctionRules(lastHistory);
    }

    private void checkEndOfAuction(View v) {
        List<String> auction = lastHistory.getAuction();
        if (auction.size() < 3) return;

        int passCount = 0;
        for (int i = auction.size() - 1; i >= 0; i--) {
            String b = auction.get(i);
            if ("Pass".equalsIgnoreCase(b)) {
                passCount++;
            } else if (!"-".equals(b)) {
                break;
            }
        }

        if (passCount >= 3) {
            onAuctionFinished(v);
        }
    }

    private void onAuctionFinished(View v) {
        activity.getGameController().setPlayerWinBidding(getWinBidding());
        v.post(() -> {
            activity.getGameController().startGame();
        });
    }

    private Player getWinBidding() {
        return activity.getGameController().getPlayers().get("West"); //todo
    }

}
