package com.example.bridge.bidding.TestBridgeBidder.runTests;

import static org.junit.Assert.assertEquals;

import com.example.bridge.bidding.Tools.BridgeBidder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class NatCTest {

    private final PBNTest test;

    public NatCTest(PBNTest test) {
        this.test = test;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return loadPBNTests("NatC");
    }

    private static Collection<Object[]> loadPBNTests(String subdirectory) {
        List<Object[]> result = new ArrayList<>();
        // Try various relative paths to find the test data directory
        String[] possiblePaths = {
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
            System.err.println("Could not find directory for subdirectory: " + subdirectory);
            System.err.println("Current working directory: " + new File(".").getAbsolutePath());
        }
        
        if (result.isEmpty()) {
            System.err.println("No tests found in subdirectory: " + subdirectory);
        }
        return result;
    }

    @Test
    public void testNatC() {
        String suggestion = BridgeBidder.suggestBid(test.getDeal(), test.getVulnerable(), test.getAuction(), "NatC","NatC");
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




























































































































































































































































































































































































































































































