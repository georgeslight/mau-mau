package de.htwberlin.persistence.repo;

import de.htwberlin.gameengine.api.model.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Custom Repository Implementation
 * You can define custom methods in your repository and provide the implementation in a separate class.
 */
@Repository
public interface GameRepository extends JpaRepository<GameState, Long>, GameRepositoryCustom {
}
