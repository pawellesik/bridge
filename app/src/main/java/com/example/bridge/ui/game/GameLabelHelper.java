package com.example.bridge.ui.game;

import android.content.Context;
import com.example.bridge.R;
import com.example.bridge.model.Player;
import java.util.Locale;

public class GameLabelHelper {

    public static String getFormattedPlayerName(Context context, String playerDirection, Player player, String gameMode) {
        int resId = 0;
        if (playerDirection != null) {
            switch (playerDirection.toUpperCase()) {
                case "N": resId = R.string.player_north; break;
                case "S": resId = R.string.player_south; break;
                case "E": resId = R.string.player_east; break;
                case "W": resId = R.string.player_west; break;
            }
        }

        if (resId == 0) return playerDirection != null ? playerDirection : "";
        String baseName = context.getString(resId);

        if ("single".equals(gameMode) && player != null && ("N".equals(playerDirection) || "S".equals(playerDirection))) {
            return String.format(Locale.US, "%s %d HCP", baseName, player.getInitialHCP());
        }

        return baseName;
    }
}
