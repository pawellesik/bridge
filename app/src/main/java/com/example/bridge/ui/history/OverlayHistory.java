package com.example.bridge.ui.history;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.R;
import com.example.bridge.core.SharedPref;
import com.example.bridge.core.db.AppDatabase;
import com.example.bridge.core.db.GameRecord;
import com.example.bridge.ui.game.GameActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OverlayHistory {

    private final GameActivity activity;
    private final View root;
    private HistoryListAdapter adapter;
    private final List<JSONObject> fullHistoryList = new ArrayList<>();
    private final List<JSONObject> filteredList = new ArrayList<>();
    private TextView tvEmpty;
    private CheckBox cbOnlySaved;
    private Spinner spinnerLevel, spinnerSuit, spinnerResult;

    public OverlayHistory(GameActivity activity) {
        this.activity = activity;
        this.root = activity.findViewById(R.id.history_overlay);
        initViews();
    }

    private void initViews() {
        if (root == null) return;

        RecyclerView rvHistory = root.findViewById(R.id.rv_history_overlay);
        tvEmpty = root.findViewById(R.id.tv_empty_history_overlay);
        cbOnlySaved = root.findViewById(R.id.cb_filter_saved_overlay);
        spinnerLevel = root.findViewById(R.id.spinner_level_filter_overlay);
        spinnerSuit = root.findViewById(R.id.spinner_suit_filter_overlay);
        spinnerResult = root.findViewById(R.id.spinner_result_filter_overlay);

        if (rvHistory != null) {
            rvHistory.setLayoutManager(new LinearLayoutManager(activity));
            adapter = new HistoryListAdapter(filteredList, this::showDeleteDialog, this::toggleSave, item -> {
                // Handle item click - e.g., replay or show details
            });
            rvHistory.setAdapter(adapter);
        }

        setupFilters();
    }

    public void refresh() {
        System.out.print("plesik refresh");
        new Thread(() -> {
            try {
                List<GameRecord> records = AppDatabase.getInstance(activity).gameDao().getAllUniqueGames();
                System.out.print("plesik "+records.toString());
                List<JSONObject> loadedList = new ArrayList<>();
                for (GameRecord record : records) {
                    JSONObject gameData = new JSONObject(record.gameData);
                    JSONObject wrapper = new JSONObject();
                    wrapper.put("system", record.system);
                    wrapper.put("data", gameData);
                    wrapper.put("isFavorite", record.isFavorite);
                    wrapper.put("db_id", record.id); // Store ID for deletions/updates
                    loadedList.add(wrapper);
                }

                activity.runOnUiThread(() -> {
                    fullHistoryList.clear();
                    fullHistoryList.addAll(loadedList);
                    applyFilters();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setupFilters() {
        if (spinnerLevel == null || spinnerSuit == null || spinnerResult == null || cbOnlySaved == null) return;

        String[] levelOptions = { activity.getString(R.string.filter_level_all), "1", "2", "3", "4", "5", "6", "7" };
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, levelOptions);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(levelAdapter);

        String[] suitOptions = {
                activity.getString(R.string.filter_suit_all), activity.getString(R.string.filter_contract_nt),
                activity.getString(R.string.filter_contract_spades), activity.getString(R.string.filter_contract_hearts),
                activity.getString(R.string.filter_contract_diamonds), activity.getString(R.string.filter_contract_clubs)
        };
        ArrayAdapter<String> suitAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, suitOptions);
        suitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSuit.setAdapter(suitAdapter);

        String[] resultOptions = {
                activity.getString(R.string.filter_result_all), activity.getString(R.string.filter_result_won), activity.getString(R.string.filter_result_lost)
        };
        ArrayAdapter<String> resultAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, resultOptions);
        resultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResult.setAdapter(resultAdapter);

        cbOnlySaved.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { applyFilters(); }
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
        int selectedLevelIdx = spinnerLevel.getSelectedItemPosition();
        int selectedSuitIdx = spinnerSuit.getSelectedItemPosition();
        int resultType = spinnerResult.getSelectedItemPosition();

        for (JSONObject gameWrapper : fullHistoryList) {
            try {
                if (onlySaved && !gameWrapper.optBoolean("isFavorite", false)) continue;

                JSONObject game = gameWrapper.has("data") ? gameWrapper.getJSONObject("data") : gameWrapper;
                String contractStr = game.optString("Contract", game.optString("contract", "PASS"));
                int tricks = game.optInt("Result", game.optInt("snTricks", 0));

                boolean won = true;
                int contractLevel = 0;
                if (!contractStr.toUpperCase().contains("PASS")) {
                    String[] conParts = contractStr.split("(?<=\\d)(?=\\D)| ");
                    if (conParts.length >= 1) {
                        try {
                            contractLevel = Integer.parseInt(conParts[0].replaceAll("\\D", ""));
                            if (tricks < (contractLevel + 6)) won = false;
                        } catch (Exception e) {}
                    }
                }

                if (resultType == 1 && !won) continue;
                if (resultType == 2 && won) continue;
                if (selectedLevelIdx > 0 && contractLevel != selectedLevelIdx) continue;

                if (selectedSuitIdx > 0) {
                    String contractUpper = contractStr.toUpperCase();
                    boolean match = false;
                    switch (selectedSuitIdx) {
                        case 1: match = contractUpper.contains("NT"); break;
                        case 2: match = contractUpper.contains("S") || contractUpper.contains("SPADES"); break;
                        case 3: match = contractUpper.contains("H") || contractUpper.contains("HEARTS"); break;
                        case 4: match = contractUpper.contains("D") || contractUpper.contains("DIAMONDS"); break;
                        case 5: match = contractUpper.contains("C") || contractUpper.contains("CLUBS"); break;
                    }
                    if (!match) continue;
                }
                filteredList.add(gameWrapper);
            } catch (Exception e) { e.printStackTrace(); }
        }
        adapter.notifyDataSetChanged();
        if (tvEmpty != null) tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void toggleSave(int position) {
        new Thread(() -> {
            try {
                JSONObject item = filteredList.get(position);
                int dbId = item.optInt("db_id", -1);
                if (dbId != -1) {
                    boolean newFavoriteStatus = !item.optBoolean("isFavorite", false);
                    AppDatabase.getInstance(activity).gameDao().updateFavoriteStatus(dbId, newFavoriteStatus);
                    
                    activity.runOnUiThread(() -> {
                        try {
                            item.put("isFavorite", newFavoriteStatus);
                            adapter.notifyItemChanged(position);
                        } catch (Exception e) {}
                    });
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void showDeleteDialog(int position) {
        new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    new Thread(() -> {
                        try {
                            JSONObject itemToRemove = filteredList.get(position);
                            int dbId = itemToRemove.optInt("db_id", -1);
                            if (dbId != -1) {
                                AppDatabase.getInstance(activity).gameDao().deleteById(dbId);
                                activity.runOnUiThread(() -> {
                                    fullHistoryList.remove(itemToRemove);
                                    applyFilters();
                                });
                            }
                        } catch (Exception e) { e.printStackTrace(); }
                    }).start();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }


    public void saveGameToHistory(Context context, String gameJson) {
        new Thread(() -> {
            try {
                JSONArray batch = new JSONArray(gameJson);
                long now = System.currentTimeMillis();
                AppDatabase db = AppDatabase.getInstance(context);

                for (int i = 0; i < batch.length(); i++) {
                    JSONObject obj = batch.getJSONObject(i);
                    
                    GameRecord record = new GameRecord();
                    record.timestamp = now;
                    record.system = obj.optString("system");
                    record.gameData = obj.optJSONObject("data").toString();
                    record.isFavorite = false;

                    db.gameDao().insert(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public int getVisibility() {
        return root != null ? root.getVisibility() : View.GONE;
    }
}
