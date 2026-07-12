package com.example.bridge.bidding.TestBridgeBidder;

import com.example.bridge.bidding.Tools.BridgeBidder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BiddingTests {

    private final PBNTest test;

    public BiddingTests(PBNTest test) {
        this.test = test;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return loadPBNTests("TwoOverOneGameForce");
    }

    private static Collection<Object[]> loadPBNTests(String subdirectory) {
        List<Object[]> result = new ArrayList<>();
        // Try various relative paths to find the test data directory
        String[] possiblePaths = {
            "app/src/test/java/com/example/bridge/bidding/TestBridgeBidder/",
            "src/test/java/com/example/bridge/bidding/TestBridgeBidder/",
        };

        File dir = null;
        for (String path : possiblePaths) {
            File candidate = new File(path + subdirectory);
            if (candidate.exists() && candidate.isDirectory()) {
                dir = candidate;
                break;
            }
        }
        
        if (dir != null) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".pbn"));
            if (files != null) {
                for (File file : files) {
                    try {
                        String content = new String(Files.readAllBytes(file.toPath()));
                        List<PBNTest> pbnTests = PBNUtils.importTests(content);
                        for (PBNTest t : pbnTests) {
                            t.setName(file.getName() + ": " + t.getName());
                            result.add(new Object[]{t});
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.err.println("Could not find directory for subdirectory: " + subdirectory);
            System.err.println("Current working directory: " + new File(".").getAbsolutePath());
        }
        return result;
    }

    @Test
    public void testTwoOverOne() {
        String suggestion = BridgeBidder.suggestBid(test.getDeal(), test.getVulnerable(), test.getAuction());
        if (!test.getExpectedCall().equals(suggestion)) {
            String msg = "FAILURE: " + test.getName() + "\n" +
                         "  Auction:  " + test.getAuction() + "\n" +
                         "  Deal:     " + test.getDeal() + "\n" +
                         "  Vulnerable: " + test.getVulnerable() + "\n" +
                         "  Expected: " + test.getExpectedCall() + "\n" +
                         "  Actual:   " + suggestion + "\n";
            System.err.println(msg);
            assertEquals(msg, test.getExpectedCall(), suggestion);
        }
    }
}




























































































































































































































































































































































































































































































