package com.example.bridge.ui.game;

import android.widget.TextView;
import com.example.bridge.R;
import com.example.bridge.model.Player;

import java.util.Map;

public class GamePlayerLabels {
    private final TextView nameNorth, nameSouth, nameEast, nameWest;
    private final GameActivity activity;

    public GamePlayerLabels(GameActivity activity) {
        this.activity = activity;
        this.nameNorth = activity.findViewById(R.id.name_north);
        this.nameSouth = activity.findViewById(R.id.name_south);
        this.nameEast = activity.findViewById(R.id.name_east);
        this.nameWest = activity.findViewById(R.id.name_west);
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
        nameNorth.setBackgroundResource(0);
        nameSouth.setBackgroundResource(0);
        nameEast.setBackgroundResource(0);
        nameWest.setBackgroundResource(0);

        if (activePlayerName == null) return;

        TextView activeTv = getTextView(activePlayerName);
        if (activeTv != null) {
            activeTv.setBackgroundResource(R.drawable.transparent_white_frame);
        }
    }

    private TextView getTextView(String playerName) {
        switch (playerName) {
            case "North": return nameNorth;
            case "South": return nameSouth;
            case "East": return nameEast;
            case "West": return nameWest;
            default: return null;
        }
    }
}
