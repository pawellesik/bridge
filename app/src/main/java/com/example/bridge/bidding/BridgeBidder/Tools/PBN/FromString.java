package com.example.bridge.bidding.BridgeBidder.Tools.PBN;

import com.example.bridge.bidding.BridgeBidder.Tools.Game;
import java.util.ArrayList;
import java.util.List;

public class FromString {
    public static List<Game> parseGames(String text) {
        List<String> gameTextList = new ArrayList<>();
        String[] lines = text.split("\\r?\\n");
        StringBuilder gameText = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                if (gameText.length() > 0) {
                    gameTextList.add(gameText.toString());
                    gameText.setLength(0);
                }
            } else {
                gameText.append(line).append("\n");
            }
        }
        if (gameText.length() > 0) {
            gameTextList.add(gameText.toString());
        }

        List<Game> games = new ArrayList<>();
        for (String gt : gameTextList) {
            games.add(Game.parse(gt));
        }
        return games;
    }

    public static List<PBNTag> tokenizeTags(String text) {
        List<PBNTag> tags = new ArrayList<>();
        String[] lines = text.split("\\r?\\n");
        PBNTag currentTag = null;
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("%") || trimmed.startsWith(";")) continue;
            if (trimmed.startsWith("[")) {
                currentTag = new PBNTag();
                tags.add(currentTag);
                int spaceIndex = trimmed.indexOf(' ');
                currentTag.name = trimmed.substring(1, spaceIndex);
                int firstQuote = trimmed.indexOf('"');
                int lastQuote = trimmed.lastIndexOf('"');
                currentTag.value = trimmed.substring(firstQuote + 1, lastQuote);
            } else if (currentTag != null) {
                currentTag.data.add(trimmed);
            }
        }
        return tags;
    }

    public static class PBNTag {
        public String name;
        public String value;
        public List<String> data = new ArrayList<>();
    }
}




























































































































































































































































































































































































































































































