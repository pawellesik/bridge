package com.example.bridge.ui.history;

import android.graphics.Color;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bridge.R;
import com.example.bridge.model.Card;
import com.example.bridge.model.Trick;
import com.example.bridge.ui.game.GameActivity;
import com.example.bridge.ui.game.GameTop;
import com.example.bridge.ui.game.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameActivityHistory {

    private final GameActivity activity;
    private final GameController gameController;
    private GameTop gameTop;
    private final View resultsOverlay;
    private final LinearLayout tableHistoryRes;
    private final LinearLayout tableHistoryHeader;
    private final Button btnAutoReplay;
    private final TextView tvSimInfo;

    private final TextView tvNorthRes, tvSouthRes, tvEastRes, tvWestRes;

    private List<Trick> playHistoryTrick = new ArrayList<>();
    private List<Trick> playAutoHistoryTrick = new ArrayList<>();
    private int simClaimCount = 0;
    private int currentSimTrickIndex;
    private boolean isShowingAutoHistory = false;
    private int userSnScore;
    private int savedAutoSnScore;



    public GameActivityHistory(GameActivity gameActivity, GameController gameController, GameTop gameTop) {
        this.activity = gameActivity;
        this.gameController = gameController;
        this.gameTop = gameTop;

        this.resultsOverlay = gameActivity.findViewById(R.id.results_overlay);
        this.tableHistoryRes = gameActivity.findViewById(R.id.table_history_res);
        this.tableHistoryHeader = gameActivity.findViewById(R.id.table_history_header);
        this.btnAutoReplay = gameActivity.findViewById(R.id.btn_auto_replay);
        this.tvSimInfo = gameActivity.findViewById(R.id.tv_trick_info);

        this.tvNorthRes = gameActivity.findViewById(R.id.tv_north_cards_res);
        this.tvSouthRes = gameActivity.findViewById(R.id.tv_south_cards_res);
        this.tvEastRes = gameActivity.findViewById(R.id.tv_east_cards_res);
        this.tvWestRes = gameActivity.findViewById(R.id.tv_west_cards_res);

        setupListeners();
    }

    private void setupListeners() {
        //if (btnAutoReplay != null) btnAutoReplay.setOnClickListener(v -> toggleAutoReplay());

        View btnFirst = activity.findViewById(R.id.btn_first_trick);
        if (btnFirst != null) btnFirst.setOnClickListener(v -> jumpSimTrick(-1));
        
        View btnPrev = activity.findViewById(R.id.btn_prev_trick);
        if (btnPrev != null) btnPrev.setOnClickListener(v -> changeSimTrick(-1));
        
        View btnNext = activity.findViewById(R.id.btn_next_trick);
        if (btnNext != null) btnNext.setOnClickListener(v -> changeSimTrick(1));
        
        View btnLast = activity.findViewById(R.id.btn_last_trick);
        if (btnLast != null) btnLast.setOnClickListener(v -> jumpSimTrick(1));
    }

    public void showResults(List<Trick> history, int claim, int snScore, int autoSnScore, List<Trick> autoHistory) {
        this.playHistoryTrick = history;
        this.simClaimCount = claim;
        this.userSnScore = snScore;
        this.savedAutoSnScore = autoSnScore;
        this.currentSimTrickIndex = (history != null) ? history.size() : 0;
        this.isShowingAutoHistory = false;
        this.playAutoHistoryTrick = autoHistory;

        setBtnAutoReplayText(true, autoSnScore);
        displayHistory(history, claim);
        updateSimTrickUI(true);
        if (resultsOverlay != null) resultsOverlay.setVisibility(View.VISIBLE);
    }

    private void setBtnAutoReplayText(boolean toAuto, int score) {
        if (btnAutoReplay == null) return;
        if (toAuto) {
            btnAutoReplay.setText(activity.getString(R.string.auto_play_deal_format, score));
        } else {
            btnAutoReplay.setText(activity.getString(R.string.back_to_my_play_format, score));
        }
    }

    public void changeSimTrick(int i) {
        List<Trick> history = isShowingAutoHistory ? playAutoHistoryTrick : playHistoryTrick;
        if (i < 0 && currentSimTrickIndex > 0) {
            currentSimTrickIndex -= 1;
        } else if (i > 0 && currentSimTrickIndex < (history != null ? history.size() : 0)) {
            currentSimTrickIndex += 1;
        }
        updateSimTrickUI(true);
    }

    public void jumpSimTrick(int direction) {
        List<Trick> history = isShowingAutoHistory ? playAutoHistoryTrick : playHistoryTrick;
        if (direction < 0) {
            currentSimTrickIndex = 0;
        } else {
            currentSimTrickIndex = (history != null ? history.size() : 0);
        }
        updateSimTrickUI(true);
    }

    private void updateSimTrickUI(boolean shouldScroll) {
        List<Trick> history = isShowingAutoHistory ? playAutoHistoryTrick : playHistoryTrick;
        int claim = isShowingAutoHistory ? 0 : simClaimCount;

        if (tvSimInfo != null) tvSimInfo.setText(String.valueOf(currentSimTrickIndex));

        List<Card> previousTricksCards = new ArrayList<>();
        List<Card> currentTrickCards = null;
        Map<String, Card> currentTrickMap = null;

        int simSnScore = 0;
        int simWeScore = 0;

        if (history != null) {
            for (int trickIdx = 0; trickIdx < currentSimTrickIndex; trickIdx++) {
                Trick trick = history.get(trickIdx);
                String winner = trick.getWinnerTrick();
                if ("North".equals(winner) || "South".equals(winner)) simSnScore++;
                else if ("East".equals(winner) || "West".equals(winner)) simWeScore++;

                if (trickIdx < currentSimTrickIndex - 1) {
                    previousTricksCards.addAll(trick.getCardsOnTable());
                } else {
                    currentTrickMap = trick.getCardsOnTableMap();
                    currentTrickCards = trick.getCardsOnTable();
                }
            }
        }

        if (history != null && currentSimTrickIndex == history.size() && claim > 0) {
            simSnScore += claim;
        }

        gameTop.updateScores(simSnScore, simWeScore);
        activity.onUpdateLastTrickInTop(currentTrickMap);

        // Reset backgrounds
        if (tvNorthRes != null) tvNorthRes.setBackgroundResource(R.drawable.bright_green_frame_black);
        if (tvSouthRes != null) tvSouthRes.setBackgroundResource(R.drawable.bright_green_frame_black);
        if (tvEastRes != null) tvEastRes.setBackgroundResource(R.drawable.bright_green_frame_black);
        if (tvWestRes != null) tvWestRes.setBackgroundResource(R.drawable.bright_green_frame_black);

        // Highlight winner of current trick
        if (history != null && currentSimTrickIndex > 0) {
            Trick currentTrick = history.get(currentSimTrickIndex - 1);
            String winner = currentTrick.getWinnerTrick();
            if ("North".equals(winner) && tvNorthRes != null)
                tvNorthRes.setBackgroundResource(R.drawable.bright_green_frame_yellow);
            else if ("South".equals(winner) && tvSouthRes != null)
                tvSouthRes.setBackgroundResource(R.drawable.bright_green_frame_yellow);
            else if ("East".equals(winner) && tvEastRes != null)
                tvEastRes.setBackgroundResource(R.drawable.bright_green_frame_yellow);
            else if ("West".equals(winner) && tvWestRes != null)
                tvWestRes.setBackgroundResource(R.drawable.bright_green_frame_yellow);
        }

        if (currentTrickMap != null) {
            for (Map.Entry<String, Card> entry : currentTrickMap.entrySet()) {
                //activity.showPlayedCardInSim(entry.getValue(), entry.getKey());todo
            }
        }

        updateHistoryHighlightAndScroll(shouldScroll);
    }

    private void updateHistoryHighlightAndScroll(boolean shouldScroll) {
        if (tableHistoryRes == null) return;
        int rowCount = tableHistoryRes.getChildCount();
        for (int i = 0; i < rowCount; i++) {
            View row = tableHistoryRes.getChildAt(i);
            // i is trick index (0-12), currentSimTrickIndex is 1-based
            if (i + 1 == currentSimTrickIndex) {
                row.setBackgroundResource(R.drawable.middle_green_frame_black);
                row.setForeground(androidx.appcompat.content.res.AppCompatResources.getDrawable(activity, R.drawable.middle_green_frame_black_border_only));
            } else {
                row.setForeground(null);
                if (i + 1 < currentSimTrickIndex) {
                    row.setBackgroundColor(Color.parseColor("#E2F5E3"));
                } else {
                    row.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }
            }
            row.setPadding(0, 0, 0, 0);
        }

        if (shouldScroll && currentSimTrickIndex > 0 && currentSimTrickIndex <= rowCount) {
            View targetRow = tableHistoryRes.getChildAt(currentSimTrickIndex - 1);
            if (targetRow != null) {
                View scrollView = activity.findViewById(R.id.scroll_history);
                if (scrollView != null) {
                    scrollView.post(() ->
                            ((androidx.core.widget.NestedScrollView) scrollView)
                                    .smoothScrollTo(0, targetRow.getTop()));
                }
            }
        }
    }

    private void displayHistory(List<Trick> history, int claim) {
        if (tableHistoryRes == null || tableHistoryHeader == null) return;
        
        // Header alignment with logic
        tableHistoryHeader.setOnClickListener(v -> {
            currentSimTrickIndex = 0;
            updateSimTrickUI(false);
        });

        tableHistoryRes.removeAllViews();

        if (history == null) return;

        for (int i = 0; i < history.size(); i++) {
            final int trickNum = i + 1;
            Trick trick = history.get(i);
            
            LinearLayout row = new LinearLayout(activity);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setDividerDrawable(androidx.core.content.ContextCompat.getDrawable(activity, R.drawable.history_divider));
            row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            row.setOnClickListener(v -> {
                currentSimTrickIndex = trickNum;
                updateSimTrickUI(false);
            });

            com.example.bridge.model.Card[] trickCards = new com.example.bridge.model.Card[4];
            int winnerCol = getPlayerColumn(trick.getWinnerTrick());

            for (Map.Entry<String, Card> entry : trick.getCardsOnTableMap().entrySet()) {
                int col = getPlayerColumn(entry.getKey());
                if (col != -1) {
                    trickCards[col] = entry.getValue();
                }
            }

            for (int c = 0; c < 4; c++) {
                LinearLayout cellLayout = new LinearLayout(activity);
                cellLayout.setOrientation(LinearLayout.HORIZONTAL);
                cellLayout.setGravity(Gravity.CENTER);
                
                // Use weight=1 to match header exactly
                LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                cellLayout.setLayoutParams(cellParams);

                TextView tv = new TextView(activity);
                ImageView iv = new ImageView(activity);

                if (trickCards[c] != null) {
                    tv.setText(trickCards[c].getRank().display);
                    tv.setTextColor(Color.BLACK);
                    tv.setTypeface(null, android.graphics.Typeface.BOLD);

                    iv.setImageResource(trickCards[c].getSuit().resId);
                    iv.setColorFilter(trickCards[c].getSuit().getColor(activity));
                    
                    int iconSize = (int) (14 * activity.getResources().getDisplayMetrics().density);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(iconSize, iconSize);
                    lp.leftMargin = (int) (4 * activity.getResources().getDisplayMetrics().density);
                    iv.setLayoutParams(lp);
                } else {
                    tv.setText("-");
                    tv.setTextColor(Color.BLACK);
                    tv.setTypeface(null, android.graphics.Typeface.BOLD);
                    iv.setVisibility(View.GONE);
                }

                cellLayout.addView(tv);
                cellLayout.addView(iv);

                if (c == winnerCol) {
                    cellLayout.setBackgroundResource(R.drawable.green_win_in_row);
                }

                cellLayout.setPadding(8, 16, 8, 16);
                row.addView(cellLayout);
            }
            tableHistoryRes.addView(row);
        }

        if (claim > 0) {
            TextView claimTv = new TextView(activity);
            claimTv.setPadding(16, 8, 16, 8);
            claimTv.setTypeface(null, android.graphics.Typeface.BOLD);
            
            String label = activity.getString(R.string.claimed_tricks_label) + " ";
            String countText = activity.getString(claim == 1 ? R.string.claimed_trick_count : R.string.claimed_tricks_count, claim);
            
            android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder(label + countText);
            ssb.setSpan(new android.text.style.ForegroundColorSpan(Color.RED), 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new android.text.style.ForegroundColorSpan(Color.BLACK), label.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            claimTv.setText(ssb);
            claimTv.setOnClickListener(v -> {
                currentSimTrickIndex = history.size();
                updateSimTrickUI(false);
            });
            tableHistoryRes.addView(claimTv);
        }
    }





    public int getPlayerColumn(String name) {
        if ("West".equals(name)) return 0;
        if ("North".equals(name)) return 1;
        if ("East".equals(name)) return 2;
        if ("South".equals(name)) return 3;
        return -1;
    }

    public void hide() {
        if (resultsOverlay != null) resultsOverlay.setVisibility(View.GONE);
    }

    public boolean isVisible() {
        return resultsOverlay != null && resultsOverlay.getVisibility() == View.VISIBLE;
    }
}
