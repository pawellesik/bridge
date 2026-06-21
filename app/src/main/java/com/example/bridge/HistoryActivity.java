package com.example.bridge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.model.Suit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter adapter;
    private List<JSONObject> historyList;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView rvHistory = findViewById(R.id.rv_history);
        tvEmpty = findViewById(R.id.tv_empty_history);

        String json = getSharedPreferences("BridgePrefs", MODE_PRIVATE).getString("gameHistory", "[]");

        historyList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                historyList.add(array.getJSONObject(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (historyList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvHistory.setLayoutManager(new LinearLayoutManager(this));
            adapter = new HistoryAdapter(historyList, this::showDeleteDialog);
            rvHistory.setAdapter(adapter);
        }
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    historyList.remove(position);
                    saveHistory();
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, historyList.size());
                    if (historyList.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void saveHistory() {
        JSONArray array = new JSONArray();
        for (JSONObject obj : historyList) {
            array.put(obj);
        }
        getSharedPreferences("BridgePrefs", MODE_PRIVATE)
                .edit()
                .putString("gameHistory", array.toString())
                .apply();
    }

    private interface OnDeleteListener {
        void onDelete(int position);
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
        private final List<JSONObject> items;
        private final OnDeleteListener deleteListener;

        HistoryAdapter(List<JSONObject> items, OnDeleteListener deleteListener) {
            this.items = items;
            this.deleteListener = deleteListener;
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
                
                // Parse contract: e.g. "6 Spades", "PASS", "1 NT"
                if (contractStr.toUpperCase().contains("PASS")) {
                    holder.tvContract.setText(R.string.contract_pass);
                    holder.ivSuit.setVisibility(View.GONE);
                } else {
                    String[] parts = contractStr.split(" ");
                    if (parts.length >= 2) {
                        holder.tvContract.setText(parts[0]);
                        String suitPart = parts[1].toUpperCase();
                        if (suitPart.equals("NT")) {
                            holder.ivSuit.setVisibility(View.GONE);
                            holder.tvContract.setText(parts[0] + " " + holder.itemView.getContext().getString(R.string.suit_nt));
                        } else {
                            try {
                                Suit suit = Suit.valueOf(suitPart);
                                holder.ivSuit.setVisibility(View.VISIBLE);
                                holder.ivSuit.setImageResource(suit.resId);
                            } catch (Exception e) {
                                holder.ivSuit.setVisibility(View.GONE);
                                holder.tvContract.setText(contractStr);
                            }
                        }
                    } else {
                        holder.tvContract.setText(contractStr);
                        holder.ivSuit.setVisibility(View.GONE);
                    }
                }

                String resultStr = item.getString("result");
                int snTricks = 0;
                try {
                    // result format: "SN: 10 - WE: 3"
                    String[] resParts = resultStr.split(" ");
                    if (resParts.length >= 2) {
                        snTricks = Integer.parseInt(resParts[1]);
                    }
                } catch (Exception e) {}

                holder.tvResult.setText(holder.itemView.getContext().getString(R.string.result_label, snTricks));
                holder.tvDate.setText(item.getString("date"));

                // Logic for color: yellow if contract failed
                boolean failed = false;
                if (!contractStr.toUpperCase().contains("PASS")) {
                    try {
                        String[] conParts = contractStr.split(" ");
                        if (conParts.length >= 1) {
                            int level = Integer.parseInt(conParts[0]);
                            if (snTricks < (level + 6)) {
                                failed = true;
                            }
                        }
                    } catch (Exception e) {}
                }
                
                holder.tvResult.setTextColor(failed ? 0xFFFFEE58 : 0xFFFFFFFF);
                
                holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(holder.getAdapterPosition()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvContract, tvResult, tvDate;
            ImageView ivSuit;
            ImageButton btnDelete;

            ViewHolder(View itemView) {
                super(itemView);
                tvContract = itemView.findViewById(R.id.tv_history_contract);
                tvResult = itemView.findViewById(R.id.tv_history_result);
                tvDate = itemView.findViewById(R.id.tv_history_date);
                ivSuit = itemView.findViewById(R.id.iv_history_suit);
                btnDelete = itemView.findViewById(R.id.btn_delete_history);
            }
        }
    }
}
