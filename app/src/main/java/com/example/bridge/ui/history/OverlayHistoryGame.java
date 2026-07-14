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
                java.util.List<com.example.bridge.core.db.GameRecord> records = com.example.bridge.core.db.AppDatabase.getInstance(activity).gameDao().getGamesByDealId(dbId);
                if (records != null && !records.isEmpty()) {
                    for (com.example.bridge.core.db.GameRecord record : records) {
                        android.util.Log.d("plesik", "Loaded game system: " + record.system + " - " + record.gameData);
                    }

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
