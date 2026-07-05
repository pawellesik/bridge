package com.example.bridge.ui.biddings;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.model.Player;
import com.example.bridge.ui.game.GameActivity;

import java.util.ArrayList;
import java.util.List;

public class GameBiddingHistory {
    private final List<String> auction = new ArrayList<>();
    private Player firstPlayer;
    private RecyclerView rvBiddingHistory;
    private View biddingControlsOverlay;
    private GameActivity gameActivity;
    private boolean showYellowTile = false;

    public GameBiddingHistory(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        this.rvBiddingHistory = gameActivity.getRvBiddingHistory();
        this.biddingControlsOverlay = gameActivity.getBiddingControlsOverlay();
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public List<String> getAuction() {
        return auction;
    }

    public void addFakeAuction() {
        auction.clear();
        auction.add("Pass");
        auction.add("1C");
        auction.add("Pass");
        auction.add("1H");
        auction.add("Pass");
        auction.add("1S");
        auction.add("Pass");
        auction.add("2NT");
        auction.add("Pass");
        auction.add("3C");
        /*auction.add("Pass");
        auction.add("3H");
        auction.add("Pass");
        auction.add("2NT");
        auction.add("X");*/

    }

    public void updateBiddingHistory() {
        // 1. Align with the first bidder (West, North, East, South)
        if (firstPlayer != null) {
            int offset = 0;
            switch (firstPlayer.getName()) {
                case "West":
                    offset = 0;
                    break;
                case "North":
                    offset = 1;
                    break;
                case "East":
                    offset = 2;
                    break;
                case "South":
                    offset = 3;
                    break;
            }

            int currentLeading = 0;
            while (currentLeading < auction.size() && "-".equals(auction.get(currentLeading))) {
                currentLeading++;
            }

            if (currentLeading < offset) {
                for (int i = 0; i < (offset - currentLeading); i++) auction.add(0, "-");
            } else if (currentLeading > offset) {
                for (int i = 0; i < (currentLeading - offset); i++) auction.remove(0);
            }
        }

        // 2. Remove trailing dashes and add one empty placeholder for the "next" bid
        while (!auction.isEmpty() && "-".equals(auction.get(auction.size() - 1))) {
            auction.remove(auction.size() - 1);
        }
        auction.add(""); // Placeholder for the active tile

        if (gameActivity.getGameBiddingHistoryAdapter() != null) {
            gameActivity.getGameBiddingHistoryAdapter().notifyDataSetChanged();

            if (this.rvBiddingHistory != null && !auction.isEmpty()) {
                rvBiddingHistory.post(() -> {
                    // Extra padding so we can scroll the last row even higher
                    int controlsHeight = (biddingControlsOverlay != null) ? biddingControlsOverlay.getHeight() : 0;
                    float density = gameActivity.getResources().getDisplayMetrics().density;
                    int extraSpace = (int) (80 * density); // Two rows (40dp each) of extra space

                    rvBiddingHistory.setPadding(0, 0, 0, controlsHeight + extraSpace);
                    rvBiddingHistory.setClipToPadding(false);

                    // Scroll to the placeholder
                    rvBiddingHistory.smoothScrollToPosition(Math.max(0, auction.size() - 1));
                });
            }
        }
    }

}
