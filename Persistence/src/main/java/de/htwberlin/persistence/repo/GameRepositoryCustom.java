package de.htwberlin.persistence.repo;

import de.htwberlin.gameengine.api.model.GameState;

public interface GameRepositoryCustom {
    void saveGame(GameState gameState);
}
