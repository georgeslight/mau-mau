package de.htwberlin.persistence.repo;

import de.htwberlin.gameengine.api.model.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateRepository extends JpaRepository<GameState, Long> {
}
