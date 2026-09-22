package com.example.bridge.runTests;

import static org.junit.Assert.assertEquals;

import com.example.bridge.bidding.Tools.BridgeBidder;

import org.junit.Test;

public class SAYCTest {

    @Test
    public void testSAYCOpening1NT() {
        // 15-17 HCP balanced -> should open 1NT in SAYC
        // N: KQ93.KQ8.Q42.KT7 (16 HCP, 4-3-3-3 balanced)
        String deal = "N:KQ93.KQ8.Q42.KT7 842.A6542.T9.J32 65.T973.A85.9854 AJT7.J.KJ763.A63";
        String suggestion = BridgeBidder.suggestBid(deal, "None", "", "SAYC", "PassOnly");
        assertEquals("1NT", suggestion);
    }

    @Test
    public void testSAYCOpening1H() {
        // 13+ HCP, 5+ Hearts -> 1H
        // N: 84.AK985.Q42.KT7 (14 HCP, 5 Hearts)
        String deal = "N:842.AK985.Q42.KT 84.642.T93.J322 65.T973.A85.9854 AJT7.J.KJ763.A63";
        String suggestion = BridgeBidder.suggestBid(deal, "None", "", "SAYC", "PassOnly");
        assertEquals("1H", suggestion);
    }

    @Test
    public void testSAYCOpening1S() {
        // 13+ HCP, 5+ Spades -> 1S
        // N: AK985.84.Q42.KT7 (14 HCP, 5 Spades)
        String deal = "N:AK985.842.Q42.KT 84.642.T93.J322 65.T973.A85.9854 AJT7.J.KJ763.A63";
        String suggestion = BridgeBidder.suggestBid(deal, "None", "", "SAYC", "PassOnly");
        assertEquals("1S", suggestion);
    }

    @Test
    public void testSAYCOpeningStrong2C() {
        // 22+ HCP -> 2C
        // N: AKQJ.AKQ.AKQ.T98 (25 HCP)
        String deal = "N:AKQJ.AKQ.AKQ.T98 842.6542.T92.J32 653.T973.854.7654 7.J8.J763.A632";
        String suggestion = BridgeBidder.suggestBid(deal, "None", "", "SAYC", "PassOnly");
        assertEquals("2C", suggestion);
    }

    @Test
    public void testSAYCStayman() {
        // Auction: 1NT - Pass - 2C (Stayman)
        // N opens 1NT (15-17 BAL), S bids Stayman 2C with 8+ HCP and 4-card major
        String deal = "N:KQ93.KQ8.Q42.KT7 842.6542.T92.J32 AJ72.T973.A85.98 65.J.KJ763.A6543";
        String suggestion = BridgeBidder.suggestBid(deal, "None", "1NT Pass", "SAYC", "PassOnly");
        assertEquals("2C", suggestion);
    }

    @Test
    public void testSAYCJacobyTransferToHearts() {
        // Auction: 1NT - Pass - 2D (Transfer to Hearts)
        // N opens 1NT, S has 5+ Hearts -> 2D
        String deal = "N:KQ93.KQ8.Q42.KT7 842.6542.T92.J32 72.AQT973.854.98 65.J.KJ763.A6543";
        String suggestion = BridgeBidder.suggestBid(deal, "None", "1NT Pass", "SAYC", "PassOnly");
        assertEquals("2D", suggestion);
    }
}
