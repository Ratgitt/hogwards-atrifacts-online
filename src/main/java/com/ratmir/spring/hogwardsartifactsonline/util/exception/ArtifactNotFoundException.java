package com.ratmir.spring.hogwardsartifactsonline.util.exception;

public class ArtifactNotFoundException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "Could not find artifact with id ";

    public ArtifactNotFoundException(Long artifactId) {
        super(DEFAULT_MESSAGE + artifactId);
    }
}
