package com.example.bridge.ui.biddings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.bridge.R;
import com.example.bridge.bidding.Tools.BidRule;
import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.Constraint;
import com.example.bridge.bidding.Tools.Direction;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BiddingInfoFormatter {

    public static String getDescriptionsForRules(PositionState ps, List<BidRule> rules) {
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

    public static SpannableStringBuilder formatDescriptionText(Context context, String text) {
        if (text == null || text.trim().isEmpty()) return new SpannableStringBuilder("");

        String sortedText = sortDescriptionSuitClauses(context, text);
        String s = sortedText;

        s = s.replace("Spades", "♠")
             .replace("Hearts", "♥")
             .replace("Diamonds", "♦")
             .replace("Clubs", "♣")
             .replace("Piki", "♠")
             .replace("Kiery", "♥")
             .replace("Kara", "♦")
             .replace("Trefle", "♣");

        s = s.replaceAll("\\bC:", "♣:")
             .replaceAll("\\bD:", "♦:")
             .replaceAll("\\bH:", "♥:")
             .replaceAll("\\bS:", "♠:");

        for (int level = 1; level <= 7; level++) {
            s = s.replace(level + "C", level + "♣")
                 .replace(level + "D", level + "♦")
                 .replace(level + "H", level + "♥")
                 .replace(level + "S", level + "♠");
        }

        s = s.replace("♠\uFE0E", "♠")
             .replace("♥\uFE0E", "♥")
             .replace("♦\uFE0E", "♦")
             .replace("♣\uFE0E", "♣");

        SpannableStringBuilder ssb = new SpannableStringBuilder(s);

        if (context == null) return ssb;

        int iconSizePx = (int) (14 * context.getResources().getDisplayMetrics().density);

        for (int i = 0; i < ssb.length(); i++) {
            char ch = ssb.charAt(i);
            Suit suit = null;
            if (ch == '♠') suit = Suit.Spades;
            else if (ch == '♥') suit = Suit.Hearts;
            else if (ch == '♦') suit = Suit.Diamonds;
            else if (ch == '♣') suit = Suit.Clubs;

            if (suit != null) {
                com.example.bridge.model.Suit modelSuit = getModelSuit(suit);
                if (modelSuit != null) {
                    Drawable drawable = getSuitDrawable(context, modelSuit, iconSizePx);
                    if (drawable != null) {
                        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                        ssb.setSpan(imageSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        return ssb;
    }

    public static void appendSuitSymbol(Context context, SpannableStringBuilder ssb, Suit s, String suffix) {
        com.example.bridge.model.Suit modelSuit = getModelSuit(s);
        if (modelSuit == null) {
            ssb.append(s.toSymbol()).append(suffix);
            return;
        }

        if (context == null) {
            ssb.append(modelSuit.symbol).append(suffix);
            return;
        }

        Drawable drawable = ContextCompat.getDrawable(context, modelSuit.resId);
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable).mutate();
            int color = modelSuit.getColor(context);
            DrawableCompat.setTint(drawable, color);
            
            int size = (int) (14 * context.getResources().getDisplayMetrics().density);
            drawable.setBounds(0, 0, size, size);
            
            ssb.append(" ");
            ssb.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), ssb.length() - 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(modelSuit.symbol);
        }
        
        ssb.append(suffix);
    }

    public static void updatePlayerKnowledge(TextView textView, Direction d, BiddingState liveBiddingState, Context context) {
        if (textView == null || liveBiddingState == null || context == null) return;
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

        Suit[] orderedSuits = {
                Suit.Spades,
                Suit.Hearts,
                Suit.Diamonds,
                Suit.Clubs
        };

        for (Suit s : orderedSuits) {
            HandSummary.SuitSummary suitSum = summary.getSuits().get(s);
            if (suitSum != null) {
                Range shape = suitSum.getShape();
                if (shape != null && shape.getMin() > 0) {
                    SpannableStringBuilder suitSsb = new SpannableStringBuilder();
                    String suffix = (shape.getMin() == shape.getMax()) ? ": " + shape.getMin() : ": " + shape.getMin() + "+";
                    appendSuitSymbol(context, suitSsb, s, suffix);
                    parts.add(suitSsb);
                }
            }
        }

        Map<Suit, String> pairMaxShapes = new EnumMap<>(Suit.class);
        for (int i = 0; i < pos.getCallCount(); i++) {
            CallDetails details = pos.getCallDetails(i);
            if (details.getMatchedRules() != null) {
                for (BidRule rule : details.getMatchedRules()) {
                    if (rule.getConstraints() != null) {
                        for (Constraint constraint : rule.getConstraints()) {
                            if (constraint instanceof com.example.bridge.bidding.Constraints.PairMaxShape.PairShowsMaxShape) {
                                com.example.bridge.bidding.Constraints.PairMaxShape.PairShowsMaxShape pms = (com.example.bridge.bidding.Constraints.PairMaxShape.PairShowsMaxShape) constraint;
                                String desc = pms.describe(rule.getCall(), pos);
                                Suit s = null;
                                String text = desc;
                                if (desc.startsWith(Suit.Spades.toSymbol())) {
                                    s = Suit.Spades;
                                    text = desc.substring(1);
                                } else if (desc.startsWith(Suit.Hearts.toSymbol())) {
                                    s = Suit.Hearts;
                                    text = desc.substring(1);
                                } else if (desc.startsWith(Suit.Diamonds.toSymbol())) {
                                    s = Suit.Diamonds;
                                    text = desc.substring(1);
                                } else if (desc.startsWith(Suit.Clubs.toSymbol())) {
                                    s = Suit.Clubs;
                                    text = desc.substring(1);
                                }
                                
                                if (s != null) {
                                    pairMaxShapes.put(s, text);
                                } else {
                                    parts.add(desc);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        for (Suit s : orderedSuits) {
            if (pairMaxShapes.containsKey(s)) {
                SpannableStringBuilder suitSsb = new SpannableStringBuilder();
                appendSuitSymbol(context, suitSsb, s, pairMaxShapes.get(s));
                parts.add(suitSsb);
            }
        }

        Set<Integer> aces = summary.getCountAces();
        if (aces != null && !aces.isEmpty()) {
            String acesVal = aces.toString().replace("[", "").replace("]", "");
            parts.add(context.getString(R.string.public_knowledge_aces, acesVal));
        }
        Set<Integer> kings = summary.getCountKings();
        if (kings != null && !kings.isEmpty()) {
            String kingsVal = kings.toString().replace("[", "").replace("]", "");
            parts.add(context.getString(R.string.public_knowledge_kings, kingsVal));
        }

        if (!parts.isEmpty()) {
            SpannableStringBuilder finalSsb = new SpannableStringBuilder();
            String fullName = (d == Direction.N) ? context.getString(R.string.player_north) : context.getString(R.string.player_south);
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

    private static com.example.bridge.model.Suit getModelSuit(Suit suit) {
        if (suit == null) return null;
        switch (suit) {
            case Clubs: return com.example.bridge.model.Suit.CLUBS;
            case Diamonds: return com.example.bridge.model.Suit.DIAMONDS;
            case Hearts: return com.example.bridge.model.Suit.HEARTS;
            case Spades: return com.example.bridge.model.Suit.SPADES;
            default: return null;
        }
    }

    private static Drawable getSuitDrawable(Context context, com.example.bridge.model.Suit suit, int sizePx) {
        if (suit == null) return null;

        Drawable drawable = ContextCompat.getDrawable(context, suit.resId);
        if (drawable == null) return null;

        drawable = DrawableCompat.wrap(drawable).mutate();
        int color = suit.getColor(context);
        DrawableCompat.setTint(drawable, color);
        drawable.setBounds(0, 0, sizePx, sizePx);

        return drawable;
    }

    private static String sortDescriptionSuitClauses(Context context, String text) {
        if (text == null || text.trim().isEmpty()) return "";

        String[] lines = text.split("\n");
        List<String> formattedLines = new ArrayList<>();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) continue;

            String processed = processSingleLine(context, trimmedLine);
            if (!processed.isEmpty() && !formattedLines.contains(processed)) {
                formattedLines.add(processed);
            }
        }

        return String.join("\n", formattedLines);
    }

    private static String processSingleLine(Context context, String line) {
        String[] rawClauses = line.split(",\\s*");
        if (rawClauses.length == 0) return line;

        List<String> normalizedClauses = new ArrayList<>();
        for (String c : rawClauses) {
            String trimmed = c.trim();
            if (!trimmed.isEmpty()) {
                normalizedClauses.add(trimmed);
            }
        }

        class ClauseItem {
            final String text;
            final int originalIndex;
            final double categoryPriority;

            ClauseItem(String text, int originalIndex) {
                this.text = formatClauseText(context, text);
                this.originalIndex = originalIndex;
                this.categoryPriority = categorizeClause(text);
            }
        }

        List<ClauseItem> items = new ArrayList<>();
        for (int i = 0; i < normalizedClauses.size(); i++) {
            items.add(new ClauseItem(normalizedClauses.get(i), i));
        }

        items.sort((a, b) -> {
            if (Double.compare(a.categoryPriority, b.categoryPriority) != 0) {
                return Double.compare(a.categoryPriority, b.categoryPriority);
            }
            return Integer.compare(a.originalIndex, b.originalIndex);
        });

        List<String> sortedTextList = new ArrayList<>();
        for (ClauseItem item : items) {
            if (!sortedTextList.contains(item.text)) {
                sortedTextList.add(item.text);
            }
        }

        return String.join(", ", sortedTextList);
    }

    private static String formatClauseText(Context context, String clause) {
        if (clause == null) return "";
        String trimmed = clause.trim();
        String lower = trimmed.toLowerCase();

        if (trimmed.equalsIgnoreCase("Ask for Aces") || trimmed.equalsIgnoreCase("Ask about aces") || trimmed.equalsIgnoreCase("Pytanie o asy")) {
            return context != null ? context.getString(R.string.ask_for_aces) : "Pytanie o asy";
        }
        if (trimmed.equalsIgnoreCase("Ask for Kings") || trimmed.equalsIgnoreCase("Ask about kings") || trimmed.equalsIgnoreCase("Pytanie o króle")) {
            return context != null ? context.getString(R.string.ask_for_kings) : "Pytanie o króle";
        }

        if (lower.startsWith("aces:")) {
            String val = normalizeCommaRangeToDash(trimmed.substring(5).trim());
            String prefix = (context != null) ? context.getString(R.string.public_knowledge_aces, "").replace("%1$s", "").trim() : "Asy:";
            return prefix + " " + val;
        }
        if (lower.startsWith("kings:")) {
            String val = normalizeCommaRangeToDash(trimmed.substring(6).trim());
            String prefix = (context != null) ? context.getString(R.string.public_knowledge_kings, "").replace("%1$s", "").trim() : "Króle:";
            return prefix + " " + val;
        }

        if (isHcpClause(trimmed)) {
            return formatHcpClause(trimmed);
        }

        if (hasSuitSymbolOrName(trimmed)) {
            return formatSuitClause(trimmed);
        }

        return trimmed;
    }

    private static String formatSuitClause(String clause) {
        if (clause == null) return "";
        String trimmed = clause.trim();

        String symbol = "";
        if (trimmed.contains("♠") || trimmed.contains("Spades") || trimmed.contains("Piki") || trimmed.matches(".*\\bS[:\\d+].*")) symbol = "♠";
        else if (trimmed.contains("♥") || trimmed.contains("Hearts") || trimmed.contains("Kiery") || trimmed.matches(".*\\bH[:\\d+].*")) symbol = "♥";
        else if (trimmed.contains("♦") || trimmed.contains("Diamonds") || trimmed.contains("Kara") || trimmed.matches(".*\\bD[:\\d+].*")) symbol = "♦";
        else if (trimmed.contains("♣") || trimmed.contains("Clubs") || trimmed.contains("Trefle") || trimmed.matches(".*\\bC[:\\d+].*")) symbol = "♣";

        if (symbol.isEmpty()) return trimmed;

        if (trimmed.matches("^[♠♥♦♣]\\s*:\\s*.+")) {
            return trimmed.replaceAll("^[♠♥♦♣]\\s*:\\s*", symbol + ": ");
        }

        Matcher m = Pattern.compile("(\\d+(?:-\\d+|\\+)?)").matcher(trimmed);
        if (m.find() && m.group(1) != null) {
            String length = m.group(1);
            boolean isPair = trimmed.toLowerCase().contains("pair");
            return symbol + ": " + length + (isPair ? " (pair)" : "");
        }

        return symbol + ": " + trimmed.replaceAll("(?i)(spades|hearts|diamonds|clubs|piki|kiery|kara|trefle|[♠♥♦♣])", "").trim();
    }

    private static String normalizeCommaRangeToDash(String val) {
        if (val == null) return "";
        Matcher m = Pattern.compile("(\\d+)(?:,\\s*(\\d+))+").matcher(val);
        if (m.find()) {
            Matcher numMatcher = Pattern.compile("\\d+").matcher(val);
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            while (numMatcher.find()) {
                int num = Integer.parseInt(numMatcher.group());
                if (num < min) min = num;
                if (num > max) max = num;
            }
            if (min != Integer.MAX_VALUE && max != Integer.MIN_VALUE) {
                String rangeStr = (min == max) ? String.valueOf(min) : min + "-" + max;
                return val.replaceAll("(\\d+)(?:,\\s*(\\d+))+", rangeStr);
            }
        }
        return val;
    }

    private static boolean isHcpClause(String clause) {
        if (clause == null) return false;

        if (hasSuitSymbolOrName(clause)) {
            return false;
        }

        String lower = clause.toLowerCase();
        return lower.contains("hcp") || lower.contains("point") || lower.contains("punkty") || lower.contains("punkt")
                || clause.matches("^\\d+-(?:\\d+|\\+)$") || clause.matches("^\\d+\\+$");
    }

    private static boolean hasSuitSymbolOrName(String clause) {
        if (clause == null) return false;
        return clause.contains("♠") || clause.contains("♥") || clause.contains("♦") || clause.contains("♣")
                || clause.contains("Spades") || clause.contains("Hearts") || clause.contains("Diamonds") || clause.contains("Clubs")
                || clause.contains("Piki") || clause.contains("Kiery") || clause.contains("Kara") || clause.contains("Trefle");
    }

    private static String formatHcpClause(String clause) {
        if (clause == null) return "HCP: 0-40";

        String lower = clause.toLowerCase();

        Matcher rangeMatcher = Pattern.compile("(\\d+(?:-\\d+|\\+)?)").matcher(clause);
        String range = "";
        if (rangeMatcher.find()) {
            range = rangeMatcher.group(1);
        }

        if (range == null || range.isEmpty()) {
            range = clause.replaceAll("(?i)(pair|points|hcp|punkty|punkt)", "").trim();
        }

        if (lower.contains("pair point") || lower.contains("pair pts") || lower.contains("punkty pary")) {
            return "pair points: " + range;
        } else if (lower.contains("pair hcp") || lower.contains("hcp pary") || lower.contains("pair")) {
            return "pair HCP: " + range;
        } else {
            return "HCP: " + range;
        }
    }

    private static double categorizeClause(String clause) {
        String lower = clause.toLowerCase();

        if (isHcpClause(clause)) {
            if (lower.contains("pair point") || lower.contains("pair pts") || lower.contains("punkty pary")) {
                return 0.3;
            } else if (lower.contains("pair hcp") || lower.contains("hcp pary") || lower.contains("pair")) {
                return 0.2;
            } else {
                return 0.1;
            }
        }

        boolean isComplexOrPair = lower.contains("pair") || lower.contains("nofit") 
                || lower.contains("max") || lower.contains("min") || lower.contains("trump");

        double baseSuit = 0.0;
        if (clause.contains("♠") || clause.contains("Spades") || clause.contains("Piki") || clause.matches(".*\\bS[:\\d+].*")) baseSuit = 1.0;
        else if (clause.contains("♥") || clause.contains("Hearts") || clause.contains("Kiery") || clause.matches(".*\\bH[:\\d+].*")) baseSuit = 2.0;
        else if (clause.contains("♦") || clause.contains("Diamonds") || clause.contains("Kara") || clause.matches(".*\\bD[:\\d+].*")) baseSuit = 3.0;
        else if (clause.contains("♣") || clause.contains("Clubs") || clause.contains("Trefle") || clause.matches(".*\\bC[:\\d+].*")) baseSuit = 4.0;

        if (baseSuit > 0.0) {
            if (lower.contains("length") || lower.contains("at least") || lower.contains("co najmniej") || lower.matches(".*\\b[♠♥♦♣]\\s*:\\s*\\d+.*")) {
                return baseSuit;
            } else {
                return baseSuit + 0.5;
            }
        }
        
        if (isComplexOrPair) {
            return 6.0;
        }

        return 7.0;
    }
}
