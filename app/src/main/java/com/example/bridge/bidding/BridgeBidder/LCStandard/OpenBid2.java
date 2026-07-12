package com.example.bridge.bidding.BridgeBidder.LCStandard;

import com.example.bridge.bidding.BridgeBidder.*;
import com.example.bridge.bidding.BridgeBidder.Conventions.Blackwood;
import java.util.ArrayList;
import java.util.List;

public class OpenBid2 extends Open {

    public static PositionCalls responderChangedSuits(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            partnerBids(RespondBid2::secondBid),

            // Responder bid a major suits and we have a fit.  Support at appropriate level.
            // RaisePartner() requires a known 8+ card fit.  If the selected, the rule shows trump
            shows(Bid._2H, raisePartner(), DummyMinimum),
            shows(Bid._2S, raisePartner(), DummyMinimum),
            shows(Bid._3H, raisePartner(null, 1, 8), DummyMedium),
            shows(Bid._3S, raisePartner(null, 1, 8), DummyMedium),
            shows(Bid._4H, raisePartner(null, 2, 8), DummyMaximum),
            shows(Bid._4S, raisePartner(null, 2, 8), DummyMaximum),

            // We can't raise partner's suit.  
            // TODO: Here is where welsh bidding would happen...  1NT or 2NT
            shows(Bid._1H, shape(4, 6)),
            shows(Bid._1S, shape(4, 6)),

            // TODO: These need to be lower priority...
            shows(Bid._2D, raisePartner(), Minimum),
            shows(Bid._3D, raisePartner(null, 1, 8), Medium),

            // If we have a 19 point balanced hand then better to show this with a rebid of 2NT
            // than a forcing jump shift or reverse.
            shows(Bid._2NT, BALANCED, points(Rebid2NT)),

            // With a big hand we need to make a forcing bid.  Reverse if possible.
            properties(new Bid[] { Bid._2D, Bid._2H, Bid._2S }, true, IS_REVERSE_BID),
            shows(Bid._2D, IS_REVERSE_BID, REVERSE_SHAPE, MediumOrBetter),
            shows(Bid._2H, IS_REVERSE_BID, REVERSE_SHAPE, MediumOrBetter),
            shows(Bid._2S, IS_REVERSE_BID, REVERSE_SHAPE, MediumOrBetter),


            // TODO: What about minors.  This is bad. Think we want to fall through to 3NT...
            //Shows(4, Strain.Clubs, DefaultPriority + 10, Fit8Plus, ShowsTrump, Points(MediumOpener)),
            //Shows(4, Strain.Diamonds, DefaultPriority + 10, Fit8Plus, ShowsTrump, Points(MediumOpener)),

            // Show a new suit at an appropriate level...
            //			Shows(Bid._2C, Balanced(false), Points(MinimumOpener), LongestUnbidSuit()),
            //            Shows(Bid._2C, Balanced(false), Points(MinimumOpener), LongestUnbidSuit()),
            shows(Bid._2H, IS_NEW_SUIT, IS_NOT_REVERSE, NOT_BALANCED, Minimum, shape(4, 6)),
            shows(Bid._2C, IS_NEW_SUIT, NOT_BALANCED, CantJumpShift, shape(4, 6)),
            shows(Bid._2D, IS_NEW_SUIT, IS_NOT_REVERSE, NOT_BALANCED, CantJumpShift, shape(4, 6)),
    
            // Rebid a 6 card suit
            shows(Bid._2C, IS_REBID, shape(6, 11), Minimum),
            shows(Bid._2D, IS_REBID, shape(6, 11), Minimum),
            shows(Bid._2H, IS_REBID, shape(6, 11), Minimum),
            shows(Bid._2S, IS_REBID, shape(6, 11), Minimum),

            shows(Bid._3C, IS_REBID, shape(6, 11), Medium),
            shows(Bid._3D, IS_REBID, shape(6, 11), Medium),
            shows(Bid._3H, IS_REBID, shape(6, 11), Medium),
            shows(Bid._3S, IS_REBID, shape(6, 11), Medium),

            shows(Bid._2H, isLastBid(Bid._1S), shape(4, 6), points(LessThanJumpShift)),
            shows(Bid._3H, isLastBid(Bid._1S), shape(4, 5), points(JumpShift)),

            propertiesForcingToGame(new Call[] { Bid._2H, Bid._2S, Bid._3C, Bid._3D, Bid._3H, Bid._3S }, true, IS_JUMP_SHIFT),
            // TODO: Need to jump-shift only if this is the 2nd longest suit.  Perhaps this is good enough.  
            shows(Bid._2H, IS_JUMP_SHIFT, shape(4, 6), points(JumpShift)),
            shows(Bid._2S, IS_JUMP_SHIFT, shape(4, 6), points(JumpShift)),
            shows(Bid._3C, IS_JUMP_SHIFT, shape(4, 6), points(JumpShift)),
            shows(Bid._3D, IS_JUMP_SHIFT, shape(4, 6), points(JumpShift)),
            shows(Bid._3H, IS_JUMP_SHIFT, shape(4, 6), points(JumpShift)),
            shows(Bid._3S, IS_JUMP_SHIFT, shape(4, 6), points(JumpShift)),

            // We have tried every possible way to show a strong hand by reversing or jump shifting.  If we get here
            // and have not found a bid but we are very strong then we just need to bid 3 or 4 of our suit.
            shows(Bid._4H, IS_REBID, EXCELLENT_PLUS_SUIT, shape(7, 11), points(20, 21)),
            shows(Bid._3H, IS_REBID, shape(6, 11), points(17, 19)),
            shows(Bid._4S, IS_REBID, EXCELLENT_PLUS_SUIT, shape(7, 11), points(20, 21)),
            shows(Bid._3S, IS_REBID, shape(6, 11), points(17, 19)),
            // TODO: Need to implement minors here too.  Long, strong minors need a backup if no reverse available.
            // 

            // TODO: Need to implement 3NT bid if long running minor.  Suits stopped????

            // Lowest priority if nothing else fits is bid NT
            shows(Bid._1NT, BALANCED, highCardPoints(12, 14), points(Rebid1NT))
        );
        return choices;
    }

    public static PositionCalls twoOverOne(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit partnerSuit = ((Bid)ps.getPartner().getLastCall()).getSuit();
        choices.addRules(
            partnerBids(RespondBid2::secondBid2Over1),

            // These show a trump suit.  Responder must play in this.
            // TODO: Should the idea of a "forced" trump suit be remembered in the bidding system?
            // This would allow competative bids to use this information to place the contract...
            // TODO: What is the length requirements and quality requirements for these bids?
            properties(new Bid[]{Bid._3H, Bid._3S}, null, false, false, true, null, null, null, null, IS_REBID),
            shows(Bid._3H, IS_REBID, shape(7), EXCELLENT_PLUS_SUIT),
            shows(Bid._3H, IS_REBID, shape(8), GOOD_PLUS_SUIT),
            shows(Bid._3H, IS_REBID, shape(9, 11)),
            shows(Bid._3S, IS_REBID, shape(7), EXCELLENT_PLUS_SUIT),
            shows(Bid._3S, IS_REBID, shape(8), GOOD_PLUS_SUIT),
            shows(Bid._3S, IS_REBID, shape(9, 11)),

            // If partner has shown hearts after we bid spades then agree on that suit.
            properties(new Call[] {Bid._3H, Bid._4H }, null, false, false, true, null, null, null, null, isPartnersSuit()),
            shows(Bid._3H, isPartnersSuit(), shape(3, 7), points(14, 40)),
            shows(Bid._4H, isPartnersSuit(), shape(3, 7), points(12, 13)),

            // Need to show any 4+ card major that is a new suit.  Reverses don't exist
            shows(Bid._2H, IS_NEW_SUIT, shape(4, 6)),
            shows(Bid._2S, IS_NEW_SUIT, shape(4, 6)),

            // TODO: If partner shows a minor then do we want to raise?  Or bid NT?  Or bid a new major if
            // we have one?  One D/2C/ Now shouldn't we show majors?  Or bid NT?
            // TODO:  If it was 1S/2H and we have 3+H then raise to 3 or 4 hearts...  Only special case.
            shows(new Bid(3, partnerSuit), FIT_8_PLUS),

            shows(Bid._2NT, BALANCED),

            shows(Bid._2D, IS_REBID, shape(6, 11), LONGEST_SUIT),
            shows(Bid._2H, IS_REBID, shape(6, 8), LONGEST_SUIT),
            shows(Bid._2S, IS_REBID, shape(6, 8), LONGEST_SUIT),
            shows(Bid._3C, IS_REBID, shape(6, 11), LONGEST_SUIT),

            shows(Bid._2D, IS_NEW_SUIT, shape(4, 6)),
            shows(Bid._3C, IS_NEW_SUIT, shape(4, 6)),
            shows(Bid._3D, IS_NEW_SUIT, IS_NON_JUMP, shape(4, 6))
        );
        return choices;
    }

    public static PositionCalls responderPassedInCompetition(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Bid._2C, IS_REBID, shape(6, 11), Minimum),
            shows(Bid._2D, IS_REBID, shape(6, 11), Minimum),
            shows(Bid._2H, IS_REBID, shape(6, 11), Minimum),
            shows(Bid._2S, IS_REBID, shape(6, 11), Minimum),

            shows(Bid._3C, IS_REBID, shape(6, 11), MediumOrBetter),
            shows(Bid._3D, IS_REBID, shape(6, 11), MediumOrBetter),
            shows(Bid._3H, IS_REBID, shape(6, 11), MediumOrBetter),
            shows(Bid._3S, IS_REBID, shape(6, 11), MediumOrBetter),

            shows(Call.PASS)
        );
        return choices;
    }

    public static PositionCalls semiForcingNT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Call.PASS, BALANCED, points(12, 13)),

            shows(Bid._2NT, BALANCED, highCardPoints(18, 19), points(19, 20)),

            shows(Bid._2C, IS_NEW_SUIT, shape(4, 6), points(12, 16)),
            shows(Bid._2D, IS_NEW_SUIT, shape(4, 6), points(12, 16)),
            shows(Bid._2H, IS_NEW_SUIT, shape(4, 6), points(12, 16)),

            shows(Bid._2C, IS_REBID, shape(6, 11), points(12, 16)),
            shows(Bid._2D, IS_REBID, shape(6, 11), points(12, 16)),
            shows(Bid._2H, IS_REBID, shape(6, 11), points(12, 16)),
            shows(Bid._2S, IS_REBID, shape(6, 11), points(12, 16))
        );
        return choices;
    }

    public static PositionCalls oneNTOverMajorOpen(PositionState ps) {
        return responderChangedSuits(ps);
    }

    public static PositionCalls oneNTOverMinorOpen(PositionState ps) {
        return responderChangedSuits(ps);
    }

    public static PositionCalls twoNTOverMinorOpen(PositionState ps) {
        return responderChangedSuits(ps);
    }

    public static PositionCalls threeNTOverClubOpen(PositionState ps) {
        return responderChangedSuits(ps);
    }

    public static PositionCalls responderBidNT(PositionState ps) {
        return ps.getPairState().getBiddingSystem().getPositionCalls(ps);
    }

    public static PositionCalls responderRaisedMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Compete::compBids);
        return choices;
    }

    public static PositionCalls responderRaisedMajor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            Blackwood.initiateConvention(ps),
            partnerBids(Bid._3H, RespondBid2::openerInvitedGame),
            partnerBids(Bid._3S, RespondBid2::openerInvitedGame),

            shows(Bid._3H, FIT_8_PLUS, pairPoints(PAIR_GAME_INVITE)),
            shows(Bid._3S, FIT_8_PLUS, pairPoints(PAIR_GAME_INVITE)),

            shows(Bid._4H, FIT_8_PLUS, pairPoints(PAIR_GAME)),
            shows(Bid._4S, FIT_8_PLUS, pairPoints(PAIR_GAME))
        );
        return choices;
    }
}




























































































































































































































































































































































































































































































