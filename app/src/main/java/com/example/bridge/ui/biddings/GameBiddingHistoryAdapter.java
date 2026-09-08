package com.example.bridge.ui.biddings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.R;
import com.example.bridge.model.Suit;

import java.util.ArrayList;
import java.util.List;

public class GameBiddingHistoryAdapter extends RecyclerView.Adapter<GameBiddingHistoryAdapter.ViewHolder> {

    private final List<String> bids;
    private List<String> descriptions = new ArrayList<>();
    private boolean highlightLast = false;
    private String previewSelection = "";
    private final int layoutId;
    private boolean showPreviewTile = true;

    private static PopupWindow activePopupWindow = null;

    public GameBiddingHistoryAdapter(List<String> bids) {
        this(bids, R.layout.item_bid_tile);
    }

    public GameBiddingHistoryAdapter(List<String> bids, int layoutId) {
        this.bids = bids;
        this.layoutId = layoutId;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = (descriptions != null) ? descriptions : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setPreviewSelection(String preview) {
        this.previewSelection = (preview != null) ? preview : "";
        notifyDataSetChanged();
    }

    public void setHighlightLast(boolean highlight) {
        this.highlightLast = highlight;
        notifyDataSetChanged();
    }

    public void setShowPreviewTile(boolean show) {
        this.showPreviewTile = show;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < bids.size()) {
            String bid = bids.get(position);
            holder.bind(bid, false, false, layoutId);

            String desc = (position < descriptions.size()) ? descriptions.get(position) : null;
            if (desc != null && !desc.trim().isEmpty() && !"-".equals(bid)) {
                holder.showQuestionIcon(true);
                holder.itemView.setOnClickListener(v -> showDescriptionTooltip(v, bid, desc));
            } else {
                holder.showQuestionIcon(false);
                holder.itemView.setOnClickListener(null);
            }
        } else {
            // Kafelek podglądu (następny ruch)
            boolean isSouthColumn = (position % 4 == 3);
            holder.bind(previewSelection, true, highlightLast && isSouthColumn, layoutId);
            holder.showQuestionIcon(false);
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return bids.size() + (showPreviewTile ? 1 : 0);
    }

    private static void showDescriptionTooltip(View anchorView, String bid, String description) {
        if (activePopupWindow != null) {
            try {
                activePopupWindow.dismiss();
            } catch (Exception ignored) {}
            activePopupWindow = null;
        }

        if (description == null || description.trim().isEmpty()) return;

        Context context = anchorView.getContext();
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_bid_description, null);

        TextView tvLabel = popupView.findViewById(R.id.tv_popup_bid_label);
        TextView tvLevel = popupView.findViewById(R.id.tv_popup_bid_level);
        ImageView ivSuit = popupView.findViewById(R.id.iv_popup_bid_suit);
        TextView tvText = popupView.findViewById(R.id.tv_popup_bid_description);

        if (tvLabel != null) {
            tvLabel.setText(context.getString(R.string.explanation_label));
        }

        if (tvLevel != null && ivSuit != null) {
            bindBidHeader(context, bid, tvLevel, ivSuit);
        }

        if (tvText != null) {
            tvText.setText(formatDescriptionText(context, description), TextView.BufferType.SPANNABLE);
        }

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setElevation(0f);

        activePopupWindow = popupWindow;

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popupView.getMeasuredWidth();

        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorCenterX = location[0] + anchorView.getWidth() / 2;

        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int popupX = anchorCenterX - popupWidth / 2;
        popupX = Math.max(16, Math.min(popupX, screenWidth - popupWidth - 16));

        ImageView ivArrow = popupView.findViewById(R.id.iv_popup_arrow);
        if (ivArrow != null) {
            int arrowWidth = (int) (16 * context.getResources().getDisplayMetrics().density);
            int arrowOffsetInPopup = anchorCenterX - popupX - (arrowWidth / 2);
            arrowOffsetInPopup = Math.max(16, Math.min(arrowOffsetInPopup, popupWidth - arrowWidth - 16));

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ivArrow.getLayoutParams();
            if (params != null) {
                params.leftMargin = arrowOffsetInPopup;
                ivArrow.setLayoutParams(params);
            }
        }

        int y = location[1] + anchorView.getHeight() - 2;

        popupWindow.showAtLocation(anchorView, android.view.Gravity.NO_GRAVITY, popupX, y);
    }

