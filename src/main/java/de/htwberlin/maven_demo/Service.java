package de.htwberlin.maven_demo;


import de.htwberlin.maven_demo.model.Card;

public interface Service {

        void initializeGame(int numberOfPlayers);
        void startTurn(int player);
        void playCard(Card card);
        void drawCard(int player);
        void nextPlayer();
        void previousPlayer();
        boolean checkWinner(int player);
        void shuffleDiscardPileIntoDrawPile();
        void applySpecialCardEffects(Card card);
        /*sub methode for playCard*/
        boolean checkValidCard(Card card, Card topCard);
        void updateGameState();
        void endGame();
        String mau();
        void discardCard(Card card);
        void surrender();
        void playJack();
        void play7();
        void play8();
        void playAce();

    }