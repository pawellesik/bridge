package com.example.bridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        
        View titleView = findViewById(R.id.title_text);
        if (titleView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(titleView.getRootView(), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        findViewById(R.id.btn_lang_en).setOnClickListener(v -> changeLanguage("en"));
        findViewById(R.id.btn_lang_pl).setOnClickListener(v -> changeLanguage("pl"));

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, GameActivity.class);
            startActivity(intent);
        });
    }

    private void changeLanguage(String lang) {
        LocaleHelper.setLocale(this, lang);
        recreate(); // Refresh activity to apply new strings
    }
}
