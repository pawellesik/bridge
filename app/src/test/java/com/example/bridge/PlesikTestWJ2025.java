package com.example.bridge;

import static org.junit.Assert.assertEquals;

import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.BridgeBidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.Game;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.runTests.PBNTest;
import com.example.bridge.runTests.PBNUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class PlesikTestWJ2025 {

    private final PBNTest test;

    public PlesikTestWJ2025(PBNTest test) {
        this.test = test;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        List<Object[]> result = new ArrayList<>();

        String[] possiblePaths = {
                "app/src/test/java/com/example/bridge/",
                "src/test/java/com/example/bridge/"
        };

        // Load files from WJ2025 directory
        for (String basePath : possiblePaths) {
            File wjDir = new File(basePath + "WJ2025");
            if (wjDir.exists() && wjDir.isDirectory()) {
                File[] files = wjDir.listFiles((dir, name) -> new File(dir, name).isFile());
                if (files != null) {
                    for (File file : files) {
                        try {
                            String content = new String(Files.readAllBytes(file.toPath()));
                            List<PBNTest> pbnTests = PBNUtils.importTests(content);
                            for (PBNTest t : pbnTests) {
                                t.setName("[" + file.getName() + "] " + t.getName());
                                result.add(new Object[]{t});
                            }
                        } catch (IOException e) {
                            // continue
                        }
                    }
                }
                break;
            }
        }

        // Also load test.pbn if present
        try {
            String content = loadPbnFileStatic("test.pbn");
            List<PBNTest> pbnTests = PBNUtils.importTests(content);
            for (PBNTest t : pbnTests) {
                t.setName("[test.pbn] " + t.getName());
                result.add(new Object[]{t});
            }
        } catch (Exception e) {
            // ignore if test.pbn not found
        }

        return result;
    }

    @Test
    public void testWJ2025() {
        String suggestion = BridgeBidder.suggestBid(test.getDeal(), test.getVulnerable(), test.getAuction(), "WJ2025", "PassOnly");

        if (!test.getExpectedCall().equals(suggestion)) {
            // Detailed breakdown on failure (Identical to NatCTest)
            System.err.println("\n--- DETAILED LICYTACJA BREAKDOWN (WJ2025) ---");
            Game game = Game.parse(test.getDeal(), test.getVulnerable());
            game.bidSystemNS = "WJ2025";
            game.bidSystemEW = "PassOnly";
            BiddingState debugState = new BiddingState(game);

            String auctionStr = test.getAuction();
            if (auctionStr != null && !auctionStr.trim().isEmpty()) {
                String[] existingAuction = auctionStr.split("\\s+");
                for (String bidStr : existingAuction) {
                    if (bidStr.trim().isEmpty()) continue;
                    PositionCalls choices = debugState.getCallChoices();
                    Call actualCall = Call.parse(bidStr);
                    CallDetails details = choices.get(actualCall);

                    String logId = (details != null) ? details.getMatchedLogID(debugState.getNextToAct()) : "UNKNOWN";
                    System.err.println(debugState.getNextToAct().getDirection() + " licytuje: " + bidStr + " [LogID: " + logId + "]");

                    debugState.makeCall(actualCall);
                }
            }

            PositionCalls suggestionChoices = debugState.getCallChoices();
            CallDetails suggestedDetails = suggestionChoices.getBestCall();
            String suggestedLogId = (suggestedDetails != null) ? suggestedDetails.getMatchedLogID(debugState.getNextToAct()) : "NONE";

            String msg = "FAILURE: " + test.getName() + "\n" +
                    "  Auction:      " + test.getAuction() + "\n" +
                    "  Deal:         " + test.getDeal() + "\n" +
                    "  Vulnerable:   " + test.getVulnerable() + "\n" +
                    "  Expected:     " + test.getExpectedCall() + "\n" +
                    "  Actual:       " + suggestion + " [LogID: " + suggestedLogId + "]\n";
            System.err.println(msg);
            assertEquals(msg, test.getExpectedCall(), suggestion);
        }
    }

    private static String loadPbnFileStatic(String filename) {
        String[] possiblePaths = {
                "app/src/test/java/com/example/bridge/",
                "src/test/java/com/example/bridge/"
        };

        for (String path : possiblePaths) {
            File file = new File(path + filename);
            if (file.exists()) {
                try {
                    return new String(Files.readAllBytes(file.toPath()));
                } catch (IOException e) {
                    // continue searching
                }
            }
        }
        throw new RuntimeException("Could not find " + filename + ". Working directory: " + new File(".").getAbsolutePath());
    }
}
