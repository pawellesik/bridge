package com.example.bridge.ui.biddings;

import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.TextView;

import com.example.bridge.R;
import com.example.bridge.bidding.Tools.BiddingState;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallDetails;
import com.example.bridge.bidding.Tools.ContractState;
import com.example.bridge.bidding.Tools.Direction;
import com.example.bridge.bidding.Tools.Game;
import com.example.bridge.bidding.Tools.Hand;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;
import com.example.bridge.ui.game.GameActivity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SingleGameBidding {
    private final GameActivity activity;
    private BiddingState liveBiddingState;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public SingleGameBidding(GameActivity activity) {
        this.activity = activity;
        setupSystemSelectionListeners();
    }

    private void setupSystemSelectionListeners() {
        View cardWj = activity.findViewById(R.id.system_wj);
        View cardSayc = activity.findViewById(R.id.system_sayc);
        View cardNatc = activity.findViewById(R.id.system_natc);

        if (cardWj == null) return;

        View.OnClickListener listener = v -> {
            resetSystemSelection();
            v.setBackgroundResource(R.drawable.bg_system_card_selected);
            int checkId = -1;
            int vid = v.getId();
            if (vid == R.id.system_wj) checkId = R.id.iv_wj_check;
            else if (vid == R.id.system_sayc) checkId = R.id.iv_sayc_check;
            else if (vid == R.id.system_natc) checkId = R.id.iv_natc_check;

            View check = activity.findViewById(checkId);
            if (check != null) check.setVisibility(View.VISIBLE);
        };

        cardWj.setOnClickListener(listener);
        cardSayc.setOnClickListener(listener);
        cardNatc.setOnClickListener(listener);
    }

    private void resetSystemSelection() {
        int[] cardIds = {R.id.system_wj, R.id.system_sayc, R.id.system_natc};
        int[] checkIds = {R.id.iv_wj_check, R.id.iv_sayc_check, R.id.iv_natc_check};
        for (int i = 0; i < cardIds.length; i++) {
            View card = activity.findViewById(cardIds[i]);
            if (card != null) card.setBackgroundResource(R.drawable.bg_system_card_unselected);
            View check = activity.findViewById(checkIds[i]);
            if (check != null) check.setVisibility(View.GONE);
        }
    }

    public void start() {
        if (activity.getBiddingControlsOverlay() != null) {
            activity.getBiddingControlsOverlay().setVisibility(View.GONE);
        }

        View btnClose = activity.findViewById(R.id.btn_close_bidding_overlay);
        if (btnClose != null) btnClose.setVisibility(View.GONE);

        View selectionContainer = activity.findViewById(R.id.system_selection_container);
        if (selectionContainer != null) {
            selectionContainer.setVisibility(View.GONE);
        }

        Game game = new Game();
        Direction[] dirs = Direction.values();
        Direction dealerDir = dirs[(int) (Math.random() * 4)];
        game.dealer = dealerDir;
        
        activity.getPbnCollection().getPbn().setDealer(dealerDir.toString());
        game.bidSystemNS = "NatC";
        game.bidSystemEW = "PassOnly";

        Map<String, List<Card>> hands = activity.getGameController().getHandsMap();
        activity.getPbnCollection().getPbn().initNewGame(hands,  activity.getGameMode());
        activity.getPbnCollection().getPbn().setPlayerNames("West", "North", "East", "South");

        game.getDeal().put(Direction.N, Hand.parse(activity.getPbnCollection().getPbn().formatHand(hands.get("North"))));
        game.getDeal().put(Direction.E, Hand.parse(activity.getPbnCollection().getPbn().formatHand(hands.get("East"))));
        game.getDeal().put(Direction.S, Hand.parse(activity.getPbnCollection().getPbn().formatHand(hands.get("South"))));
        game.getDeal().put(Direction.W, Hand.parse(activity.getPbnCollection().getPbn().formatHand(hands.get("West"))));

        liveBiddingState = new BiddingState(game);

        String firstPlayerName = "West";
        if (dealerDir == Direction.N) firstPlayerName = "North";
        else if (dealerDir == Direction.E) firstPlayerName = "East";
        else if (dealerDir == Direction.S) firstPlayerName = "South";

        activity.getGameBiddingHistory().setFirstPlayer(activity.getGameController().getPlayers().get(firstPlayerName));
        activity.getGameBiddingHistory().getAuction().clear();
        
        activity.getGameBiddingHistoryAdapter().setShowPreviewTile(true);
        activity.getGameBiddingHistoryAdapter().setHighlightLast(false);
        activity.getGameBiddingHistory().updateBiddingHistory();

        handleNextTurn();
    }

    public void handleNextTurn() {
        if (liveBiddingState == null || liveBiddingState.getContract().isAuctionComplete()) return;

        Direction nextToAct = liveBiddingState.getNextToAct().getDirection();

        if (nextToAct == Direction.S) {
            activity.runOnUiThread(() -> {
                activity.getGameBiddingHistoryAdapter().setHighlightLast(true);
                updatePublicKnowledgeView();
                activity.getGameBiddingHistory().updateBiddingHistory();
                
                activity.getGameBidding().applyAuctionRules(activity.getGameBiddingHistory());
                if (activity.getBiddingControlsOverlay() != null) {
                    activity.getBiddingControlsOverlay().setVisibility(View.VISIBLE);
                }
            });
            return;
        }

        activity.runOnUiThread(() -> {
            activity.getGameBiddingHistoryAdapter().setHighlightLast(false);
            activity.getGameBiddingHistory().updateBiddingHistory();
            
            View infoLayout = activity.findViewById(R.id.public_knowledge_container_layout);
            if (infoLayout != null) infoLayout.setVisibility(View.GONE);

            if (activity.getBiddingControlsOverlay() != null) {
                activity.getBiddingControlsOverlay().setVisibility(View.GONE);
            }
        });

        handler.postDelayed(() -> {
            PositionCalls choices = liveBiddingState.getCallChoices();
            CallDetails bestCall = choices.getBestCall();
            Call call = (bestCall != null) ? bestCall.getCall() : Call.PASS;

            makeRobotBid(call);
        }, 800);
    }

    private void makeRobotBid(Call call) {
        String bidStr = call.toString();
        liveBiddingState.makeCall(call);
        activity.getGameBiddingHistory().getAuction().add(bidStr);
        activity.getPbnCollection().getPbn().addBid(bidStr);
        activity.getGameBiddingHistory().updateBiddingHistory(null, true);

        if (liveBiddingState.getContract().isAuctionComplete()) {
            onAuctionFinished();
        } else {
            handleNextTurn();
        }
    }

    public void syncManualBid(Call call) {
        if (liveBiddingState != null) {
            liveBiddingState.makeCall(call);
            if (liveBiddingState.getContract().isAuctionComplete()) {
                onAuctionFinished();
            } else {
                handleNextTurn();
            }
        }
    }

    private void onAuctionFinished() {
        ContractState contractState = liveBiddingState.getContract();
        if (contractState.isPassedOut()) {
            activity.getGameController().onBiddingFinished(new Contract(true), null);
        } else {
            Direction declarerDir = contractState.getDeclarer();
            String declarerName = "West";
            if (declarerDir == Direction.N) declarerName = "North";
            else if (declarerDir == Direction.E) declarerName = "East";
            else if (declarerDir == Direction.S) declarerName = "South";

            Player declarer = activity.getGameController().getPlayers().get(declarerName);

            com.example.bridge.bidding.Tools.Bid finalBid = contractState.getBid();
            Suit suit = null;
            if (finalBid.getStrain() != com.example.bridge.bidding.Tools.Strain.NoTrump) {
                suit = Suit.valueOf(finalBid.getStrain().name().toUpperCase());
            }
            Contract contract = new Contract(finalBid.getLevel(), suit);

            activity.getGameController().onBiddingFinished(contract, declarer);
        }
    }

    public void updatePublicKnowledgeView() {
        if (liveBiddingState == null) return;

        View container = activity.findViewById(R.id.public_knowledge_container_layout);
        if (container == null) return;

        container.setVisibility(View.VISIBLE);

        TextView tvNorth = container.findViewById(R.id.tv_pk_north);
        TextView tvSouth = container.findViewById(R.id.tv_pk_south);
        TextView tvTrump = container.findViewById(R.id.tv_pk_trump);

        updatePlayerKnowledge(tvNorth, Direction.N);
        updatePlayerKnowledge(tvSouth, Direction.S);

        PositionState northPos = liveBiddingState.getPositions().get(Direction.N);
        com.example.bridge.bidding.Tools.Suit nsTrump = (northPos != null) ? northPos.getPairState().getTrumpSuit() : null;
        if (nsTrump != null) {
            tvTrump.setVisibility(View.VISIBLE);
            SpannableStringBuilder ssb = new SpannableStringBuilder("UZGODNIONY ATUT NS: ");
            appendSuitSymbol(ssb, nsTrump, "");
            tvTrump.setText(ssb);
        } else {
            tvTrump.setVisibility(View.GONE);
        }
    }

    private void updatePlayerKnowledge(TextView textView, Direction d) {
        PositionState pos = liveBiddingState.getPositions().get(d);
        if (pos == null) {
            textView.setVisibility(View.GONE);
            return;
        }
        HandSummary summary = pos.getPublicHandSummary();
        if (summary == null) {
            textView.setVisibility(View.GONE);
            return;
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(d.name()).append(": ");

        Range p = summary.getHighCardPoints();
        if (p != null)
            ssb.append("HCP: ").append(String.valueOf(p.getMin())).append("-").append(String.valueOf(p.getMax())).append(" ");

        Set<Integer> aces = summary.getCountAces();
        if (aces != null && !aces.isEmpty()) {
            ssb.append("Asy: ").append(aces.toString()).append(" ");
        }
        Set<Integer> kings = summary.getCountKings();
        if (kings != null && !kings.isEmpty()) {
            ssb.append("Krole: ").append(kings.toString()).append(" ");
        }

        for (com.example.bridge.bidding.Tools.Suit s : com.example.bridge.bidding.Tools.Suit.values()) {
            HandSummary.SuitSummary suitSum = summary.getSuits().get(s);
            if (suitSum != null) {
                Range shape = suitSum.getShape();
                if (shape != null && shape.getMin() > 0) {
                    appendSuitSymbol(ssb, s, ":" + shape.getMin() + "+ ");
                }
            }
        }

        if (ssb.length() > 3) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(ssb);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void appendSuitSymbol(SpannableStringBuilder ssb, com.example.bridge.bidding.Tools.Suit s, String suffix) {
        com.example.bridge.model.Suit modelSuit;
        switch (s) {
            case Clubs: modelSuit = com.example.bridge.model.Suit.CLUBS; break;
            case Diamonds: modelSuit = com.example.bridge.model.Suit.DIAMONDS; break;
            case Hearts: modelSuit = com.example.bridge.model.Suit.HEARTS; break;
            case Spades: modelSuit = com.example.bridge.model.Suit.SPADES; break;
            default: ssb.append(s.toSymbol()).append(suffix); return;
        }

        // Pobieramy ikonę (tę samą co w historii)
        Drawable drawable = ContextCompat.getDrawable(activity, modelSuit.resId);
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable).mutate();
            // Pobieramy kolor (z uwzględnieniem 4-kolorowej talii)
            int color = modelSuit.getColor(activity);
            DrawableCompat.setTint(drawable, color);
            
            // Ustawiamy rozmiar ikony na zbliżony do rozmiaru tekstu (14dp)
            int size = (int) (14 * activity.getResources().getDisplayMetrics().density);
            drawable.setBounds(0, 0, size, size);
            
            ssb.append(" "); // Miejsce na ikonę
            ssb.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), ssb.length() - 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(modelSuit.symbol);
        }
        
        ssb.append(suffix);
    }
}