    private static void bindBidHeader(Context context, String bid, TextView tvLevel, ImageView ivSuit) {
        if (bid == null || bid.isEmpty()) return;

        ivSuit.setVisibility(View.GONE);

        if (bid.equalsIgnoreCase("Pass") || bid.equalsIgnoreCase("P")) {
            tvLevel.setText("Pass");
            tvLevel.setTextColor(0xFF2E7D32);
        } else if (bid.equalsIgnoreCase("X") || bid.equalsIgnoreCase("Double")) {
            tvLevel.setText("Kontra");
            tvLevel.setTextColor(0xFFE57373);
        } else if (bid.equalsIgnoreCase("XX")) {
            tvLevel.setText("Rekontra");
            tvLevel.setTextColor(0xFF64B5F6);
        } else {
            try {
                String level = bid.substring(0, 1);
                String suitPart = bid.substring(1).toUpperCase();

                if (suitPart.equalsIgnoreCase("NT")) {
                    tvLevel.setText(level + "NT");
                    tvLevel.setTextColor(0xFF222222);
                } else {
                    tvLevel.setText(level);
                    ivSuit.setVisibility(View.VISIBLE);

                    Suit s = null;
                    switch (suitPart) {
                        case "C": s = Suit.CLUBS; ivSuit.setImageResource(R.drawable.clubs); break;
                        case "D": s = Suit.DIAMONDS; ivSuit.setImageResource(R.drawable.diamonds); break;
                        case "H": s = Suit.HEARTS; ivSuit.setImageResource(R.drawable.heart); break;
                        case "S": s = Suit.SPADES; ivSuit.setImageResource(R.drawable.spades); break;
                    }

                    if (s != null) {
                        int suitColor = s.getColor(context);
                        tvLevel.setTextColor(suitColor);
                        ivSuit.setImageTintList(android.content.res.ColorStateList.valueOf(suitColor));
                    }
                }
            } catch (Exception e) {
                tvLevel.setText(bid);
            }
        }
    }

    public static SpannableStringBuilder formatBidSpannable(Context context, String bid) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (bid == null || bid.isEmpty()) return ssb;

