package com.example.bridge.ui.history;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bridge.R;
import com.example.bridge.ui.game.GameActivity;
import org.json.JSONObject;

public class OverlayHistoryGame {

    private final GameActivity activity;
    private final View root;
    private int currentDbId = -1;

    public OverlayHistoryGame(GameActivity activity) {
        this.activity = activity;
        this.root = activity.findViewById(R.id.history_game_overlay);
        initViews();
    }

    private void initViews() {
        if (root == null) return;
        // Basic initialization, similar to OverlayHistory
    }

    public void showGame(int dbId) {
        this.currentDbId = dbId;
        if (root != null) {
            root.setVisibility(View.VISIBLE);
            loadGameData(dbId);
        }
    }

    private void loadGameData(int dbId) {
        new Thread(() -> {
            try {
                com.example.bridge.core.db.GameRecord record = com.example.bridge.core.db.AppDatabase.getInstance(activity).gameDao().getById(dbId);
                if (record != null) {
                    android.util.Log.d("plesik", "Loaded game: " + record.system + " - " + record.gameData);
                    // Update UI with game data here (on main thread)
                }
            } catch (Exception e) {
                android.util.Log.e("plesik", "Error loading game data", e);
            }
        }).start();
    }

    public void hide() {
        if (root != null) {
            root.setVisibility(View.GONE);
        }
    }

    public boolean isVisible() {
        return root != null && root.getVisibility() == View.VISIBLE;
    }
}
