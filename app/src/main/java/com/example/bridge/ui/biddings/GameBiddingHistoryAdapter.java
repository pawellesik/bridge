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
                holder.itemView.setOnClickListener(v -> showDescriptionTooltip(v, bid, desc));
            } else {
                holder.itemView.setOnClickListener(null);
            }
        } else {
            // Kafelek podglądu (następny ruch)
            boolean isSouthColumn = (position % 4 == 3);
            holder.bind(previewSelection, true, highlightLast && isSouthColumn, layoutId);
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
        int x = location[0] + (anchorView.getWidth() - popupWidth) / 2;
        int y = location[1] + anchorView.getHeight() - 2;

        popupWindow.showAtLocation(anchorView, android.view.Gravity.NO_GRAVITY, Math.max(16, x), y);
    }

    private static void bindBidHeader(Context context, String bid, TextView tvLevel, ImageView ivSuit) {
        if (bid == null || bid.isEmpty()) return;

        ivSuit.setVisibility(View.GONE);

        if (bid.equalsIgnoreCase("Pass") || bid.equalsIgnoreCase("P")) {
            tvLevel.setText("Pass");
            tvLevel.setTextColor(0xFF81C784);
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

    public static SpannableStringBuilder formatDescriptionText(Context context, String text) {
        if (text == null) return new SpannableStringBuilder("");

        String s = text;

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

        ViewHolder(View itemView) {
            super(itemView);
            tvLevel = itemView.findViewById(R.id.tv_bid_level);
            ivSuit = itemView.findViewById(R.id.iv_bid_suit);
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
