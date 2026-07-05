package com.example.bridge.ui.biddings;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.model.Player;
import com.example.bridge.ui.game.GameActivity;

import java.util.ArrayList;
import java.util.List;

public class GameBiddingHistory {
    private final List<String> auction = new ArrayList<>();
    private Player firstPlayer;
    private RecyclerView rvBiddingHistory;
    private GameActivity gameActivity;

    public GameBiddingHistory(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        this.rvBiddingHistory = gameActivity.getRvBiddingHistory();
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public List<String> getAuction() {
        return auction;
    }

    public int getCountAuction() {
        int count=0;
        for (String auction: auction){
            if (!auction.equals("-")){
                count++;
            }
        }
        return count;
    }

    public void addFakeAuction() {
        auction.clear();
        auction.add("Pass");
        /*auction.add("1C");
        auction.add("Pass");
        auction.add("1H");
        auction.add("Pass");
        auction.add("1S");
        auction.add("Pass");
        auction.add("2NT");
        auction.add("Pass");
        auction.add("3C");
        auction.add("Pass");
        auction.add("3H");
        auction.add("Pass");
        auction.add("2NT");
        auction.add("X");*/

    }

    public void updateBiddingHistory() {
        updateBiddingHistory(null, true);
    }

    public void updateBiddingHistory(String currentSelection) {
        updateBiddingHistory(currentSelection, false);
    }

    public void updateBiddingHistory(String currentSelection, boolean shouldScroll) {
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

        // 2. Remove trailing dashes
        while (!auction.isEmpty() && "-".equals(auction.get(auction.size() - 1))) {
            auction.remove(auction.size() - 1);
        }

        if (gameActivity.getGameBiddingHistoryAdapter() != null) {
            // Update adapter's preview and data
            gameActivity.getGameBiddingHistoryAdapter().setPreviewSelection(currentSelection);
            gameActivity.getGameBiddingHistoryAdapter().notifyDataSetChanged();

            if (this.rvBiddingHistory != null) {
                rvBiddingHistory.post(() -> {
                    float density = gameActivity.getResources().getDisplayMetrics().density;
                    // The bidding controls overlap the bottom of the history by about 36dp (the tabs).
                    // We set a padding so the bids don't get stuck behind the controls.
                    int paddingBottom = (int) (40 * density);

                    rvBiddingHistory.setPadding(0, 0, 0, paddingBottom);
                    rvBiddingHistory.setClipToPadding(false);

                    // Scroll only if requested (e.g. after a new bid is added)
                    if (shouldScroll && rvBiddingHistory.getHeight() > 0) {
                        int rowHeight = (int) (40 * density);
                        int totalRows = (auction.size() + 4) / 4; // include the preview row
                        int totalContentHeight = totalRows * rowHeight;
                        int visibleArea = rvBiddingHistory.getHeight() - paddingBottom;

                        if (totalContentHeight > visibleArea) {
                            rvBiddingHistory.smoothScrollToPosition(auction.size());
                        }
                    }
                });
            }
        }
    }

}
