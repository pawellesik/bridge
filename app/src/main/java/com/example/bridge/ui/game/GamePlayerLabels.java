package com.example.bridge.ui.game;

import android.widget.TextView;
import com.example.bridge.R;
import com.example.bridge.model.Player;

import java.util.Map;

public class GamePlayerLabels {
    private TextView viewNorth, viewSouth, viewEast, viewWest;
    private final GameActivity activity;

    public GamePlayerLabels(GameActivity activity) {
        this.activity = activity;
        resetViews();
    }

    public void resetViews() {
        this.viewNorth = activity.findViewById(R.id.name_north);
        this.viewSouth = activity.findViewById(R.id.name_south);
        this.viewEast = activity.findViewById(R.id.name_east);
        this.viewWest = activity.findViewById(R.id.name_west);
    }

    public void swapNS() {
        TextView temp = viewNorth;
        viewNorth = viewSouth;
        viewSouth = temp;
    }

    public void swapEW() {
        TextView temp = viewEast;
        viewEast = viewWest;
        viewWest = temp;
    }

    public void updateAll(Map<String, Player> players, String gameMode) {
        updateLabel("North", players.get("North"), gameMode);
        updateLabel("South", players.get("South"), gameMode);
        updateLabel("East", players.get("East"), gameMode);
        updateLabel("West", players.get("West"), gameMode);
    }

    public void updateLabel(String playerName, Player player, String gameMode) {
        TextView tv = getTextView(playerName);
        if (tv != null) {
            tv.setText(GameLabelHelper.getFormattedPlayerName(activity, playerName, player, gameMode));
        }
    }

    public void updateTurn(String activePlayerName) {
        viewNorth.setBackgroundResource(0);
        viewSouth.setBackgroundResource(0);
        viewEast.setBackgroundResource(0);
        viewWest.setBackgroundResource(0);

        if (activePlayerName == null) return;

        TextView activeTv = getTextView(activePlayerName);
        if (activeTv != null) {
            activeTv.setBackgroundResource(R.drawable.transparent_white_frame);
        }
    }

    private TextView getTextView(String playerName) {
        switch (playerName) {
            case "North": return viewNorth;
            case "South": return viewSouth;
            case "East": return viewEast;
            case "West": return viewWest;
            default: return null;
        }
    }
}
