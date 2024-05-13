package de.htwberlin.service;

import de.htwberlin.model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerManagerTest {

    /**
     * Tests the creation of a player.
     * It verifies that the createPlayer method of PlayerManagement produces a non-null player object.
     */
    @Test
    void createPlayer() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = playerManagement.createPlayer();
        assertNotNull(player);
    }


    @Test
    void sortPlayersCards() {
    }

    /**
     * Tests the surrender operation for a player.
     * It ensures that a player can be surrendered without errors.
     */

    @Test
    void surrender() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player(new ArrayList<>());
        playerManagement.surrender(player);
    }


    /**
     * Tests the "mau" action for a player.
     * It verifies that a player can perform the "mau" action without errors.
     */
    @Test
    void mau() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player(new ArrayList<>());
        playerManagement.mau(player);

    }

    /**
     * Tests handling of a lost "mau" scenario for a player.
     * It ensures that the lost "mau" scenario can be handled without errors.
     */
    @Test
    void lostMau() {
        PlayerManagement playerManagement = new PlayerManagement();
        Player player = new Player(new ArrayList<>());
        playerManagement.lostMau(player);
    }

}