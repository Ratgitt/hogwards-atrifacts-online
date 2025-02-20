package com.ratmir.spring.hogwardsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record UserDto(
        Long id,

        @NotEmpty(message = "Username is required")
        String username,

        Boolean enabled,

        @NotEmpty(message = "Roles are required")
        String roles
) {
}
