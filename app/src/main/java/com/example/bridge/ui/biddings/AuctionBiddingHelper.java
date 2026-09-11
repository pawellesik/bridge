package com.example.bridge.ui.biddings;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bridge.R;
import com.example.bridge.bidding.Tools.BidRule;
import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.Direction;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.ui.game.GameActivity;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuctionBiddingHelper {

    private final GameActivity activity;

    public AuctionBiddingHelper(GameActivity activity) {
        this.activity = activity;
    }

    public void updateBiddingRulesView(BiddingState liveBiddingState) {
        View outerLayout = activity.findViewById(R.id.public_knowledge_container_layout);
        View container = activity.findViewById(R.id.bidding_rules_container);
        LinearLayout rulesContent = activity.findViewById(R.id.layout_rules_content);
        if (container == null || rulesContent == null) return;

        if (liveBiddingState == null || liveBiddingState.getContract().isAuctionComplete()) {
            container.setVisibility(View.GONE);
            return;
        }

        Direction nextToAct = liveBiddingState.getNextToAct().getDirection();
        if (nextToAct != Direction.S) {
            container.setVisibility(View.GONE);
            return;
        }

        PositionCalls choices = liveBiddingState.getCallChoices();
        if (choices == null || choices.isEmpty()) {
            container.setVisibility(View.GONE);
            return;
        }

        rulesContent.removeAllViews();
        PositionState ps = liveBiddingState.getNextToAct();

        List<Map.Entry<Call, String>> ruleHints = new ArrayList<>();
        for (Map.Entry<Call, CallDetails> entry : choices.entrySet()) {
            Call call = entry.getKey();
            CallDetails details = entry.getValue();
            if (call == null || details == null) continue;

            List<BidRule> showRules = new ArrayList<>();
            for (BidRule rule : details.getRules()) {
                if (com.example.bridge.bidding.Constraints.RuleShow.hasRuleShow(rule)) {
                    showRules.add(rule);
                }
            }

            if (showRules.isEmpty()) continue;

            String desc = BiddingInfoFormatter.getDescriptionsForRules(ps, showRules);
            if (desc == null || desc.trim().isEmpty()) {
                desc = details.getDescription(ps);
            }
            if (desc == null || desc.trim().isEmpty()) continue;

            ruleHints.add(new AbstractMap.SimpleEntry<>(call, desc));
        }

        int addedCount = 0;
        for (Map.Entry<Call, String> hint : ruleHints) {
            Call call = hint.getKey();
            String desc = hint.getValue();

            LinearLayout row = new LinearLayout(activity);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            row.setPadding(0, 6, 0, 6);

            TextView tvBid = new TextView(activity);
            tvBid.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 11);
            tvBid.setTypeface(null, android.graphics.Typeface.BOLD);
            tvBid.setMinWidth((int) (55 * activity.getResources().getDisplayMetrics().density));
            SpannableStringBuilder formattedBid = GameBiddingHistoryAdapter.formatBidSpannable(activity, call.toString());
            tvBid.setText(formattedBid, TextView.BufferType.SPANNABLE);

            TextView tvCriteria = new TextView(activity);
            tvCriteria.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 11);
            tvCriteria.setTextColor(0xFF1B2E1D);
            SpannableStringBuilder formattedDesc = GameBiddingHistoryAdapter.formatDescriptionText(activity, desc);
            tvCriteria.setText(formattedDesc, TextView.BufferType.SPANNABLE);

            row.addView(tvBid);
            row.addView(tvCriteria);

            rulesContent.addView(row);
            addedCount++;

            View divider = new View(activity);
            divider.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1));
            divider.setBackgroundColor(0x20000000);
            rulesContent.addView(divider);
        }

        if (addedCount > 0) {
            View spacer = new View(activity);
            spacer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, (int) (4 * activity.getResources().getDisplayMetrics().density)));
            rulesContent.addView(spacer);

            TextView tvFooterNote = new TextView(activity);
            tvFooterNote.setText(activity.getString(R.string.bidding_rules_footer_note));
            tvFooterNote.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 10.5f);
            tvFooterNote.setTextColor(0xFFD84315);
            tvFooterNote.setPadding(0, 2, 0, 2);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = (int) (2 * activity.getResources().getDisplayMetrics().density);
            tvFooterNote.setLayoutParams(params);

            rulesContent.addView(tvFooterNote);

            if (outerLayout != null) outerLayout.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
        } else {
            container.setVisibility(View.GONE);
        }
    }

    public void updateBiddingHintsView(BiddingState liveBiddingState) {
        View outerLayout = activity.findViewById(R.id.public_knowledge_container_layout);
        View container = activity.findViewById(R.id.bidding_hints_container);
        LinearLayout hintsContent = activity.findViewById(R.id.layout_hints_content);
        if (container == null || hintsContent == null) return;

        if (liveBiddingState == null || liveBiddingState.getContract().isAuctionComplete()) {
            container.setVisibility(View.GONE);
            return;
        }

        Direction nextToAct = liveBiddingState.getNextToAct().getDirection();
        if (nextToAct != Direction.S) {
            container.setVisibility(View.GONE);
            return;
        }

        PositionCalls choices = liveBiddingState.getCallChoices();

        hintsContent.removeAllViews();
        PositionState ps = liveBiddingState.getNextToAct();

        CallDetails bestDetails = choices != null ? choices.getBestCall() : null;
        Call bestCall = null;
        if (bestDetails != null && choices != null) {
            for (Map.Entry<Call, CallDetails> entry : choices.entrySet()) {
                if (entry.getValue() == bestDetails) {
                    bestCall = entry.getKey();
                    break;
                }
            }
        }

        boolean isPass = (bestCall == null || Call.PASS.equals(bestCall) || "Pass".equalsIgnoreCase(bestCall.toString()) || "P".equalsIgnoreCase(bestCall.toString()));
        if (isPass) {
            bestCall = Call.PASS;
            bestDetails = choices != null ? choices.get(Call.PASS) : null;
        }

        String desc = "";
        if (!isPass && bestDetails != null) {
            List<BidRule> matchedRules = getMatchingRulesForPlayer(ps, bestDetails);
            desc = BiddingInfoFormatter.getDescriptionsForRules(ps, matchedRules);
            
            if (desc == null || desc.trim().isEmpty()) {
                desc = bestDetails.getDescription(ps);
            }
            
            if (desc != null && desc.contains("\n")) {
                desc = desc.substring(0, desc.indexOf('\n'));
            }
        } else {
            desc = "";
        }

        LinearLayout row = new LinearLayout(activity);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setPadding(0, 6, 0, 6);

        TextView tvBid = new TextView(activity);
        tvBid.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 11);
        tvBid.setTypeface(null, android.graphics.Typeface.BOLD);
        tvBid.setMinWidth((int) (55 * activity.getResources().getDisplayMetrics().density));
        SpannableStringBuilder formattedBid = GameBiddingHistoryAdapter.formatBidSpannable(activity, bestCall.toString());
        tvBid.setText(formattedBid, TextView.BufferType.SPANNABLE);

        TextView tvCriteria = new TextView(activity);
        tvCriteria.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 11);
        tvCriteria.setTextColor(0xFF1B2E1D);
        SpannableStringBuilder formattedDesc = GameBiddingHistoryAdapter.formatDescriptionText(activity, desc);
        tvCriteria.setText(formattedDesc, TextView.BufferType.SPANNABLE);

        row.addView(tvBid);
        row.addView(tvCriteria);

        hintsContent.addView(row);

        if (outerLayout != null) outerLayout.setVisibility(View.VISIBLE);
        container.setVisibility(View.VISIBLE);
    }

    public void updatePublicKnowledgeView(BiddingState liveBiddingState) {
        View outerLayout = activity.findViewById(R.id.public_knowledge_container_layout);
        View container = activity.findViewById(R.id.public_knowledge_container);
        if (container == null) return;

        if (liveBiddingState == null) {
            if (outerLayout != null) outerLayout.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            return;
        }

        PositionState northPos = liveBiddingState.getPositions().get(Direction.N);
        PositionState southPos = liveBiddingState.getPositions().get(Direction.S);

        if ((northPos == null || northPos.getCallCount() == 0) &&
                (southPos == null || southPos.getCallCount() == 0)) {
            container.setVisibility(View.GONE);
            return;
        }

        if (outerLayout != null) outerLayout.setVisibility(View.VISIBLE);
        container.setVisibility(View.VISIBLE);

        TextView tvNorth = container.findViewById(R.id.tv_pk_north);
        TextView tvSouth = container.findViewById(R.id.tv_pk_south);
        TextView tvTrump = container.findViewById(R.id.tv_pk_trump);

        BiddingInfoFormatter.updatePlayerKnowledge(tvNorth, Direction.N, liveBiddingState, activity);
        BiddingInfoFormatter.updatePlayerKnowledge(tvSouth, Direction.S, liveBiddingState, activity);

        Suit nsTrump = (northPos != null) ? northPos.getPairState().getTrumpSuit() : null;
        if (nsTrump != null) {
            tvTrump.setVisibility(View.VISIBLE);
            SpannableStringBuilder ssb = new SpannableStringBuilder(activity.getString(R.string.agreed_ns_trump));
            BiddingInfoFormatter.appendSuitSymbol(activity, ssb, nsTrump, "");
            tvTrump.setText(ssb);
        } else {
            tvTrump.setVisibility(View.GONE);
        }
    }

    private List<BidRule> getMatchingRulesForPlayer(PositionState ps, CallDetails details) {
        List<BidRule> matched = new ArrayList<>(details.getMatchedRules());
        if (matched.isEmpty() && ps != null && ps.hasHand()) {
            for (BidRule rule : details.getRules()) {
                if (ps.privateHandConforms(rule)) {
                    matched.add(rule);
                }
            }
        }
        return matched;
    }
}
