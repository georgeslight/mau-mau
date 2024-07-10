package de.htwberlin.persistence.repo;

import de.htwberlin.playermanagement.api.model.Player;

public interface CustomPlayerRepository {
    Player customFindMethod(Long id);
}
