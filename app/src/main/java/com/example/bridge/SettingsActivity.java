package com.example.bridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences("BridgePrefs", MODE_PRIVATE);

        setupCardColors(prefs);
        setupQuickGame(prefs);
        setupSingleplayer(prefs);

        findViewById(R.id.btn_back_settings).setOnClickListener(v -> finish());
    }

    private void setupCardColors(SharedPreferences prefs) {
        boolean isColorful = prefs.getBoolean("card_colors_colorful", true);
        RadioGroup rg = findViewById(R.id.rg_card_colors);
        if (isColorful) {
            ((RadioButton)findViewById(R.id.rb_colorful)).setChecked(true);
        } else {
            ((RadioButton)findViewById(R.id.rb_standard)).setChecked(true);
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            boolean colorful = (checkedId == R.id.rb_colorful);
            prefs.edit().putBoolean("card_colors_colorful", colorful).apply();
        });
    }

    private void setupQuickGame(SharedPreferences prefs) {
        String difficulty = prefs.getString("quick_game_difficulty", "Medium");
        RadioGroup rg = findViewById(R.id.rg_difficulty);
        
        if ("Easy".equals(difficulty)) ((RadioButton)findViewById(R.id.rb_easy)).setChecked(true);
        else if ("Hard".equals(difficulty)) ((RadioButton)findViewById(R.id.rb_hard)).setChecked(true);
        else ((RadioButton)findViewById(R.id.rb_medium)).setChecked(true);

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            String newDifficulty = "Medium";
            if (checkedId == R.id.rb_easy) newDifficulty = "Easy";
            else if (checkedId == R.id.rb_hard) newDifficulty = "Hard";
            prefs.edit().putString("quick_game_difficulty", newDifficulty).apply();
        });
    }

    private void setupSingleplayer(SharedPreferences prefs) {
        Spinner spinner = findViewById(R.id.spinner_bidding_system);
        String[] systems = {"SAYC", "WJ", "NAT+c"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, systems) {
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
                    v.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32"));
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
