package com.example.bridge.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bridge.R;
import com.example.bridge.ui.game.GameActivity;
import com.example.bridge.ui.game.GameController;

public class OverlaySettings {
    private final GameActivity activity;
    private final com.example.bridge.core.DataStoreManager dataStoreManager;

    public OverlaySettings(GameActivity activity) {
        this.activity = activity;
        this.dataStoreManager = com.example.bridge.core.DataStoreManager.getInstance(activity);
        setup();
    }

    private void setup() {
        if (activity.getSettingsOverlay() == null) return;
        setupCardColors();
        setupQuickGame();
        setupSingleplayer();
        

    }

    private void setupCardColors() {
        boolean isColorful;
        try {
            isColorful = dataStoreManager.getPreference(com.example.bridge.core.DataStoreManager.CARD_COLORS_COLORFUL, true)
                    .firstOrError()
                    .onErrorReturnItem(true)
                    .blockingGet();
        } catch (Exception e) {
            isColorful = true;
        }

        RadioGroup rg = activity.getSettingsOverlay().findViewById(R.id.rg_card_colors);
        if (rg == null) return;

        if (isColorful) {
            ((RadioButton) activity.getSettingsOverlay().findViewById(R.id.rb_colorful)).setChecked(true);
        } else {
            ((RadioButton) activity.getSettingsOverlay().findViewById(R.id.rb_standard)).setChecked(true);
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            boolean colorful = (checkedId == R.id.rb_colorful);
            android.util.Log.d("plesik", "Saving card_colors_colorful: " + colorful);
            dataStoreManager.setPreference(com.example.bridge.core.DataStoreManager.CARD_COLORS_COLORFUL, colorful)
                    .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                    .subscribe();
            activity.refreshAllColors();
        });
    }

    private void setupQuickGame() {
        String difficulty;
        try {
            difficulty = dataStoreManager.getPreference(com.example.bridge.core.DataStoreManager.QUICK_GAME_DIFFICULTY, "Medium")
                    .firstOrError()
                    .onErrorReturnItem("Medium")
                    .blockingGet();
        } catch (Exception e) {
            difficulty = "Medium";
        }

        RadioGroup rg = activity.getSettingsOverlay().findViewById(R.id.rg_difficulty);
        if (rg == null) return;
        
        if ("Easy".equals(difficulty)) ((RadioButton)activity.getSettingsOverlay().findViewById(R.id.rb_easy)).setChecked(true);
        else if ("Hard".equals(difficulty)) ((RadioButton)activity.getSettingsOverlay().findViewById(R.id.rb_hard)).setChecked(true);
        else ((RadioButton)activity.getSettingsOverlay().findViewById(R.id.rb_medium)).setChecked(true);

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            String newDifficulty = "Medium";
            if (checkedId == R.id.rb_easy) newDifficulty = "Easy";
            else if (checkedId == R.id.rb_hard) newDifficulty = "Hard";
            dataStoreManager.setPreference(com.example.bridge.core.DataStoreManager.QUICK_GAME_DIFFICULTY, newDifficulty)
                    .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                    .subscribe();
        });
    }

    private void setupSingleplayer() {
        Spinner spinner = activity.getSettingsOverlay().findViewById(R.id.spinner_bidding_system);
        if (spinner == null) return;
        
        String[] systems = {"SAYC", "WJ", "NAT+c"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, systems) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView) {
                    ((TextView) v).setTextColor(android.graphics.Color.WHITE);
                    ((TextView) v).setTextSize(16);
                }
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (v instanceof TextView) {
                    ((TextView) v).setTextColor(android.graphics.Color.WHITE);
                    v.setBackgroundColor(android.graphics.Color.parseColor("#1A3026"));
                }
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String savedSystem;
        try {
            savedSystem = dataStoreManager.getPreference(com.example.bridge.core.DataStoreManager.BIDDING_SYSTEM, "SAYC")
                    .firstOrError()
                    .onErrorReturnItem("SAYC")
                    .blockingGet();
        } catch (Exception e) {
            savedSystem = "SAYC";
        }

        for (int i = 0; i < systems.length; i++) {
            if (systems[i].equals(savedSystem)) {
                spinner.setSelection(i);
                break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataStoreManager.setPreference(com.example.bridge.core.DataStoreManager.BIDDING_SYSTEM, systems[position])
                        .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                        .subscribe();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
