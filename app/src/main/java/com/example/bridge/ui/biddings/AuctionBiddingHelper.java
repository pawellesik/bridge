package com.example.bridge.ui.biddings;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.bridge.R;
import com.example.bridge.bidding.Tools.BidRule;
import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.Direction;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.ui.game.GameActivity;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

            String desc = getDescriptionsForRules(ps, showRules);
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
            desc = getDescriptionsForRules(ps, matchedRules);
            
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

        updatePlayerKnowledge(tvNorth, Direction.N, liveBiddingState);
        updatePlayerKnowledge(tvSouth, Direction.S, liveBiddingState);

        com.example.bridge.bidding.Tools.Suit nsTrump = (northPos != null) ? northPos.getPairState().getTrumpSuit() : null;
        if (nsTrump != null) {
            tvTrump.setVisibility(View.VISIBLE);
            SpannableStringBuilder ssb = new SpannableStringBuilder(activity.getString(R.string.agreed_ns_trump));
            appendSuitSymbol(ssb, nsTrump, "");
            tvTrump.setText(ssb);
        } else {
            tvTrump.setVisibility(View.GONE);
        }
    }

    private void updatePlayerKnowledge(TextView textView, Direction d, BiddingState liveBiddingState) {
        PositionState pos = liveBiddingState.getPositions().get(d);
        if (pos == null) {
            textView.setVisibility(View.GONE);
            return;
        }
        HandSummary summary = pos.getPublicHandSummary();
        if (summary == null) {
            textView.setVisibility(View.GONE);
            return;
        }

        List<CharSequence> parts = new ArrayList<>();

        Range p = summary.getHighCardPoints();
        if (p != null) {
            if (p.getMin() == p.getMax()) {
                parts.add("HCP: " + p.getMin());
            } else {
                parts.add("HCP: " + p.getMin() + "-" + p.getMax());
            }
        }

        com.example.bridge.bidding.Tools.Suit[] orderedSuits = {
                com.example.bridge.bidding.Tools.Suit.Spades,
                com.example.bridge.bidding.Tools.Suit.Hearts,
                com.example.bridge.bidding.Tools.Suit.Diamonds,
                com.example.bridge.bidding.Tools.Suit.Clubs
        };

        for (com.example.bridge.bidding.Tools.Suit s : orderedSuits) {
            HandSummary.SuitSummary suitSum = summary.getSuits().get(s);
            if (suitSum != null) {
                Range shape = suitSum.getShape();
                if (shape != null && shape.getMin() > 0) {
                    SpannableStringBuilder suitSsb = new SpannableStringBuilder();
                    String suffix = (shape.getMin() == shape.getMax()) ? ": " + shape.getMin() : ": " + shape.getMin() + "+";
                    appendSuitSymbol(suitSsb, s, suffix);
                    parts.add(suitSsb);
                }
            }
        }

        Set<Integer> aces = summary.getCountAces();
        if (aces != null && !aces.isEmpty()) {
            String acesVal = aces.toString().replace("[", "").replace("]", "");
            parts.add(activity.getString(R.string.public_knowledge_aces, acesVal));
        }
        Set<Integer> kings = summary.getCountKings();
        if (kings != null && !kings.isEmpty()) {
            String kingsVal = kings.toString().replace("[", "").replace("]", "");
            parts.add(activity.getString(R.string.public_knowledge_kings, kingsVal));
        }

        if (!parts.isEmpty()) {
            SpannableStringBuilder finalSsb = new SpannableStringBuilder();
            String fullName = (d == Direction.N) ? activity.getString(R.string.player_north) : activity.getString(R.string.player_south);
            finalSsb.append(fullName).append(": ");

            for (int i = 0; i < parts.size(); i++) {
                if (i > 0) finalSsb.append(", ");
                finalSsb.append(parts.get(i));
            }

            textView.setVisibility(View.VISIBLE);
            textView.setText(finalSsb, TextView.BufferType.SPANNABLE);
        } else {
            textView.setVisibility(View.GONE);
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

    private String getDescriptionsForRules(PositionState ps, List<BidRule> rules) {
        if (rules == null || rules.isEmpty()) return "";
        List<String> ruleDescriptions = new ArrayList<>();
        for (BidRule rule : rules) {
            List<String> ruleDescs = rule.constraintDescriptions(ps);
            if (ruleDescs != null && !ruleDescs.isEmpty()) {
                String desc = String.join(", ", ruleDescs);
                if (!ruleDescriptions.contains(desc)) {
                    ruleDescriptions.add(desc);
                }
            }
        }
        return String.join("\n", ruleDescriptions);
    }

    private void appendSuitSymbol(SpannableStringBuilder ssb, com.example.bridge.bidding.Tools.Suit s, String suffix) {
        com.example.bridge.model.Suit modelSuit;
        switch (s) {
            case Clubs: modelSuit = com.example.bridge.model.Suit.CLUBS; break;
            case Diamonds: modelSuit = com.example.bridge.model.Suit.DIAMONDS; break;
            case Hearts: modelSuit = com.example.bridge.model.Suit.HEARTS; break;
            case Spades: modelSuit = com.example.bridge.model.Suit.SPADES; break;
            default: ssb.append(s.toSymbol()).append(suffix); return;
        }

        Drawable drawable = ContextCompat.getDrawable(activity, modelSuit.resId);
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable).mutate();
            int color = modelSuit.getColor(activity);
            DrawableCompat.setTint(drawable, color);
            
            int size = (int) (14 * activity.getResources().getDisplayMetrics().density);
            drawable.setBounds(0, 0, size, size);
            
            ssb.append(" ");
            ssb.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), ssb.length() - 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(modelSuit.symbol);
        }
        
        ssb.append(suffix);
    }
}
