package com.example.bridge.ui.statistic;

import android.view.View;
import android.widget.Toast;

import com.example.bridge.R;
import com.example.bridge.ui.game.GameActivity;
import com.google.android.material.button.MaterialButton;

public class OverlayStatistic {

    private final GameActivity activity;
    private final View root;

    private MaterialButton btnClearJustDeclare;
    private MaterialButton btnClearSingleplayer;
    private MaterialButton btnClearMultiplayer;

    public OverlayStatistic(GameActivity activity) {
        this.activity = activity;
        this.root = activity.findViewById(R.id.statistic_overlay);
        initViews();
    }

    private void initViews() {
        if (root == null) return;

        btnClearJustDeclare = root.findViewById(R.id.btn_clear_just_declare);
        btnClearSingleplayer = root.findViewById(R.id.btn_clear_singleplayer);
        btnClearMultiplayer = root.findViewById(R.id.btn_clear_multiplayer);

        if (btnClearJustDeclare != null) {
            btnClearJustDeclare.setOnClickListener(v -> clearSessionStats("Just Declare"));
        }

        if (btnClearSingleplayer != null) {
            btnClearSingleplayer.setOnClickListener(v -> clearSessionStats("Singleplayer"));
        }

        if (btnClearMultiplayer != null) {
            btnClearMultiplayer.setOnClickListener(v -> clearSessionStats("Multiplayer"));
        }
    }

    private void clearSessionStats(String mode) {
        Toast.makeText(activity, "Cleared session statistics for " + mode, Toast.LENGTH_SHORT).show();
        refresh();
    }

    public void refresh() {
        // Refresh statistics views if needed
    }

    public void show() {
        if (root != null) {
            root.setVisibility(View.VISIBLE);
            refresh();
        }
    }

    public void hide() {
        if (root != null) {
            root.setVisibility(View.GONE);
        }
    }

    public boolean isVisible() {
        return root != null && root.getVisibility() == View.VISIBLE;
    }

    public View getRoot() {
        return root;
    }
}
