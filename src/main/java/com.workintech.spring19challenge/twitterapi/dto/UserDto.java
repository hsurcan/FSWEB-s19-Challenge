package com.twitterapi.dto;

import java.util.Set;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        Set<RoleDto> roles
) {}
