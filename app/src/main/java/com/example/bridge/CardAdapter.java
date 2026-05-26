package com.example.bridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.model.Card;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Suit;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    public interface OnCardClickListener {
        void onCardClick(Card card, int position);
    }

    private final List<Card> cards;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnCardClickListener listener;

    public CardAdapter(List<Card> cards) {
        this.cards = cards;
    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);
        if (card == null) {
            holder.itemView.setVisibility(View.GONE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.bind(card, position == selectedPosition);
            
            holder.itemView.setOnClickListener(v -> {
                int currentPos = holder.getAdapterPosition();
                if (currentPos == RecyclerView.NO_POSITION) return;

                int previousSelected = selectedPosition;
                selectedPosition = currentPos;
                
                if (previousSelected != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelected);
                }
                notifyItemChanged(selectedPosition);

                if (listener != null) {
                    listener.onCardClick(card, currentPos);
                }
            });
        }
    }

    public void clearSelection() {
        int prev = selectedPosition;
        selectedPosition = RecyclerView.NO_POSITION;
        if (prev != RecyclerView.NO_POSITION) {
            notifyItemChanged(prev);
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank;
        ImageView ivSuitSmall, ivSuitLarge;
        MaterialCardView cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            ivSuitSmall = itemView.findViewById(R.id.iv_suit_small);
            ivSuitLarge = itemView.findViewById(R.id.iv_suit_large);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(Card card, boolean isSelected) {
            String rankStr = getRankString(card.getRank());
            tvRank.setText(rankStr);

            int suitResId = getSuitDrawable(card.getSuit());
            ivSuitSmall.setImageResource(suitResId);
            ivSuitLarge.setImageResource(suitResId);

            int color = (card.getSuit() == Suit.HEARTS || card.getSuit() == Suit.DIAMONDS) 
                ? 0xFFFF0000 : 0xFF000000;
            tvRank.setTextColor(color);

            if (isSelected) {
                cardView.setCardBackgroundColor(android.graphics.Color.YELLOW);
            } else {
                cardView.setCardBackgroundColor(android.graphics.Color.WHITE);
            }
        }

        private String getRankString(Rank rank) {
            switch (rank) {
                case ACE: return "A";
                case KING: return "K";
                case QUEEN: return "Q";
                case JACK: return "J";
                case TEN: return "10";
                case NINE: return "9";
                case EIGHT: return "8";
                case SEVEN: return "7";
                case SIX: return "6";
                case FIVE: return "5";
                case FOUR: return "4";
                case THREE: return "3";
                case TWO: return "2";
                default: return "?";
            }
        }

        private int getSuitDrawable(Suit suit) {
            switch (suit) {
                case HEARTS: return R.drawable.suit_hearts;
                case DIAMONDS: return R.drawable.suit_diamonds;
                case SPADES: return R.drawable.suit_spades;
                case CLUBS: return R.drawable.suit_clubs;
                default: return 0;
            }
        }
    }



}