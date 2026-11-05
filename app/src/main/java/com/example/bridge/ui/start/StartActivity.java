package com.example.bridge.ui.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bridge.R;
import com.example.bridge.core.LocaleHelper;
import com.example.bridge.ui.game.GameActivity;

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
        updateLanguageButtonStates();

        findViewById(R.id.btn_start).setOnClickListener(v -> launchGame("quick"));
        findViewById(R.id.btn_single).setOnClickListener(v -> launchGame("single"));
        findViewById(R.id.btn_multiplayer).setOnClickListener(v -> launchGame("multi"));


        View moreContainer = findViewById(R.id.more_options_container);
        findViewById(R.id.btn_more).setOnClickListener(v -> {
            if (moreContainer != null) {
                boolean isVisible = moreContainer.getVisibility() == View.VISIBLE;
                moreContainer.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            }
        });

        findViewById(R.id.btn_create_deal).setOnClickListener(v -> launchGame("create"));


        findViewById(R.id.btn_about).setOnClickListener(v -> {
            // Future use
        });
    }

    private void launchGame(String mode) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        View menuContainer = findViewById(R.id.menu_container);

        if (loadingIndicator != null) loadingIndicator.setVisibility(View.VISIBLE);
        if (menuContainer != null) menuContainer.setVisibility(View.GONE);

        Intent intent = new Intent(StartActivity.this, GameActivity.class);
        intent.putExtra("GAME_MODE", mode);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset UI when returning to this screen
        View loadingIndicator = findViewById(R.id.loading_indicator);
        View menuContainer = findViewById(R.id.menu_container);

        if (loadingIndicator != null) loadingIndicator.setVisibility(View.GONE);
        if (menuContainer != null) menuContainer.setVisibility(View.VISIBLE);
        updateLanguageButtonStates();
    }

    private void updateLanguageButtonStates() {
        String currentLang = LocaleHelper.getLanguage(this);
        View btnEn = findViewById(R.id.btn_lang_en);
        View btnPl = findViewById(R.id.btn_lang_pl);

        if (btnEn != null && btnPl != null) {
            if ("pl".equalsIgnoreCase(currentLang)) {
                btnEn.setAlpha(0.5f);
                btnPl.setAlpha(1.0f);
            } else {
                btnEn.setAlpha(1.0f);
                btnPl.setAlpha(0.5f);
            }
        }
    }

    private void changeLanguage(String lang) {
        String currentLang = LocaleHelper.getLanguage(this);
        if (currentLang != null && currentLang.equalsIgnoreCase(lang)) {
            return; // Already selected, do nothing
        }
        LocaleHelper.setLocale(this, lang);
        recreate();
    }
}
