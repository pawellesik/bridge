package com.example.bridge.ui.history;

import android.content.Context;
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
import com.example.bridge.core.LocaleHelper;
import com.example.bridge.core.SharedPref;
import com.example.bridge.model.Suit;
import com.example.bridge.ui.game.GameActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private HistoryListAdapter adapter;
    private List<JSONObject> fullHistoryList;
    private List<JSONObject> filteredList;
    private TextView tvEmpty;
    private CheckBox cbOnlySaved;
    private Spinner spinnerLevel, spinnerSuit, spinnerResult;
    private SharedPref sharedPref;

    @Override
    protected void attachBaseContext(Context newBase) {
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

        String json = getSharedPreferences(SharedPref.PREFS_NAME, MODE_PRIVATE).getString(SharedPref.KEY_HISTORY, "[]");

        fullHistoryList = new ArrayList<>();
        try {
            if (json.trim().startsWith("[")) {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    fullHistoryList.add(array.getJSONObject(i));
                }
            } else if (json.trim().startsWith("{")) {
                fullHistoryList.add(new JSONObject(json));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filteredList = new ArrayList<>(fullHistoryList);
        
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryListAdapter(filteredList, this::showDeleteDialog, this::toggleSave, item -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("replayedGameJson", item.toString());
            startActivity(intent);
        });
        rvHistory.setAdapter(adapter);

        setupFilters();
        applyFilters();
    }

    private void setupFilters() {
        String[] levelOptions = { getString(R.string.filter_level_all), "1", "2", "3", "4", "5", "6", "7" };
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

        String[] suitOptions = {
                getString(R.string.filter_suit_all), getString(R.string.filter_contract_nt),
                getString(R.string.filter_contract_spades), getString(R.string.filter_contract_hearts),
                getString(R.string.filter_contract_diamonds), getString(R.string.filter_contract_clubs)
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

        String[] resultOptions = {
                getString(R.string.filter_result_all), getString(R.string.filter_result_won), getString(R.string.filter_result_lost)
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
                if (onlySaved && !gameWrapper.optBoolean("isSaved", false)) continue;

                JSONObject game = gameWrapper.has("data") ? gameWrapper.getJSONObject("data") : gameWrapper;
                
                String contractStr = game.optString("Contract", game.optString("contract", "PASS"));
                int tricks = game.optInt("Result", game.optInt("snTricks", 0));

                boolean won = true;
                int contractLevel = 0;
                if (!contractStr.toUpperCase().contains("PASS")) {
                    String[] conParts = contractStr.split("(?<=\\d)(?=\\D)| "); // Split by space or between digit and non-digit
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
        tvEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void toggleSave(int position) {
        try {
            JSONObject item = filteredList.get(position);
            boolean currentStatus = item.optBoolean("isSaved", false);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(currentStatus ? R.string.unhighlight_confirm_message : R.string.highlight_confirm_message)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        try {
                            item.put("isSaved", !currentStatus);
                            saveHistory();
                            applyFilters();
                        } catch (Exception e) { e.printStackTrace(); }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showDeleteDialog(int position) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    JSONObject itemToRemove = filteredList.get(position);
                    fullHistoryList.remove(itemToRemove);
                    saveHistory();
                    applyFilters();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    public static void saveGameToHistory(Context context, String gameJson) {
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences(SharedPref.PREFS_NAME, Context.MODE_PRIVATE);
            String existing = prefs.getString(SharedPref.KEY_HISTORY, "[]");
            JSONArray historyArray = new JSONArray(existing);

            // Save the entire collection of systems as one game entry
            JSONArray newGamesBatch = new JSONArray(gameJson);
            if (newGamesBatch.length() > 0) {
                historyArray.put(newGamesBatch);
            }

            prefs.edit().putString(SharedPref.KEY_HISTORY, historyArray.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveHistory() {
        JSONArray array = new JSONArray();
        for (JSONObject obj : fullHistoryList) {
            array.put(obj);
        }
        getSharedPreferences(SharedPref.PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(SharedPref.KEY_HISTORY, array.toString())
                .apply();
    }
}
