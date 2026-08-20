package com.twitterapi.dto.converter;

import com.twitterapi.dto.RoleDto;
import com.twitterapi.dto.UserDto;
import com.twitterapi.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDtoConverter {

    public UserDto convert(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(r -> new RoleDto(r.getId(), r.getAuthority()))
                        .collect(Collectors.toSet())
        );
    }
}
