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
import com.example.bridge.ui.game.GameBidding;
import com.example.bridge.ui.game.GameController;

public class OverlaySettings {
    private final GameActivity activity;
    private final View overlay;
    private final SharedPreferences prefs;
    private final GameController gameController;
    private final GameBidding gameBidding;

    public OverlaySettings(GameActivity activity, View overlay, GameController gameController, GameBidding gameBidding) {
        this.activity = activity;
        this.overlay = overlay;
        this.gameController = gameController;
        this.gameBidding = gameBidding;
        this.prefs = activity.getSharedPreferences("BridgePrefs", Context.MODE_PRIVATE);
        setup();
    }

    private void setup() {
        if (overlay == null) return;
        setupCardColors();
        setupQuickGame();
        setupSingleplayer();
        
        View btnBack = overlay.findViewById(R.id.btn_back_settings);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                overlay.setVisibility(View.GONE);
                // Return to game view in bottom nav
                com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = activity.findViewById(R.id.bottom_navigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_game);
                }
            });
        }
    }

    private void setupCardColors() {
        boolean isColorful = prefs.getBoolean("card_colors_colorful", true);
        RadioGroup rg = overlay.findViewById(R.id.rg_card_colors);
        if (rg == null) return;

        if (isColorful) {
            ((RadioButton) overlay.findViewById(R.id.rb_colorful)).setChecked(true);
        } else {
            ((RadioButton) overlay.findViewById(R.id.rb_standard)).setChecked(true);
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            boolean colorful = (checkedId == R.id.rb_colorful);
            prefs.edit().putBoolean("card_colors_colorful", colorful).commit();
            activity.getSouthAdapter().setCardsEnabled(false);
            activity.getNorthAdapter().setCardsEnabled(false);
            //if (gameBidding != null) gameBidding.applyColors();
        });
    }

    private void setupQuickGame() {
        String difficulty = prefs.getString("quick_game_difficulty", "Medium");
        RadioGroup rg = overlay.findViewById(R.id.rg_difficulty);
        if (rg == null) return;
        
        if ("Easy".equals(difficulty)) ((RadioButton)overlay.findViewById(R.id.rb_easy)).setChecked(true);
        else if ("Hard".equals(difficulty)) ((RadioButton)overlay.findViewById(R.id.rb_hard)).setChecked(true);
        else ((RadioButton)overlay.findViewById(R.id.rb_medium)).setChecked(true);

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            String newDifficulty = "Medium";
            if (checkedId == R.id.rb_easy) newDifficulty = "Easy";
            else if (checkedId == R.id.rb_hard) newDifficulty = "Hard";
            prefs.edit().putString("quick_game_difficulty", newDifficulty).apply();
        });
    }

    private void setupSingleplayer() {
        Spinner spinner = overlay.findViewById(R.id.spinner_bidding_system);
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

        String savedSystem = prefs.getString("bidding_system", "SAYC");
        for (int i = 0; i < systems.length; i++) {
            if (systems[i].equals(savedSystem)) {
                spinner.setSelection(i);
                break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putString("bidding_system", systems[position]).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
