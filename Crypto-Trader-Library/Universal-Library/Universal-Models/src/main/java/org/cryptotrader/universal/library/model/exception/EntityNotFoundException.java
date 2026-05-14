package org.cryptotrader.universal.library.model.exception;

import org.cryptotrader.universal.library.entity.IdentifiableEntity;

public class EntityNotFoundException extends RuntimeException {
    private static String DEFAULT_MESSAGE = "Entity not found";

    public EntityNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, IdentifiableEntity entity) {
        super(message + ": ID " + entity.getId() + " (" + entity.getClass().getSimpleName() + ")" );
    }

    public EntityNotFoundException(IdentifiableEntity entity) {
        super(DEFAULT_MESSAGE + ": ID " + entity.getId() + " (" + entity.getClass().getSimpleName() + ")" );
    }
}
