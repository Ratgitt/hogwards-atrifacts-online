package com.ratmir.spring.hogwardsartifactsonline.util.exception;

public class ObjectNotFoundException extends RuntimeException {

    public static final String ARTIFACT_NOT_FOUND_MESSAGE = "Could not find artifact with id ";
    public static final String WIZARD_NOT_FOUND_MESSAGE = "Could not find wizard with id ";

    public ObjectNotFoundException(String objectName, Long id) {
        super("Could not find " + objectName + " with id " + id);
    }
}