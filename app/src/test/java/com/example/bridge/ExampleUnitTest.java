package com.example.bridge;

import com.example.bridge.bidding.Tools.Game;
import com.example.bridge.ui.history.Pbn;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPbnJsonBiddingSystem() throws Exception {
        Pbn pbn = new Pbn(null, "1");
        pbn.setBidSystemNS("SAYC");
        pbn.setBidSystemEW("Precision");

        JSONObject json = pbn.toJsonObject();
        assertEquals("SAYC", json.optString("BidSystemNS"));
        assertEquals("Precision", json.optString("BidSystemEW"));

        Pbn loaded = new Pbn(null, "1");
        loaded.loadFromJsonObject(json);
        assertEquals("SAYC", loaded.getBidSystemNS());
        assertEquals("Precision", loaded.getBidSystemEW());

        Game game = loaded.toGame();
        assertEquals("SAYC", game.bidSystemNS);
        assertEquals("Precision", game.bidSystemEW);

        String pbnString = loaded.generatePbn();
        assertTrue(pbnString.contains("[BidSystemNS \"SAYC\"]"));
        assertTrue(pbnString.contains("[BidSystemEW \"Precision\"]"));

        Game parsedGame = Game.parse(pbnString);
        assertEquals("SAYC", parsedGame.bidSystemNS);
        assertEquals("Precision", parsedGame.bidSystemEW);
    }
}