package com.ratmir.spring.hogwardsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record CreateUserDto(

        @NotEmpty(message = "Username is required")
        String username,

        @NotEmpty(message = "Password is required")
        String password,

        Boolean enabled,

        @NotEmpty(message = "Roles are required")
        String roles
) {
}
