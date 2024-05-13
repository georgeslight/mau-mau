package de.htwberlin.service;

import de.htwberlin.model.GameState;
import de.htwberlin.model.Player;
import de.htwberlin.model.Rules;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameServiceTest {

    GameState gameState = mock(GameState.class);
    GameService gameService = mock(GameService.class);
    Player player1 = mock(Player.class);
    Player player2 = mock(Player.class);
    Player player3 = mock(Player.class);
    Player player4 = mock(Player.class);
    Rules rules = mock(Rules.class);
    RuleService ruleService = mock(RuleService.class);


    /**
    * initializeGame should create a game with the correct number of players.
    */
    @Test
    void correctNumberOfPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        gameState.setPlayers(players);
        assertEquals(4, gameState.getPlayers().size());
    }
    /**
     *
     */
    @Test
    void nextPlayer() {

    }

    @Test
    void endGame() {
    }

    @Test
    void drawCard() {
    }

    @Test
    void playCard() {
    }

    @Test
    void checkWinner() {
    }

}