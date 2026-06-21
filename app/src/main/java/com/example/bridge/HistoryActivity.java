package com.example.bridge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
    private List<JSONObject> fullHistoryList;
    private List<JSONObject> filteredList;
    private TextView tvEmpty;
    private CheckBox cbOnlySaved;
    private Spinner spinnerContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView rvHistory = findViewById(R.id.rv_history);
        tvEmpty = findViewById(R.id.tv_empty_history);
        cbOnlySaved = findViewById(R.id.cb_filter_saved);
        spinnerContract = findViewById(R.id.spinner_contract_filter);

        String json = getSharedPreferences("BridgePrefs", MODE_PRIVATE).getString("gameHistory", "[]");

        fullHistoryList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                fullHistoryList.add(array.getJSONObject(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filteredList = new ArrayList<>(fullHistoryList);
        
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(filteredList, this::showDeleteDialog);
        rvHistory.setAdapter(adapter);

        setupFilters();
        applyFilters();
    }

    private void setupFilters() {
        // Setup Contract Spinner
        String[] options = {
                getString(R.string.filter_contract_all),
                getString(R.string.filter_contract_nt),
                getString(R.string.filter_contract_spades),
                getString(R.string.filter_contract_hearts),
                getString(R.string.filter_contract_diamonds),
                getString(R.string.filter_contract_clubs)
        };
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContract.setAdapter(spinnerAdapter);

        // Listeners
        cbOnlySaved.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());
        
        spinnerContract.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void applyFilters() {
        filteredList.clear();
        boolean onlySaved = cbOnlySaved.isChecked();
        int contractType = spinnerContract.getSelectedItemPosition(); // 0:All, 1:NT, 2:S, 3:H, 4:D, 5:C

        for (JSONObject game : fullHistoryList) {
            try {
                // 1. Check Saved Filter
                if (onlySaved && !game.optBoolean("isSaved", false)) {
                    continue;
                }

                // 2. Check Contract Filter
                String contractStr = game.getString("contract").toUpperCase();
                if (contractType > 0) {
                    boolean match = false;
                    switch (contractType) {
                        case 1: match = contractStr.contains("NT"); break;
                        case 2: match = contractStr.contains("SPADES"); break;
                        case 3: match = contractStr.contains("HEARTS"); break;
                        case 4: match = contractStr.contains("DIAMONDS"); break;
                        case 5: match = contractStr.contains("CLUBS"); break;
                    }
                    if (!match) continue;
                }

                filteredList.add(game);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    JSONObject itemToRemove = filteredList.get(position);
                    fullHistoryList.remove(itemToRemove);
                    saveHistory();
                    applyFilters();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void saveHistory() {
        JSONArray array = new JSONArray();
        for (JSONObject obj : fullHistoryList) {
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
                    String[] resParts = resultStr.split(" ");
                    if (resParts.length >= 2) {
                        snTricks = Integer.parseInt(resParts[1]);
                    }
                } catch (Exception e) {}

                holder.tvResult.setText(holder.itemView.getContext().getString(R.string.result_label, snTricks));
                holder.tvDate.setText(item.getString("date"));

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

                boolean isSaved = item.optBoolean("isSaved", false);
                if (isSaved) {
                    ((com.google.android.material.card.MaterialCardView) holder.itemView).setCardBackgroundColor(android.graphics.Color.parseColor("#43A047"));
                } else {
                    ((com.google.android.material.card.MaterialCardView) holder.itemView).setCardBackgroundColor(android.graphics.Color.parseColor("#2E7D32"));
                }
                
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
