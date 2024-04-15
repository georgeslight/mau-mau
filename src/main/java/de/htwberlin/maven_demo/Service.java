package de.htwberlin.maven_demo;

import javax.smartcardio.Card;
import java.awt.*;

public interface Service {
        void initializeGame(int numberOfPlayers);
        void startTurn(int player);
        void playCard(Card card);
        void drawCard(int player);
        void nextPlayer();
        boolean checkWinner(int player);
        void shuffleDiscardPileIntoDrawPile();
        void applySpecialCardEffects(Card card);
        boolean checkValidCard(Card card, Card topCard);
        void updateGameState();
        void endGame();
        boolean checkCardsValidation();
        String mau();
    }

