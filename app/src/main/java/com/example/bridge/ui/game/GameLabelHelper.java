package com.example.bridge.ui.game;

import android.content.Context;
import com.example.bridge.R;
import com.example.bridge.model.Player;
import java.util.Locale;

public class GameLabelHelper {

    public static String getFormattedPlayerName(Context context, String playerName, Player player, String gameMode) {
        int resId = 0;
        switch (playerName) {
            case "North": resId = R.string.player_north; break;
            case "South": resId = R.string.player_south; break;
            case "East": resId = R.string.player_east; break;
            case "West": resId = R.string.player_west; break;
        }

        if (resId == 0) return playerName;
        String baseName = context.getString(resId);

        if ("single".equals(gameMode) && player != null && ("North".equals(playerName) || "South".equals(playerName))) {
            return String.format(Locale.US, "%s %d HCP", baseName, player.calculateHCP());
        }

        return baseName;
    }
}
