package com.example.bridge.ui.game;

import android.widget.TextView;
import com.example.bridge.R;
import com.example.bridge.model.Player;

import java.util.Map;

public class GamePlayerLabels {
    private TextView viewNorth, viewSouth, viewEast, viewWest;
    private final GameActivity activity;
    private boolean isRotated = false;

    public GamePlayerLabels(GameActivity activity) {
        this.activity = activity;
        resetViews();
    }

    public void resetViews() {
        this.viewNorth = activity.findViewById(R.id.name_north);
        this.viewSouth = activity.findViewById(R.id.name_south);
        this.viewEast = activity.findViewById(R.id.name_east);
        this.viewWest = activity.findViewById(R.id.name_west);
        this.isRotated = false;
    }

    public void setRotated(boolean rotated) {
        this.isRotated = rotated;
    }

    public void updateAll(Map<String, Player> players, String gameMode) {
        updateLabel("N", players.get("N"), gameMode);
        updateLabel("S", players.get("S"), gameMode);
        updateLabel("E", players.get("E"), gameMode);
        updateLabel("W", players.get("W"), gameMode);
    }

    public void updateLabel(String playerDirection, Player player, String gameMode) {
        String logicalDirectionForLabel = playerDirection;
        if (isRotated && playerDirection != null) {
            switch (playerDirection.toUpperCase()) {
                case "N": logicalDirectionForLabel = "S"; break;
                case "S": logicalDirectionForLabel = "N"; break;
                case "E": logicalDirectionForLabel = "W"; break;
                case "W": logicalDirectionForLabel = "E"; break;
            }
        }
        TextView tv = getTextView(playerDirection);
        if (tv != null) {
            tv.setText(GameLabelHelper.getFormattedPlayerName(activity, logicalDirectionForLabel, player, gameMode));
        }
    }

    public void updateTurn(String activePlayerDirection) {
        viewNorth.setBackgroundResource(0);
        viewSouth.setBackgroundResource(0);
        viewEast.setBackgroundResource(0);
        viewWest.setBackgroundResource(0);

        if (activePlayerDirection == null) return;

        TextView activeTv = getTextView(activePlayerDirection);
        if (activeTv != null) {
            activeTv.setBackgroundResource(R.drawable.transparent_white_frame);
        }
    }

    private TextView getTextView(String playerDirection) {
        if (playerDirection == null) return null;
        switch (playerDirection.toUpperCase()) {
            case "N": return viewNorth;
            case "S": return viewSouth;
            case "E": return viewEast;
            case "W": return viewWest;
            default: return null;
        }
    }
}
