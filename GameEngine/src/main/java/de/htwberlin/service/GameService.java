package de.htwberlin.service;

import de.htwberlin.model.Card;
import de.htwberlin.model.GameState;
import de.htwberlin.model.Player;

public class GameService implements GameManagerInterface {

    @Override
    public GameState initializeGame(int numberOfPlayers) {
//        todo
        return null;
    }

    @Override
    public Player nextPlayer(GameState gameState) {
//        todo
        return null;
    }

    @Override
    public void endGame(GameState game) {
//        todo
    }

    @Override
    public Card drawCard(int player) {
//        todo
        return null;
    }

    @Override
    public void playCard(Player player, Card card, GameState gameState) {
//        todo
    }

    @Override
    public boolean checkWinner(Player player) {
//        todo
        return false;
    }
}
