package com.example.bridge.bidding.TestBridgeBidder.runTests;

import static org.junit.Assert.assertEquals;

import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.BridgeBidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.Game;
import com.example.bridge.bidding.Tools.PositionCalls;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class Test {

    private final PBNTest test;

    public Test(PBNTest test) {
        this.test = test;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return loadPBNTests();
    }

    private static Collection<Object[]> loadPBNTests() {
        List<Object[]> result = new ArrayList<>();
        // Try various relative paths to find the test data directory
        String[] possiblePaths = {
           "src/test/java/com/example/bridge/bidding/TestBridgeBidder/",
        };
        
        File dir = null;
        for (String path : possiblePaths) {
            File candidate = new File(path);
            if (candidate.exists() && candidate.isDirectory()) {
                dir = candidate;
                break;
            }
        }

        if (dir != null) {
            File[] files = dir.listFiles((d, name) -> name.endsWith("test.pbn"));
            if (files != null) {
                for (File file : files) {
                    try {
                        System.out.println("Loading: " + file.getAbsolutePath());
                        String content = new String(Files.readAllBytes(file.toPath()));
                        List<PBNTest> pbnTests = PBNUtils.importTests(content);
                        for (PBNTest t : pbnTests) {
                            t.setName(file.getName() + ": " + t.getName());
                            result.add(new Object[]{t});
                        }
                    } catch (Exception e) {
                        System.err.println("Error loading " + file.getName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.err.println("Could not find directory for subdirectory: " );
            System.err.println("Current working directory: " + new File(".").getAbsolutePath());
        }
        
        if (result.isEmpty()) {
            System.err.println("No tests found in subdirectory: " );
        }
        return result;
    }

    @org.junit.Test
    public void testNatC() {
        String suggestion = BridgeBidder.suggestBid(test.getDeal(), test.getVulnerable(), test.getAuction(), "NatC","PassOnly");

        if (!test.getExpectedCall().equals(suggestion)) {
            // Detailed breakdown on failure
            System.err.println("\n--- DETAILED LICYTACJA BREAKDOWN (NatC) ---");
            Game game = Game.parse(test.getDeal(), test.getVulnerable());
            game.bidSystemNS = "NatC";
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
}




























































































































































































































































































































































































































































































