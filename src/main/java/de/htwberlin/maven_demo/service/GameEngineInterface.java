package de.htwberlin.maven_demo.service;

import de.htwberlin.maven_demo.GameState;
import de.htwberlin.maven_demo.model.Card;
import de.htwberlin.maven_demo.model.Deck;
import de.htwberlin.maven_demo.model.Player;

import java.util.List;

public interface GameEngineInterface {

    /**
     * Initializes the game with the specified number of players.
     * Sets up the deck, shuffles it, and distributes cards to each player.
     *
     * @param numberOfPlayers the number of players in the game
     */
    GameState initializeGame(int numberOfPlayers);

    /**
     * Creates a player to a game
     * @param gameState Game where the Player should be added to
     * @return new Player
     */
    Player createPlayer(GameState gameState);

    /**
     * Starts the turn for the specified player.
     *
     * @param player the index of the player whose turn is to start
     */
    void startTurn(int player);

    /**
     * Shuffles the deck and returns it.
     *
     * @return the shuffled deck
     */
    Deck shuffle(Deck deck);

    /**
     * Moves the game control to the next player in the sequence.
     */
    void nextPlayer();

    /**
     * sort cards in the players hand to optimize game experience
     * @param player
     * @return list of the sorted Cards -> Players hand
     */
    List<Card> sortPlayersCards(Player player);

    /**
     * Checks if the specified player has won the game.
     *
     * @param player the index of the player to check for winning condition
     * @return true if the player has won, otherwise false
     */
    boolean checkWinner(Player player);

    /**
     * When the draw pile is empty, shuffles the discard pile into it to create a new draw pile.
     */
    void shuffleDiscardPileIntoDrawPile();


    /**
     * Ends the game and performs any cleanup necessary.
     */
    void endGame(GameState game);

    /**
     * Calculates and returns the current players score based on the game state.
     *
     * @return the calculated score
     */
    int calculateScore();
}
