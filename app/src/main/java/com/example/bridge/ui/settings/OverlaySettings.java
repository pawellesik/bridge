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
    }

    private void setupCardColors() {
        boolean isColorful = settingsManager.isCardColorsColorful();

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
            settingsManager.setCardColorsColorful(colorful);
            activity.refreshAllColors();
        });
    }

    private void setupQuickGame() {

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
}
