package com.example.bridge;

import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Trick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameActivityHistory {

    private final GameActivity activity;
    private final GameController gameController;

    private final View resultsOverlay;
    private final TableLayout tableHistoryRes;
    private final Button btnAutoReplay;
    private final TextView tvSimInfo;

    private final TextView tvNorthRes, tvSouthRes, tvEastRes, tvWestRes;

    private List<Trick> playHistoryTrick = new ArrayList<>();
    private List<Trick> playAutoHistoryTrick = new ArrayList<>();
    private int simClaimCount = 0;
    private int currentSimTrickIndex;
    private boolean isShowingAutoHistory = false;
    private int userSnScore;

    public GameActivityHistory(GameActivity gameActivity, GameController gameController) {
        this.activity = gameActivity;
        this.gameController = gameController;

        this.resultsOverlay = gameActivity.findViewById(R.id.results_overlay);
        this.tableHistoryRes = gameActivity.findViewById(R.id.table_history_res);
        this.btnAutoReplay = gameActivity.findViewById(R.id.btn_auto_replay);
        this.tvSimInfo = gameActivity.findViewById(R.id.tv_trick_info);

        this.tvNorthRes = gameActivity.findViewById(R.id.tv_north_cards_res);
        this.tvSouthRes = gameActivity.findViewById(R.id.tv_south_cards_res);
        this.tvEastRes = gameActivity.findViewById(R.id.tv_east_cards_res);
        this.tvWestRes = gameActivity.findViewById(R.id.tv_west_cards_res);

        setupListeners();
    }

    private void setupListeners() {
        btnAutoReplay.setOnClickListener(v -> toggleAutoReplay());

        activity.findViewById(R.id.btn_first_trick).setOnClickListener(v -> jumpSimTrick(-1));
        activity.findViewById(R.id.btn_prev_trick).setOnClickListener(v -> changeSimTrick(-1));
        activity.findViewById(R.id.btn_next_trick).setOnClickListener(v -> changeSimTrick(1));
        activity.findViewById(R.id.btn_last_trick).setOnClickListener(v -> jumpSimTrick(1));
    }

    public void showResults(List<Trick> history, int claim, int snScore) {
        this.playHistoryTrick = history;
        this.simClaimCount = claim;
        this.userSnScore = snScore;
        this.currentSimTrickIndex = (history != null) ? history.size() : 0;
        this.isShowingAutoHistory = false;
        this.playAutoHistoryTrick = null;

        // Calculate auto score for initial button state
        Contract contract = gameController != null ? gameController.getCurrentContract() : new Contract(true);
        this.playAutoHistoryTrick = gameController != null ? gameController.calculateOptimalHistory(activity.getInitialPlayerHands(), contract) : new ArrayList<>();
        int autoSnScore = calculateSnScore(playAutoHistoryTrick);

        setBtnAutoReplayText(true, autoSnScore);
        displayHistory(history, claim);
        updateSimTrickUI(true);
        resultsOverlay.setVisibility(View.VISIBLE);
    }

    private int calculateSnScore(List<Trick> tricks) {
        int score = 0;
        if (tricks == null) return 0;
        for (Trick t : tricks) {
            String winner = t.getWinnerTrick();
            if ("North".equals(winner) || "South".equals(winner)) score++;
        }
        return score;
    }

    private void toggleAutoReplay() {
        if (activity.getInitialPlayerHands().isEmpty()) return;

        isShowingAutoHistory = !isShowingAutoHistory;
        List<Trick> activeHistory;
        int activeClaim;

        if (isShowingAutoHistory) {
            setBtnAutoReplayText(false, userSnScore);
            activeHistory = playAutoHistoryTrick;
            activeClaim = 0;
        } else {
            int autoSnScore = calculateSnScore(playAutoHistoryTrick);
            setBtnAutoReplayText(true, autoSnScore);
            activeHistory = playHistoryTrick;
            activeClaim = simClaimCount;
        }

        displayHistory(activeHistory, activeClaim);
        currentSimTrickIndex = (activeHistory != null) ? activeHistory.size() : 0;
        updateSimTrickUI(true);
    }

    private void setBtnAutoReplayText(boolean toAuto, int score) {
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

        tvSimInfo.setText(String.valueOf(currentSimTrickIndex));

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

        activity.updateSimulationScores(simSnScore, simWeScore);
        activity.onTableCleared(currentTrickMap);

        // Reset backgrounds
        tvNorthRes.setBackgroundResource(R.drawable.bright_green_frame_black);
        tvSouthRes.setBackgroundResource(R.drawable.bright_green_frame_black);
        tvEastRes.setBackgroundResource(R.drawable.bright_green_frame_black);
        tvWestRes.setBackgroundResource(R.drawable.bright_green_frame_black);

        // Highlight winner of current trick
        if (history != null && currentSimTrickIndex > 0) {
            Trick currentTrick = history.get(currentSimTrickIndex - 1);
            String winner = currentTrick.getWinnerTrick();
            if ("North".equals(winner))
                tvNorthRes.setBackgroundResource(R.drawable.bright_green_frame_yellow);
            else if ("South".equals(winner))
                tvSouthRes.setBackgroundResource(R.drawable.bright_green_frame_yellow);
            else if ("East".equals(winner))
                tvEastRes.setBackgroundResource(R.drawable.bright_green_frame_yellow);
            else if ("West".equals(winner))
                tvWestRes.setBackgroundResource(R.drawable.bright_green_frame_yellow);
        }

        if (currentTrickMap != null) {
            for (Map.Entry<String, Card> entry : currentTrickMap.entrySet()) {
                activity.showPlayedCardInSim(entry.getValue(), entry.getKey());
            }
        }

        tvNorthRes.setText(formatSimHand("North", previousTricksCards, currentTrickCards));
        tvSouthRes.setText(formatSimHand("South", previousTricksCards, currentTrickCards));
        tvEastRes.setText(formatSimHand("East", previousTricksCards, currentTrickCards));
        tvWestRes.setText(formatSimHand("West", previousTricksCards, currentTrickCards));

        updateHistoryHighlightAndScroll(shouldScroll);
    }

    private void updateHistoryHighlightAndScroll(boolean shouldScroll) {
        if (tableHistoryRes == null) return;
        int rowCount = tableHistoryRes.getChildCount();
        for (int i = 1; i < rowCount; i++) {
            View row = tableHistoryRes.getChildAt(i);
            if (i == currentSimTrickIndex) {
                row.setBackgroundResource(R.drawable.middle_green_frame_black);
                row.setForeground(androidx.appcompat.content.res.AppCompatResources.getDrawable(activity, R.drawable.middle_green_frame_black_border_only));
            } else {
                row.setForeground(null);
                if (i < currentSimTrickIndex) {
                    row.setBackgroundColor(Color.parseColor("#A5D6A7"));
                } else {
                    row.setBackgroundColor(Color.TRANSPARENT);
                }
            }
            // Ensure padding from drawable doesn't shift the content
            row.setPadding(0, 0, 0, 0);
        }

        if (shouldScroll && currentSimTrickIndex >= 0 && currentSimTrickIndex < rowCount) {
            View targetRow = tableHistoryRes.getChildAt(currentSimTrickIndex);
            if (targetRow != null) {
                activity.findViewById(R.id.scroll_history).post(() ->
                        ((androidx.core.widget.NestedScrollView) activity.findViewById(R.id.scroll_history))
                                .smoothScrollTo(0, targetRow.getTop()));
            }
        }
    }

    private void displayHistory(List<Trick> history, int claim) {
        if (tableHistoryRes == null) return;
        View headerRow = tableHistoryRes.getChildAt(0);
        if (headerRow != null) headerRow.setOnClickListener(v -> {
            currentSimTrickIndex = 0;
            updateSimTrickUI(false);
        });

        int childCount = tableHistoryRes.getChildCount();
        if (childCount > 1) tableHistoryRes.removeViews(1, childCount - 1);

        if (history == null) return;

        for (int i = 0; i < history.size(); i++) {
            final int trickNum = i + 1;
            Trick trick = history.get(i);
            TableRow row = new TableRow(activity);
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

                TextView tv = new TextView(activity);
                ImageView iv = new ImageView(activity);

                if (trickCards[c] != null) {
                    tv.setText(trickCards[c].getRank().display);
                    tv.setTextColor(Color.BLACK);
                    tv.setTypeface(null, android.graphics.Typeface.BOLD);

                    iv.setImageResource(trickCards[c].getSuit().resId);
                    iv.setColorFilter(trickCards[c].getSuit().color);
                    
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

                if (c == winnerCol)
                    cellLayout.setBackgroundResource(R.drawable.green_win_in_row);

                // Set padding AFTER background
                cellLayout.setPadding(8, 16, 8, 16);
                row.addView(cellLayout);
            }
            tableHistoryRes.addView(row);
        }

        if (claim > 0) {
            TextView claimTv = new TextView(activity);
            claimTv.setText(activity.getString(R.string.claimed_tricks, claim));
            claimTv.setTextColor(Color.RED);
            claimTv.setPadding(16, 8, 16, 8);
            claimTv.setOnClickListener(v -> {
                currentSimTrickIndex = history.size();
                updateSimTrickUI(false);
            });
            tableHistoryRes.addView(claimTv);
        }
    }

    public Spanned formatSimHand(String playerName, List<Card> previousTricks, List<Card> currentTrick) {
        String html = formatHandToHtmlForSim(activity.getInitialPlayerHands().get(playerName), previousTricks, currentTrick);

        android.text.SpannableString spanned = new android.text.SpannableString(
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, source -> {
                    int resId = 0;
                    int color = 0;
                    switch (source) {
                        case "spades":
                            resId = R.drawable.spades;
                            color = com.example.bridge.model.Suit.SPADES.color;
                            break;
                        case "heart":
                            resId = R.drawable.heart;
                            color = com.example.bridge.model.Suit.HEARTS.color;
                            break;
                        case "diamonds":
                            resId = R.drawable.diamonds;
                            color = com.example.bridge.model.Suit.DIAMONDS.color;
                            break;
                        case "clubs":
                            resId = R.drawable.clubs;
                            color = com.example.bridge.model.Suit.CLUBS.color;
                            break;
                    }

                    if (resId != 0) {
                        android.graphics.drawable.Drawable d = androidx.core.content.res.ResourcesCompat.getDrawable(
                                activity.getResources(), resId, null);
                        if (d != null) {
                            d.mutate();
                            d.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
                            float textSizePx = tvNorthRes.getTextSize();
                            int size = (int) textSizePx;
                            d.setBounds(0, 0, size, size);
                            return d;
                        }
                    }
                    return null;
                }, null)
        );

        // Zamień wszystkie ImageSpan na ALIGN_CENTER
        android.text.style.ImageSpan[] spans = spanned.getSpans(0, spanned.length(), android.text.style.ImageSpan.class);
        android.text.SpannableStringBuilder result = new android.text.SpannableStringBuilder(spanned);
        for (android.text.style.ImageSpan span : spans) {
            int start = spanned.getSpanStart(span);
            int end = spanned.getSpanEnd(span);
            android.text.style.ImageSpan centered = new android.text.style.ImageSpan(
                    span.getDrawable(), android.text.style.DynamicDrawableSpan.ALIGN_CENTER);
            result.removeSpan(span);
            result.setSpan(centered, start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return result;
    }

    public String formatHandToHtmlForSim(List<Card> hand, List<Card> previousTricksCards, List<Card> currentTrickCards) {
        StringBuilder sb = new StringBuilder();
        com.example.bridge.model.Suit[] suits = {
                com.example.bridge.model.Suit.SPADES,
                com.example.bridge.model.Suit.HEARTS,
                com.example.bridge.model.Suit.DIAMONDS,
                com.example.bridge.model.Suit.CLUBS
        };

        for (int i = 0; i < suits.length; i++) {
            com.example.bridge.model.Suit suit = suits[i];

            String suitImageName;
            switch (suit) {
                case SPADES:
                    suitImageName = "spades";
                    break;
                case HEARTS:
                    suitImageName = "heart";
                    break;
                case DIAMONDS:
                    suitImageName = "diamonds";
                    break;
                case CLUBS:
                    suitImageName = "clubs";
                    break;
                default:
                    suitImageName = "spades";
            }

            sb.append("<img src='").append(suitImageName).append("'/>&nbsp;");

            sb.append("<b>");
            boolean first = true;
            for (Card card : hand) {
                if (card.getSuit() == suit) {
                    if (!first) sb.append("&nbsp;");

                    String cardColor = "black"; // Nie rzucone
                    if (previousTricksCards != null && previousTricksCards.contains(card)) {
                        cardColor = "#999999"; // Rzucone w poprzednich lewach (szare)
                    } else if (currentTrickCards != null && currentTrickCards.contains(card)) {
                        cardColor = "red"; // Obecnie rzucona (czerwona)
                    }

                    sb.append("<font color='").append(cardColor).append("'>")
                            .append(card.getRank().display).append("</font>");
                    first = false;
                }
            }
            sb.append("</b>");
            if (i < suits.length - 1) {
                sb.append("<br/>");
            }
        }
        return sb.toString();
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
