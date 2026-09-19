package com.example.bridge.ui.settings;

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
import com.example.bridge.core.SettingsManager;
import com.example.bridge.ui.game.GameActivity;

public class OverlaySettings {
    private final GameActivity activity;
    private final SettingsManager settingsManager;

    public OverlaySettings(GameActivity activity) {
        this.activity = activity;
        this.settingsManager = SettingsManager.getInstance(activity);
        setup();
    }

    private void setup() {
        if (activity.getSettingsOverlay() == null) return;
        setupCardColors();
        setupQuickGame();
        setupSingleplayer();
        setupTestPbnSwitch();
        setupExplanationSwitch();
        setupHistorySize();
    }

    private void setupCardColors() {
        com.google.android.material.materialswitch.MaterialSwitch switchCardColors = activity.getSettingsOverlay().findViewById(R.id.switch_card_colors);
        if (switchCardColors == null) return;

        switchCardColors.setChecked(settingsManager.isCardColorsColorful());

        switchCardColors.setOnCheckedChangeListener((buttonView, isChecked) -> {
            android.util.Log.d("plesik", "Saving card_colors_colorful: " + isChecked);
            settingsManager.setCardColorsColorful(isChecked);
            activity.refreshAllColors();
        });
    }

    private void setupQuickGame() {
        com.google.android.material.materialswitch.MaterialSwitch switchWinningOnly = activity.getSettingsOverlay().findViewById(R.id.switch_winning_only);
        if (switchWinningOnly == null) return;

        switchWinningOnly.setChecked(settingsManager.isQuickGameWinningOnly());

        switchWinningOnly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setQuickGameWinningOnly(isChecked);
        });
    }

    private void setupSingleplayer() {

        String[] systems = {"SAYC", "WJ", "NatC"};
        
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
        String savedSystem = settingsManager.getBiddingSystem();

        for (int i = 0; i < systems.length; i++) {
            if (systems[i].equals(savedSystem)) {
                break;
            }
        }

    }

    private void setupTestPbnSwitch() {
        androidx.appcompat.widget.SwitchCompat switchTestPbn = activity.getSettingsOverlay().findViewById(R.id.switch_load_test_pbn);
        if (switchTestPbn == null) return;

        switchTestPbn.setChecked(settingsManager.isLoadFromTestPbn());

        switchTestPbn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setLoadFromTestPbn(isChecked);
        });
    }

    private void setupExplanationSwitch() {
        com.google.android.material.materialswitch.MaterialSwitch switchExplanation = activity.getSettingsOverlay().findViewById(R.id.switch_explanation);
        if (switchExplanation == null) return;

        switchExplanation.setChecked(settingsManager.isShowExplanation());

        switchExplanation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setShowExplanation(isChecked);
        });
    }

    private void setupHistorySize() {
        android.widget.SeekBar seekHistorySize = activity.getSettingsOverlay().findViewById(R.id.seek_history_size);
        TextView tvHistorySizeValue = activity.getSettingsOverlay().findViewById(R.id.tv_history_size_value);
        if (seekHistorySize == null || tvHistorySizeValue == null) return;

        int step = 5;
        int savedSize = settingsManager.getHistorySize();
        int snappedSaved = Math.round((float) savedSize / step) * step;
        if (snappedSaved > 1000) snappedSaved = 1000;
        if (snappedSaved < 0) snappedSaved = 0;

        seekHistorySize.setProgress(snappedSaved);
        tvHistorySizeValue.setText(String.valueOf(snappedSaved));

        seekHistorySize.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                int snappedProgress = Math.round((float) progress / step) * step;
                if (snappedProgress > 1000) snappedProgress = 1000;
                if (snappedProgress < 0) snappedProgress = 0;

                if (progress != snappedProgress) {
                    seekBar.setProgress(snappedProgress);
                    return;
                }

                tvHistorySizeValue.setText(String.valueOf(snappedProgress));
                if (fromUser) {
                    settingsManager.setHistorySize(snappedProgress);
                }
            }

            @Override
            public void onStartTrackingTouch(android.widget.SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(android.widget.SeekBar seekBar) {}
        });
    }
}
