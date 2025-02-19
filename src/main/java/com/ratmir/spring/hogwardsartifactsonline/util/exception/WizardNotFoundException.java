package com.ratmir.spring.hogwardsartifactsonline.util.exception;

public class WizardNotFoundException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "Could not find wizard with id ";

    public WizardNotFoundException(Long wizardId) {
        super(DEFAULT_MESSAGE + wizardId);
    }
}
