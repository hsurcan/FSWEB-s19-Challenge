package com.twitterapi.service;

import com.twitterapi.dto.AuthResponseDto;
import com.twitterapi.dto.request.LoginRequest;
import com.twitterapi.dto.request.RegisterRequest;
import com.twitterapi.entity.Role;
import com.twitterapi.entity.User;
import com.twitterapi.exception.AlreadyExistsException;
import com.twitterapi.exception.UserNotFoundException;
import com.twitterapi.repository.RoleRepository;
import com.twitterapi.repository.UserRepository;
import com.twitterapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDto register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new AlreadyExistsException(
                    "Bu kullanici adi zaten kayitli: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException(
                    "Bu email zaten kayitli: " + request.email());
        }

        // Varsayilan rol yoksa olusturulur (ilk kayitta roles tablosu bos olabilir).
        Role userRole = roleRepository.findByAuthority(DEFAULT_ROLE)
                .orElseGet(() -> roleRepository.save(
                        Role.builder().authority(DEFAULT_ROLE).build()));

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(userRole))
                .build();

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getUsername());

        return new AuthResponseDto(token, saved.getId(), saved.getUsername());
    }

    public AuthResponseDto login(LoginRequest request) {
        // Hatali bilgide BadCredentialsException -> GlobalExceptionHandler 401 doner.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException(request.username()));

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponseDto(token, user.getId(), user.getUsername());
    }
}
