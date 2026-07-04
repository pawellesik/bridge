package com.example.bridge.ui.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.R;
import com.example.bridge.model.Suit;

import java.util.List;

public class GameBiddingHistoryAdapter extends RecyclerView.Adapter<GameBiddingHistoryAdapter.ViewHolder> {

    private final List<String> bids;

    public GameBiddingHistoryAdapter(List<String> bids) {
        this.bids = bids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bid_tile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String bid = bids.get(position);
        holder.bind(bid);
    }

    @Override
    public int getItemCount() {
        return bids.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLevel;
        ImageView ivSuit;

        ViewHolder(View itemView) {
            super(viewPanel(itemView));
            tvLevel = itemView.findViewById(R.id.tv_bid_level);
            ivSuit = itemView.findViewById(R.id.iv_bid_suit);
        }

        private static View viewPanel(View v) {
             return v;
        }

        void bind(String bid) {
            // Default reset
            tvLevel.setTextColor(0xFF000000);
            ivSuit.setVisibility(View.GONE);
            tvLevel.setAlpha(1.0f);
            View inner = itemView.findViewById(R.id.bid_tile_inner);
            if (inner != null) inner.setBackgroundResource(R.drawable.bg_bid_history_tile);
            
            if (bid == null || bid.isEmpty() || bid.equals("-")) {
                tvLevel.setText("-");
                tvLevel.setAlpha(0.2f); // Very muted dash for empty cells
                return;
            }

            if (bid.equalsIgnoreCase("Pass") || bid.equalsIgnoreCase("P")) {
                tvLevel.setText("P");
                tvLevel.setTextColor(0xFF2E7D32); // Green for Pass as in image
            } else if (bid.equalsIgnoreCase("X") || bid.equalsIgnoreCase("Double")) {
                tvLevel.setText("X");
                tvLevel.setTextColor(0xFFC62828); // Red
            } else if (bid.equalsIgnoreCase("XX")) {
                tvLevel.setText("XX");
                tvLevel.setTextColor(0xFF1565C0); // Blue
            } else {
                // Format like "1S", "3NT"
                try {
                    String level = bid.substring(0, 1);
                    String suitPart = bid.substring(1).toUpperCase();
                    
                    if (suitPart.equalsIgnoreCase("NT")) {
                        tvLevel.setText(level + "NT");
                    } else {
                        tvLevel.setText(level);
                        ivSuit.setVisibility(View.VISIBLE);
                        int iconRes = getSuitIcon(suitPart);
                        ivSuit.setImageResource(iconRes);
                        
                        // Apply dynamic colors (2 or 4 color deck)
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
