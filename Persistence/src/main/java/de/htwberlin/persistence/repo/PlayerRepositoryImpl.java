package de.htwberlin.persistence.repo;

import de.htwberlin.playermanagement.api.model.Player;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Objects;

public class PlayerRepositoryImpl implements CustomPlayerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Player customFindMethod(Long id){
        return (Player) entityManager.createQuery("SELECT p FROM Player p WHERE p.id = :id")
                .setParameter("id", id)
                .getSingleResult();
     }

    @PostConstruct
    public void postConstruct() {
        Objects.requireNonNull(entityManager);
     }
}
