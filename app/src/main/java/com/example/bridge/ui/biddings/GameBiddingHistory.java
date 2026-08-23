package com.example.bridge.ui.biddings;

import android.view.View;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.R;
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
        int count = 0;
        for (String b : auction) {
            if (!"-".equals(b)) {
                count++;
            }
        }
        return count;
    }

    public void updateBiddingHistory() {
        updateBiddingHistory(null, true);
    }

    public void updateBiddingHistory(String currentSelection) {
        updateBiddingHistory(currentSelection, false);
    }

    public void updateBiddingHistory(String currentSelection, boolean shouldScroll) {
        if (firstPlayer != null) {
            int offset = 0;
            switch (firstPlayer.getName()) {
                case "West": offset = 0; break;
                case "North": offset = 1; break;
                case "East": offset = 2; break;
                case "South": offset = 3; break;
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

        int lastRealBidIndex = -1;
        for (int i = auction.size() - 1; i >= 0; i--) {
            if (!"-".equals(auction.get(i))) {
                lastRealBidIndex = i;
                break;
            }
        }

        if (lastRealBidIndex != -1) {
            while (auction.size() > lastRealBidIndex + 1) {
                auction.remove(auction.size() - 1);
            }
        }

        if (gameActivity.getGameBiddingHistoryAdapter() != null) {
            gameActivity.getGameBiddingHistoryAdapter().setPreviewSelection(currentSelection);
            gameActivity.getGameBiddingHistoryAdapter().notifyDataSetChanged();

            if (this.rvBiddingHistory != null && shouldScroll) {
                rvBiddingHistory.post(() -> {
                    View scrollView = gameActivity.findViewById(R.id.bidding_scroll_view);
                    if (scrollView instanceof NestedScrollView) {
                        final NestedScrollView nsv = (NestedScrollView) scrollView;
                        // We scroll multiple times to ensure we catch layout changes as elements (like PK box) appear
                        nsv.post(() -> nsv.fullScroll(View.FOCUS_DOWN));
                        nsv.postDelayed(() -> nsv.fullScroll(View.FOCUS_DOWN), 50);
                        nsv.postDelayed(() -> nsv.fullScroll(View.FOCUS_DOWN), 150);
                        nsv.postDelayed(() -> nsv.fullScroll(View.FOCUS_DOWN), 400);
                    }
                });
            }
        }
    }
}
