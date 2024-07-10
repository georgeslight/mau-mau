package de.htwberlin.persistence.repo;

import de.htwberlin.gameengine.api.model.GameState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

public class GameRepositoryImpl implements GameRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveGame(GameState gameState) {
        if (gameState.getId() == null) {
            entityManager.persist(gameState);
        } else {
            entityManager.merge(gameState);
        }
    }
}
