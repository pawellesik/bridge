package com.example.bridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
        boolean isColorful = prefs.getBoolean("card_colors_colorful", true);

        RadioGroup rg = findViewById(R.id.rg_card_colors);
        RadioButton rbColorful = findViewById(R.id.rb_colorful);
        RadioButton rbStandard = findViewById(R.id.rb_standard);

        if (isColorful) {
            rbColorful.setChecked(true);
        } else {
            rbStandard.setChecked(true);
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            boolean colorful = (checkedId == R.id.rb_colorful);
            prefs.edit().putBoolean("card_colors_colorful", colorful).apply();
        });

        findViewById(R.id.btn_back_settings).setOnClickListener(v -> finish());
        // For standard system back button support, no code needed if not using Toolbar
    }
}
