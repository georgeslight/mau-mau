package de.htwberlin.persistence.exception;

public class EntityManagerFactoryException extends RuntimeException{

    public EntityManagerFactoryException(String message) {
        super(message);
    }

    public EntityManagerFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
