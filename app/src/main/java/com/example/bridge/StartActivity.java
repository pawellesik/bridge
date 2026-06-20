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

        View loadingIndicator = findViewById(R.id.loading_indicator);
        View menuContainer = findViewById(R.id.menu_container);
        Button btnStart = findViewById(R.id.btn_start);
        
        btnStart.setOnClickListener(v -> {
            if (loadingIndicator != null) loadingIndicator.setVisibility(View.VISIBLE);
            if (menuContainer != null) menuContainer.setVisibility(View.GONE);

            Intent intent = new Intent(StartActivity.this, GameActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_history).setOnClickListener(v -> {
            // Future use
        });

        findViewById(R.id.btn_settings).setOnClickListener(v -> {
            // Future use
        });

        findViewById(R.id.btn_about).setOnClickListener(v -> {
            // Future use
        });

        findViewById(R.id.btn_quit).setOnClickListener(v -> {
            finishAffinity();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset UI when returning to this screen
        View loadingIndicator = findViewById(R.id.loading_indicator);
        View menuContainer = findViewById(R.id.menu_container);

        if (loadingIndicator != null) loadingIndicator.setVisibility(View.GONE);
        if (menuContainer != null) menuContainer.setVisibility(View.VISIBLE);
    }

    private void changeLanguage(String lang) {
        LocaleHelper.setLocale(this, lang);
        recreate(); // Refresh activity to apply new strings
    }
}
