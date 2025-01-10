package de.htwberlin.persistence.repo;

import de.htwberlin.gameengine.api.model.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Benutzerdefinierte Repository-Implementierung
 * Sie können benutzerdefinierte Methoden in Ihrem Repository definieren und die Implementierung in einer separaten Klasse bereitstellen.
 * Repository zur Verwaltung des Spielzustands.
 * Bietet grundlegende CRUD-Operationen und benutzerdefinierte Methoden für den Spielzustand.
 */
@Repository
public interface GameRepository extends JpaRepository<GameState, Long> {
}
