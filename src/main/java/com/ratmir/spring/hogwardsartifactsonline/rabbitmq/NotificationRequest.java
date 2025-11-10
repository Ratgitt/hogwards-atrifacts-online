package com.ratmir.spring.hogwardsartifactsonline.rabbitmq;

public record NotificationRequest(
        Long userId,
        String username,
        String firstname,
        String lastname,
        String message
) {
}
