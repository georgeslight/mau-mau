package de.htwberlin.maven_demo;


import de.htwberlin.maven_demo.model.Card;
import de.htwberlin.maven_demo.model.Deck;

 interface Service {

        /**
         * Initializes the game with the specified number of players.
         * Sets up the deck, shuffles it, and distributes cards to each player.
         *
         * @param numberOfPlayers the number of players in the game
         */
        void initializeGame(int numberOfPlayers);

        /**
         * Simulates cutting the deck to randomize card order further.
         */
        void cutDeck();

        /**
         * Starts the turn for the specified player.
         *
         * @param player the index of the player whose turn is to start
         */
         void startTurn(int player);

        /**
         * Allows a player to play a card from their hand onto the discard pile.
         *
         * @param card the card to be played
         */
         void playCard(Card card);

        /**
         * Handles the action of a player drawing a card from the draw pile.
         *
         * @param player the index of the player who is drawing a card
         */
         void drawCard(int player);

        /**
         * Shuffles the deck and returns it.
         *
         * @return the shuffled deck
         */
         Deck shuffle();

        /**
         * Moves the game control to the next player in the sequence.
         */
         void nextPlayer();

        /**
         * Moves the game control to the previous player, typically used when the game direction reverses.
         */
         void previousPlayer();

        /**
         * Checks if the specified player has won the game.
         *
         * @param player the index of the player to check for winning condition
         * @return true if the player has won, otherwise false
         */
         boolean checkWinner(int player);

        /**
         * When the draw pile is empty, shuffles the discard pile into it to create a new draw pile.
         */
         void shuffleDiscardPileIntoDrawPile();

        /**
         * Applies effects of special cards like skipping turns or reversing game direction.
         *
         * @param card the special card whose effects need to be applied
         */
         void applySpecialCardEffects(Card card);

        /**
         * Checks if the card played is valid based on the current top card of the discard pile.
         *
         * @param card the card being played
         * @param topCard the current top card on the discard pile
         * @return true if the play is valid, otherwise false
         */
         boolean checkValidCard(Card card, Card topCard);

        /**
         * Updates the game state after each action to reflect the current status of the game.
         */
         void updateGameState();

        /**
         * Ends the game and performs any cleanup necessary.
         */
         void endGame();

        /**
         * Called when a player has only one card left and declares "Mau".
         */
         void mau();

        /**
         * Handles the penalty for a player who fails to call "Mau" when they have one card left.
         */
         void lostMau();

        /**
         * Handles the discarding of a card to the discard pile.
         *
         * @param card the card to be discarded
         */
         void discardCard(Card card);

        /**
         * Allows a player to surrender from the game, potentially affecting game dynamics.
         */
         void surrender();

        /**
         * Handles the specific rules when a Jack is played.
         */
         void playJack();

        /**
         * Handles the specific rules when a 7 is played.
         */
         void play7();

        /**
         * Handles the specific rules when an 8 is played.
         */
         void play8();

        /**
         * Handles the specific rules when an Ace is played.
         */
         void playAce();

        /**
         * Calculates and returns the current players score based on the game state.
         *
         * @return the calculated score
         */
         int calculateScore();
    }
