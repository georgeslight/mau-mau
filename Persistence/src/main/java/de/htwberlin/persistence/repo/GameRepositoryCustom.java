package de.htwberlin.persistence.repo;

import de.htwberlin.gameengine.api.model.GameState;

/**
 * Benutzerdefiniertes Repository für zusätzliche Spielzustandsoperationen.
 * Definiert benutzerdefinierte Methoden zur Verwaltung des Spielzustands.
 */
public interface GameRepositoryCustom {

    /**
     * Speichert den Spielzustand.
     *
     * @param gameState der zu speichernde Spielzustand
     */
    void saveGame(GameState gameState);
}
