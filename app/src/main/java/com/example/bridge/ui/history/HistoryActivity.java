package com.example.bridge.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import com.example.bridge.R;
import com.example.bridge.model.Suit;
import com.example.bridge.ui.game.GameActivity;
import com.example.bridge.core.LocaleHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
    private Spinner spinnerLevel, spinnerSuit, spinnerResult;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView rvHistory = findViewById(R.id.rv_history);
        tvEmpty = findViewById(R.id.tv_empty_history);
        cbOnlySaved = findViewById(R.id.cb_filter_saved);
        spinnerLevel = findViewById(R.id.spinner_level_filter);
        spinnerSuit = findViewById(R.id.spinner_suit_filter);
        spinnerResult = findViewById(R.id.spinner_result_filter);

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
        adapter = new HistoryAdapter(filteredList, this::showDeleteDialog, this::toggleSave, item -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("replayedGameJson", item.toString());
            startActivity(intent);
        });
        rvHistory.setAdapter(adapter);

        setupFilters();
        applyFilters();
    }

    private void setupFilters() {
        // Setup Level Spinner
        String[] levelOptions = {
                getString(R.string.filter_level_all),
                "1", "2", "3", "4", "5", "6", "7"
        };
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, levelOptions) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(12);
                ((TextView) v).setPadding(0, 0, 0, 0);
                return v;
            }
        };
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(levelAdapter);

        // Setup Suit Spinner
        String[] suitOptions = {
                getString(R.string.filter_suit_all),
                getString(R.string.filter_contract_nt),
                getString(R.string.filter_contract_spades),
                getString(R.string.filter_contract_hearts),
                getString(R.string.filter_contract_diamonds),
                getString(R.string.filter_contract_clubs)
        };
        ArrayAdapter<String> suitAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, suitOptions) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(12);
                ((TextView) v).setPadding(0, 0, 0, 0);
                return v;
            }
        };
        suitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSuit.setAdapter(suitAdapter);

        // Setup Result Spinner
        String[] resultOptions = {
                getString(R.string.filter_result_all),
                getString(R.string.filter_result_won),
                getString(R.string.filter_result_lost)
        };
        ArrayAdapter<String> resultAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, resultOptions) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(12);
                ((TextView) v).setPadding(0, 0, 0, 0);
                return v;
            }
        };
        resultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResult.setAdapter(resultAdapter);

        // Listeners
        cbOnlySaved.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());
        
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        spinnerLevel.setOnItemSelectedListener(listener);
        spinnerSuit.setOnItemSelectedListener(listener);
        spinnerResult.setOnItemSelectedListener(listener);
    }

    private void applyFilters() {
        filteredList.clear();
        boolean onlySaved = cbOnlySaved.isChecked();
        int selectedLevelIdx = spinnerLevel.getSelectedItemPosition(); // 0:All, 1-7: "1"-"7"
        int selectedSuitIdx = spinnerSuit.getSelectedItemPosition(); // 0:All, 1:NT, 2:S, 3:H, 4:D, 5:C
        int resultType = spinnerResult.getSelectedItemPosition(); // 0:All, 1:Won, 2:Lost

        for (JSONObject game : fullHistoryList) {
            try {
                // 1. Check Saved Filter
                if (onlySaved && !game.optBoolean("isSaved", false)) {
                    continue;
                }

                String contractStr = game.getString("contract");
                String resultStr = game.getString("result");
                
                int tricks = 0;
                try {
                    String[] resParts = resultStr.split(" ");
                    if (resParts.length >= 2) tricks = Integer.parseInt(resParts[1]);
                } catch (Exception e) {}

                // 2. Determine Won/Lost
                boolean won = true;
                int contractLevel = 0;
                if (!contractStr.toUpperCase().contains("PASS")) {
                    try {
                        String[] conParts = contractStr.split(" ");
                        if (conParts.length >= 1) {
                            contractLevel = Integer.parseInt(conParts[0]);
                            if (tricks < (contractLevel + 6)) won = false;
                        }
                    } catch (Exception e) {}
                }

                if (resultType == 1 && !won) continue;
                if (resultType == 2 && won) continue;

                // 3. Level Filter
                if (selectedLevelIdx > 0) {
                    if (contractLevel != selectedLevelIdx) continue;
                }

                // 4. Suit Filter
                String contractUpper = contractStr.toUpperCase();
                if (selectedSuitIdx > 0) {
                    boolean match = false;
                    switch (selectedSuitIdx) {
                        case 1: match = contractUpper.contains("NT"); break;
                        case 2: match = contractUpper.contains("SPADES"); break;
                        case 3: match = contractUpper.contains("HEARTS"); break;
                        case 4: match = contractUpper.contains("DIAMONDS"); break;
                        case 5: match = contractUpper.contains("CLUBS"); break;
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

    private void toggleSave(int position) {
        try {
            JSONObject item = filteredList.get(position);
            boolean currentStatus = item.optBoolean("isSaved", false);
            int messageResId = currentStatus ? R.string.unhighlight_confirm_message : R.string.highlight_confirm_message;

            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(messageResId)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        try {
                            item.put("isSaved", !currentStatus);
                            saveHistory();
                            applyFilters();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteDialog(int position) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    JSONObject itemToRemove = filteredList.get(position);
                    fullHistoryList.remove(itemToRemove);
                    saveHistory();
                    applyFilters();
                })
                .setNegativeButton(R.string.no, null);

        AlertDialog dialog = builder.show();
        
        // Make the "Yes" button red to indicate a destructive action
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(android.graphics.Color.parseColor("#FF1744"));
        }
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

    private interface OnToggleSaveListener {
        void onToggleSave(int position);
    }

    private interface OnItemClickListener {
        void onItemClick(JSONObject item);
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
        private final List<JSONObject> items;
        private final OnDeleteListener deleteListener;
        private final OnToggleSaveListener toggleSaveListener;
        private final OnItemClickListener clickListener;

        HistoryAdapter(List<JSONObject> items, OnDeleteListener deleteListener, OnToggleSaveListener toggleSaveListener, OnItemClickListener clickListener) {
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
                
                if (contractStr.toUpperCase().contains("PASS")) {
                    holder.tvContract.setText(R.string.contract_pass);
                    holder.tvContract.setTextColor(android.graphics.Color.BLACK);
                    holder.ivSuit.setVisibility(View.GONE);
                } else {
                    String[] parts = contractStr.split(" ");
                    if (parts.length >= 2) {
                        holder.tvContract.setText(parts[0]);
                        String suitPart = parts[1].toUpperCase();
                        if (suitPart.equals("NT")) {
                            holder.ivSuit.setVisibility(View.GONE);
                            holder.tvContract.setText(parts[0] + " " + holder.itemView.getContext().getString(R.string.suit_nt));
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
                    } else {
                        holder.tvContract.setText(contractStr);
                        holder.tvContract.setTextColor(android.graphics.Color.BLACK);
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

                String fullResultText = holder.itemView.getContext().getString(R.string.result_label, snTricks);
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

                if (failed) {
                    holder.tvResult.setText(fullResultText);
                    holder.tvResult.setTextColor(android.graphics.Color.parseColor("#BDBDBD")); // Grey for lost
                } else {
                    holder.tvResult.setText(fullResultText);
                    holder.tvResult.setTextColor(android.graphics.Color.parseColor("#FFD700")); // Yellow for won
                }

                boolean isSaved = item.optBoolean("isSaved", false);
                com.google.android.material.card.MaterialCardView card = (com.google.android.material.card.MaterialCardView) holder.itemView;
                
                // Dark Forest card background
                card.setCardBackgroundColor(android.graphics.Color.parseColor("#16321A"));
                
                if (isSaved) {
                    // Yellow/Gold outline for highlighted items
                    card.setStrokeColor(android.graphics.Color.parseColor("#FFD700"));
                    card.setStrokeWidth(2);
                    // Yellow star icon
                    holder.btnToggleSave.setImageResource(R.drawable.ic_star);
                    holder.btnToggleSave.setColorFilter(android.graphics.Color.parseColor("#FFD700"));
                } else {
                    // Subtle white outline for normal items
                    card.setStrokeColor(android.graphics.Color.parseColor("#33FFFFFF"));
                    card.setStrokeWidth(1);
                    // White star icon
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
            TextView tvContract, tvResult, tvDate;
            ImageView ivSuit;
            ImageButton btnDelete, btnToggleSave;

            ViewHolder(View itemView) {
                super(itemView);
                tvContract = itemView.findViewById(R.id.tv_history_contract);
                tvResult = itemView.findViewById(R.id.tv_history_result);
                tvDate = itemView.findViewById(R.id.tv_history_date);
                ivSuit = itemView.findViewById(R.id.iv_history_suit);
                btnDelete = itemView.findViewById(R.id.btn_delete_history);
                btnToggleSave = itemView.findViewById(R.id.btn_toggle_save);
            }
        }
    }
}