        if (bid.equalsIgnoreCase("Pass") || bid.equalsIgnoreCase("P")) {
            int start = ssb.length();
            ssb.append("Pass");
            ssb.setSpan(new ForegroundColorSpan(0xFF2E7D32), start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }
        if (bid.equalsIgnoreCase("X") || bid.equalsIgnoreCase("Double")) {
            int start = ssb.length();
            ssb.append("Kontra");
            ssb.setSpan(new ForegroundColorSpan(0xFFE57373), start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }
        if (bid.equalsIgnoreCase("XX")) {
            int start = ssb.length();
            ssb.append("Rekontra");
            ssb.setSpan(new ForegroundColorSpan(0xFF64B5F6), start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }

        try {
            String level = bid.substring(0, 1);
            String suitPart = bid.substring(1).toUpperCase();

            if (suitPart.equalsIgnoreCase("NT")) {
                int start = ssb.length();
                ssb.append(level).append("NT");
                ssb.setSpan(new ForegroundColorSpan(0xFF1B2E1D), start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                Suit s = null;
                switch (suitPart) {
                    case "C": s = Suit.CLUBS; break;
                    case "D": s = Suit.DIAMONDS; break;
                    case "H": s = Suit.HEARTS; break;
                    case "S": s = Suit.SPADES; break;
                }

                if (s != null) {
                    int suitColor = s.getColor(context);
                    int startLevel = ssb.length();
                    ssb.append(level);
                    ssb.setSpan(new ForegroundColorSpan(suitColor), startLevel, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    int iconSizePx = (int) (13 * context.getResources().getDisplayMetrics().density);
                    Drawable drawable = getSuitDrawable(context, s, iconSizePx);
                    if (drawable != null) {
                        int iconStart = ssb.length();
                        ssb.append(" ");
                        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                        ssb.setSpan(imageSpan, iconStart, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    ssb.append(level).append(suitPart);
                }
            }
        } catch (Exception e) {
            ssb.append(bid);
        }

        return ssb;
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

        int iconSizePx = (int) (14 * context.getResources().getDisplayMetrics().density);

        for (int i = 0; i < ssb.length(); i++) {
            char ch = ssb.charAt(i);
            Suit suit = null;
            if (ch == '♠') suit = Suit.SPADES;
            else if (ch == '♥') suit = Suit.HEARTS;
            else if (ch == '♦') suit = Suit.DIAMONDS;
            else if (ch == '♣') suit = Suit.CLUBS;

            if (suit != null) {
                Drawable drawable = getSuitDrawable(context, suit, iconSizePx);
                if (drawable != null) {
                    ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                    ssb.setSpan(imageSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return ssb;
    }

    private static String sortDescriptionSuitClauses(Context context, String text) {
        if (text == null || text.trim().isEmpty()) return "";

        String[] lines = text.split("\n");
        List<String> formattedLines = new ArrayList<>();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) continue;

            String processed = processSingleLine(context, trimmedLine);
            if (!processed.isEmpty()) {
                formattedLines.add(processed);
            }
        }

        String rawFormatted = String.join("\n", formattedLines);
        return flattenAndDeduplicateLines(rawFormatted);
    }

    public static String flattenAndDeduplicateLines(String text) {
        if (text == null || text.trim().isEmpty()) return "";

        String[] rawLines = text.split("\n");
        if (rawLines.length <= 1) return text.trim();

        List<String> lines = new ArrayList<>();
        for (String l : rawLines) {
            String trimmed = l.trim();
            if (!trimmed.isEmpty() && !lines.contains(trimmed)) {
                lines.add(trimmed);
            }
        }

        if (lines.size() <= 1) return String.join("\n", lines);

        boolean[] subsumed = new boolean[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            if (subsumed[i]) continue;
            for (int j = 0; j < lines.size(); j++) {
                if (i == j || subsumed[j]) continue;

                if (isSubsumed(lines.get(i), lines.get(j))) {
                    subsumed[i] = true;
                    break;
                }
            }
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (!subsumed[i]) {
                result.add(lines.get(i));
            }
        }

        return String.join("\n", result);
    }

    private static boolean isSubsumed(String lineA, String lineB) {
        String[] clausesA = lineA.split(",\\s*");
        String[] clausesB = lineB.split(",\\s*");

        if (clausesA.length != clausesB.length) return false;

        boolean differs = false;
        for (int i = 0; i < clausesA.length; i++) {
            String cA = clausesA[i].trim();
            String cB = clausesB[i].trim();

            if (cA.equals(cB)) continue;

            int[] rangeA = extractRange(cA);
            int[] rangeB = extractRange(cB);

            if (rangeA != null && rangeB != null) {
                if (rangeB[0] <= rangeA[0] && rangeB[1] >= rangeA[1]) {
                    differs = true;
                    continue;
                }
            }
            return false;
        }

        return differs;
    }

    private static int[] extractRange(String clause) {
        if (clause == null) return null;

        java.util.regex.Matcher mRange = java.util.regex.Pattern.compile("(\\d+)-(\\d+)").matcher(clause);
        if (mRange.find()) {
            String g1 = mRange.group(1);
            String g2 = mRange.group(2);
            if (g1 != null && g2 != null) {
                return new int[]{Integer.parseInt(g1), Integer.parseInt(g2)};
            }
        }

        java.util.regex.Matcher mPlus = java.util.regex.Pattern.compile("(\\d+)\\+").matcher(clause);
        if (mPlus.find()) {
            String g1 = mPlus.group(1);
            if (g1 != null) {
                return new int[]{Integer.parseInt(g1), 99};
            }
        }

        java.util.regex.Matcher mSingle = java.util.regex.Pattern.compile("(\\d+)").matcher(clause);
        if (mSingle.find()) {
            String g1 = mSingle.group(1);
            if (g1 != null) {
                int val = Integer.parseInt(g1);
                return new int[]{val, val};
            }
        }

        return null;
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

        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(?:-\\d+|\\+)?)").matcher(trimmed);
        if (m.find() && m.group(1) != null) {
            String length = m.group(1);
            boolean isPair = trimmed.toLowerCase().contains("pair");
            return symbol + ": " + length + (isPair ? " (pair)" : "");
        }

        return symbol + ": " + trimmed.replaceAll("(?i)(spades|hearts|diamonds|clubs|piki|kiery|kara|trefle|[♠♥♦♣])", "").trim();
    }

    private static String normalizeCommaRangeToDash(String val) {
        if (val == null) return "";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)(?:,\\s*(\\d+))+").matcher(val);
        if (m.find()) {
            java.util.regex.Matcher numMatcher = java.util.regex.Pattern.compile("\\d+").matcher(val);
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

        java.util.regex.Matcher rangeMatcher = java.util.regex.Pattern.compile("(\\d+(?:-\\d+|\\+)?)").matcher(clause);
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

        // 1. HCP / Points First (Category 0.1 for own HCP, 0.2 for pair HCP, 0.3 for pair points)
        if (isHcpClause(clause)) {
            if (lower.contains("pair point") || lower.contains("pair pts") || lower.contains("punkty pary")) {
                return 0.3;
            } else if (lower.contains("pair hcp") || lower.contains("hcp pary") || lower.contains("pair")) {
                return 0.2;
            } else {
                return 0.1;
            }
        }

        // 2. Suits Second (Spades = 1.0, Hearts = 2.0, Diamonds = 3.0, Clubs = 4.0)
        if (clause.contains("♠") || clause.contains("Spades") || clause.contains("Piki") || clause.matches(".*\\bS[:\\d+].*")) return 1.0;
        if (clause.contains("♥") || clause.contains("Hearts") || clause.contains("Kiery") || clause.matches(".*\\bH[:\\d+].*")) return 2.0;
        if (clause.contains("♦") || clause.contains("Diamonds") || clause.contains("Kara") || clause.matches(".*\\bD[:\\d+].*")) return 3.0;
        if (clause.contains("♣") || clause.contains("Clubs") || clause.contains("Trefle") || clause.matches(".*\\bC[:\\d+].*")) return 4.0;

        // 3. Additional Conditions Third (Category 5.0)
        return 5.0;
    }

    private static Drawable getSuitDrawable(Context context, Suit suit, int sizePx) {
        if (suit == null) return null;

        Drawable drawable = ContextCompat.getDrawable(context, suit.resId);
        if (drawable == null) return null;

        drawable = DrawableCompat.wrap(drawable).mutate();
        int color = suit.getColor(context);
        DrawableCompat.setTint(drawable, color);
        drawable.setBounds(0, 0, sizePx, sizePx);

        return drawable;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLevel;
        ImageView ivSuit;
        ImageView ivQuestion;

        ViewHolder(View itemView) {
            super(itemView);
            tvLevel = itemView.findViewById(R.id.tv_bid_level);
            ivSuit = itemView.findViewById(R.id.iv_bid_suit);
            ivQuestion = itemView.findViewById(R.id.iv_bid_question);
        }

        void showQuestionIcon(boolean show) {
            if (ivQuestion != null) {
                ivQuestion.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        }

        void bind(String bid, boolean isCurrent, boolean highlightLast, int layoutId) {
            tvLevel.setText("");
            tvLevel.setTextColor(0xFF000000);
            ivSuit.setVisibility(View.GONE);
            tvLevel.setAlpha(1.0f);

            View inner = itemView.findViewById(R.id.bid_tile_inner);
            if (inner != null) {
                if (isCurrent) {
                    if (highlightLast) {
                        inner.setBackgroundResource(R.drawable.bg_bid_history_tile);
                        inner.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFFF00));
                    } else {
                        inner.setBackgroundResource(R.drawable.bg_bid_history_tile_white);
                        inner.setBackgroundTintList(null);
                    }
                } else {
                    if (layoutId == R.layout.item_bid_tile_compact) {
                        inner.setBackgroundResource(R.drawable.bg_bid_history_tile_compact);
                    } else {
                        inner.setBackgroundResource(R.drawable.bg_bid_history_tile);
                    }
                    inner.setBackgroundTintList(null);
                }
            }

            if (bid == null || bid.isEmpty()) return;

            if (bid.equals("-")) {
                tvLevel.setText("-");
                tvLevel.setAlpha(0.2f);
                return;
            }

            if (bid.equalsIgnoreCase("Pass") || bid.equalsIgnoreCase("P")) {
                tvLevel.setText("P");
                tvLevel.setTextColor(0xFF2E7D32);
            } else if (bid.equalsIgnoreCase("X") || bid.equalsIgnoreCase("Double")) {
                tvLevel.setText("X");
                tvLevel.setTextColor(0xFFC62828);
            } else if (bid.equalsIgnoreCase("XX")) {
                tvLevel.setText("XX");
                tvLevel.setTextColor(0xFF1565C0);
            } else {
                try {
                    String level = bid.substring(0, 1);
                    String suitPart = bid.substring(1).toUpperCase();

                    if (suitPart.equalsIgnoreCase("NT")) {
                        tvLevel.setText(level + "NT");
                    } else {
                        tvLevel.setText(level);
                        ivSuit.setVisibility(View.VISIBLE);
                        ivSuit.setImageResource(getSuitIcon(suitPart));

                        Suit s;
                        switch (suitPart) {
                            case "C": s = Suit.CLUBS; break;
                            case "D": s = Suit.DIAMONDS; break;
                            case "H": s = Suit.HEARTS; break;
                            case "S": s = Suit.SPADES; break;
                            default: s = null; break;
                        }

                        if (s != null) {
                            int suitColor = s.getColor(itemView.getContext());
                            tvLevel.setTextColor(suitColor);
                            ivSuit.setImageTintList(android.content.res.ColorStateList.valueOf(suitColor));
                        }
                    }
                } catch (Exception e) {
                    tvLevel.setText(bid);
                }
            }
        }

        private int getSuitIcon(String suit) {
            switch (suit) {
                case "C": return R.drawable.clubs;
                case "D": return R.drawable.diamonds;
                case "H": return R.drawable.heart;
                case "S": return R.drawable.spades;
                default: return R.drawable.spades;
            }
        }
    }
}
