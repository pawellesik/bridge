package com.example.bridge;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.model.Card;
import com.example.bridge.model.Player;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    private final List<Card> cards;
    private int selectedPos = RecyclerView.NO_POSITION;
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
        if (card == null || card == GameActivity.GHOST_CARD) {
            holder.itemView.setVisibility(View.INVISIBLE);
            holder.itemView.setOnTouchListener(null);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.bind(card);

            holder.itemView.setOnClickListener(v -> {
                int prev = selectedPos;
                selectedPos = holder.getAdapterPosition();
                if (prev != RecyclerView.NO_POSITION) notifyItemChanged(prev);
                notifyItemChanged(selectedPos);
                if (listener != null) listener.onCardClick(card);
            });

            holder.itemView.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.cardView.setCardBackgroundColor(Color.YELLOW);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (isInside(v, event)) {
                            holder.cardView.setCardBackgroundColor(Color.YELLOW);
                        } else {
                            holder.cardView.setCardBackgroundColor(Color.WHITE);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        holder.cardView.setCardBackgroundColor(Color.WHITE);
                        if (isInside(v, event)) {
                            v.performClick();
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        holder.cardView.setCardBackgroundColor(Color.WHITE);
                        break;
                }
                return false;
            });
        }
    }

    private boolean isInside(View v, MotionEvent e) {
        return e.getX() >= 0 && e.getX() <= v.getWidth() &&
               e.getY() >= 0 && e.getY() <= v.getHeight();
    }

    public void clearSelection() {
        int prev = selectedPos;
        selectedPos = RecyclerView.NO_POSITION;
        notifyItemChanged(prev);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank;
        ImageView ivSmall, ivLarge;
        MaterialCardView cardView;

        CardViewHolder(View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            ivSmall = itemView.findViewById(R.id.iv_suit_small);
            ivLarge = itemView.findViewById(R.id.iv_suit_large);
            cardView = itemView.findViewById(R.id.card_view);
        }

        void bind(Card card) {
            tvRank.setText(card.getRank().display);
            ivSmall.setImageResource(card.getSuit().resId);
            ivLarge.setImageResource(card.getSuit().resId);
            
            int suitColor = card.getSuit().getColor(tvRank.getContext());
            tvRank.setTextColor(suitColor);
            ivSmall.setColorFilter(suitColor);
            ivLarge.setColorFilter(suitColor);

            cardView.setCardBackgroundColor(Color.WHITE);
        }
    }
}
