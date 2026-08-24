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
            
            // Handle the {system: "...", data: {...}} wrapper if present
            JSONObject data = item.has("data") ? item.getJSONObject("data") : item;
            String systemName = item.optString("system", "");
            String gameModeLabel = data.optString("GameMode", "");

            String contractStr = data.optString("Contract", "PASS");
            int snTricks = data.optInt("Result", 0);
            double imp = data.optDouble("Imp", 0.0);

            if (contractStr.toUpperCase().contains("PASS")) {
                holder.tvContract.setText(R.string.contract_pass);
                if (!gameModeLabel.isEmpty()) {
                    holder.tvContract.setText(gameModeLabel + ": " + holder.tvContract.getText());
                }
                holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                holder.ivSuit.setVisibility(View.GONE);
                holder.tvResultSymbol.setText("");
            } else {
                String displayContract = contractStr;
                if (!gameModeLabel.isEmpty()) {
                    displayContract = gameModeLabel + ": " + contractStr;
                }
                else if (!systemName.isEmpty()) {
                    displayContract = systemName + ": " + contractStr;
                }
                
                String[] parts = contractStr.split("(?<=\\d)(?=\\D)"); // Split after number
                if (parts.length >= 2) {
                    holder.tvContract.setText(parts[0]);
                    String suitPart = parts[1].toUpperCase();
                    
                    if (suitPart.equals("NT")) {
                        holder.ivSuit.setVisibility(View.GONE);
                        holder.tvContract.setText(parts[0] + "NT");
                        holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                    } else {
                        try {
                            // Extract first char for Suit.valueOf if needed, or mapping
                            Suit suit = null;
                            if (suitPart.startsWith("S")) suit = Suit.SPADES;
                            else if (suitPart.startsWith("H")) suit = Suit.HEARTS;
                            else if (suitPart.startsWith("D")) suit = Suit.DIAMONDS;
                            else if (suitPart.startsWith("C")) suit = Suit.CLUBS;

                            if (suit != null) {
                                holder.ivSuit.setVisibility(View.VISIBLE);
                                holder.ivSuit.setImageResource(suit.resId);
                                int suitColor = suit.getColor(holder.itemView.getContext());
                                holder.ivSuit.setColorFilter(suitColor);
                                holder.tvContract.setTextColor(suitColor);
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            holder.ivSuit.setVisibility(View.GONE);
                            holder.tvContract.setText(contractStr);
                            holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                        }
                    }

                    // Calculate result symbol: S=, S+1, S-1
                    try {
                        int level = Integer.parseInt(parts[0].replaceAll("\\D", ""));
                        int required = level + 6;
                        int diff = snTricks - required;
                        String symbol = " S";
                        if (diff == 0) symbol += "=";
                        else if (diff > 0) symbol += "+" + diff;
                        else symbol += diff; 
                        holder.tvResultSymbol.setText(symbol);
                    } catch (Exception e) {
                        holder.tvResultSymbol.setText("");
                    }
                    holder.tvResultSymbol.setTextColor(android.graphics.Color.BLACK);

                } else {
                    holder.tvContract.setText(displayContract);
                    holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                    holder.ivSuit.setVisibility(View.GONE);
                    holder.tvResultSymbol.setText("");
                }
            }

            // IMP display
            if (imp != 0.0) {
                holder.tvPoints.setText(String.format(java.util.Locale.US, "%s%.1f IMP", (imp > 0 ? "+" : ""), imp));
                holder.tvPoints.setTextColor(imp > 0 ? android.graphics.Color.parseColor("#4CAF50") : android.graphics.Color.parseColor("#FF5252"));
            } else {
                holder.tvPoints.setText("0.0 IMP");
                holder.tvPoints.setTextColor(android.graphics.Color.WHITE);
            }

            holder.tvDate.setText(data.optString("Date", ""));

            // Game Mode Icon
            if (gameModeLabel.equalsIgnoreCase("quick")) {
                holder.ivGameMode.setImageResource(R.drawable.ic_arrow);
            } else if (gameModeLabel.equalsIgnoreCase("single")) {
                holder.ivGameMode.setImageResource(R.drawable.ic_person);
            } else {
                holder.ivGameMode.setImageResource(R.drawable.ic_arrow); // Default
            }

            boolean isFavorite = item.optBoolean("isFavorite", false);
            com.google.android.material.card.MaterialCardView card = (com.google.android.material.card.MaterialCardView) holder.itemView;
            card.setCardBackgroundColor(android.graphics.Color.parseColor("#122614"));

            if (isFavorite) {
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
        ImageView ivSuit, ivGameMode;
        ImageButton btnDelete, btnToggleSave;

        ViewHolder(View itemView) {
            super(itemView);
            tvContract = itemView.findViewById(R.id.tv_history_contract);
            tvResultSymbol = itemView.findViewById(R.id.tv_history_result_symbol);
            tvPoints = itemView.findViewById(R.id.tv_history_points);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            ivSuit = itemView.findViewById(R.id.iv_history_suit);
            ivGameMode = itemView.findViewById(R.id.iv_history_game_mode);
            btnDelete = itemView.findViewById(R.id.btn_delete_history);
            btnToggleSave = itemView.findViewById(R.id.btn_toggle_save);
        }
    }
}
