package com.example.bridge.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.R;
import com.example.bridge.model.Suit;

import org.json.JSONObject;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public interface OnToggleSaveListener {
        void onToggleSave(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(JSONObject item);
    }

    private final List<JSONObject> items;
    private final OnDeleteListener deleteListener;
    private final OnToggleSaveListener toggleSaveListener;
    private final OnItemClickListener clickListener;

    public HistoryListAdapter(List<JSONObject> items, OnDeleteListener deleteListener, OnToggleSaveListener toggleSaveListener, OnItemClickListener clickListener) {
        this.items = items;
        this.deleteListener = deleteListener;
        this.toggleSaveListener = toggleSaveListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject item = items.get(position);
            String contractStr = item.getString("contract");
            int snTricks = item.optInt("snScore", 0);
            int points = item.optInt("points", 0);

            if (contractStr.toUpperCase().contains("PASS")) {
                holder.tvContract.setText(R.string.contract_pass);
                holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                holder.ivSuit.setVisibility(View.GONE);
                holder.tvResultSymbol.setText("");
            } else {
                String[] parts = contractStr.split(" ");
                if (parts.length >= 2) {
                    holder.tvContract.setText(parts[0]);
                    String suitPart = parts[1].toUpperCase();
                    
                    if (suitPart.equals("NT")) {
                        holder.ivSuit.setVisibility(View.GONE);
                        holder.tvContract.setText(parts[0] + "NT");
                        holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                    } else {
                        try {
                            Suit suit = Suit.valueOf(suitPart);
                            holder.ivSuit.setVisibility(View.VISIBLE);
                            holder.ivSuit.setImageResource(suit.resId);
                            int suitColor = suit.getColor(holder.itemView.getContext());
                            holder.ivSuit.setColorFilter(suitColor);
                            holder.tvContract.setTextColor(suitColor);
                        } catch (Exception e) {
                            holder.ivSuit.setVisibility(View.GONE);
                            holder.tvContract.setText(contractStr);
                            holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                        }
                    }

                    // Calculate result symbol: S=, S+1, S-1
                    int required = Integer.parseInt(parts[0]) + 6;
                    int diff = snTricks - required;
                    String symbol = " S";
                    if (diff == 0) symbol += "=";
                    else if (diff > 0) symbol += "+" + diff;
                    else symbol += diff; // diff is negative
                    holder.tvResultSymbol.setText(symbol);
                    holder.tvResultSymbol.setTextColor(android.graphics.Color.BLACK);

                } else {
                    holder.tvContract.setText(contractStr);
                    holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                    holder.ivSuit.setVisibility(View.GONE);
                    holder.tvResultSymbol.setText("");
                }
            }

            // Points display
            if (points != 0) {
                holder.tvPoints.setText((points > 0 ? "+" : "") + points);
                holder.tvPoints.setTextColor(points > 0 ? android.graphics.Color.parseColor("#FFD700") : android.graphics.Color.parseColor("#FF5252"));
            } else {
                holder.tvPoints.setText("0");
                holder.tvPoints.setTextColor(android.graphics.Color.WHITE);
            }

            holder.tvDate.setText(item.getString("date"));

            boolean isSaved = item.optBoolean("isSaved", false);
            com.google.android.material.card.MaterialCardView card = (com.google.android.material.card.MaterialCardView) holder.itemView;
            card.setCardBackgroundColor(android.graphics.Color.parseColor("#122614"));

            if (isSaved) {
                card.setStrokeColor(android.graphics.Color.parseColor("#FFC107"));
                card.setStrokeWidth(3);
                holder.btnToggleSave.setImageResource(R.drawable.ic_star);
                holder.btnToggleSave.setColorFilter(android.graphics.Color.parseColor("#FFC107"));
            } else {
                card.setStrokeColor(android.graphics.Color.parseColor("#1AFFFFFF"));
                card.setStrokeWidth(1);
                holder.btnToggleSave.setImageResource(R.drawable.ic_star);
                holder.btnToggleSave.setColorFilter(android.graphics.Color.WHITE);
            }

            holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(holder.getAdapterPosition()));
            holder.btnToggleSave.setOnClickListener(v -> toggleSaveListener.onToggleSave(holder.getAdapterPosition()));
            holder.itemView.setOnClickListener(v -> clickListener.onItemClick(item));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContract, tvResultSymbol, tvPoints, tvDate;
        ImageView ivSuit;
        ImageButton btnDelete, btnToggleSave;

        ViewHolder(View itemView) {
            super(itemView);
            tvContract = itemView.findViewById(R.id.tv_history_contract);
            tvResultSymbol = itemView.findViewById(R.id.tv_history_result_symbol);
            tvPoints = itemView.findViewById(R.id.tv_history_points);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            ivSuit = itemView.findViewById(R.id.iv_history_suit);
            btnDelete = itemView.findViewById(R.id.btn_delete_history);
            btnToggleSave = itemView.findViewById(R.id.btn_toggle_save);
        }
    }
}
