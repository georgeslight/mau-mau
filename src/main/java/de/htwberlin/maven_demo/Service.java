package de.htwberlin.maven_demo;


import de.htwberlin.maven_demo.model.Card;
import de.htwberlin.maven_demo.model.Deck;
import de.htwberlin.maven_demo.model.Player;

import java.util.List;

interface Service {



        /**
         * Ends the game and performs any cleanup necessary.
         */
         void endGame(GameState game);

        /**
         * Called when a player has only one card left and declares "Mau".
         */
         void mau(Player player);

        /**
         * Handles the penalty for a player who fails to call "Mau" when they have one card left.
         */
         void lostMau(Player player);

        /**
         *
         * Handles the discarding of a card to the discard pile.
         *
         * @param card the card to be discarded
         */
         void discardCard(Card card);

        /**
         * Allows a player to surrender from the game, potentially affecting game dynamics.
         */
         void surrender(Player player);

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


    }
