package com.twitterapi.service;

import com.twitterapi.dto.AuthResponseDto;
import com.twitterapi.dto.request.LoginRequest;
import com.twitterapi.dto.request.RegisterRequest;
import com.twitterapi.entity.Role;
import com.twitterapi.entity.User;
import com.twitterapi.exception.AlreadyExistsException;
import com.twitterapi.repository.RoleRepository;
import com.twitterapi.repository.UserRepository;
import com.twitterapi.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
                "Ali", "Veli", "aliveli", "ali@test.com", "gizli123");
    }

    @Test
    void register_shouldCreateUserWithDefaultRole_whenUsernameAndEmailAreFree() {
        Role roleUser = Role.builder().id(1L).authority("ROLE_USER").build();

        when(userRepository.existsByUsername("aliveli")).thenReturn(false);
        when(userRepository.existsByEmail("ali@test.com")).thenReturn(false);
        when(roleRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.of(roleUser));
        when(passwordEncoder.encode("gizli123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtUtil.generateToken("aliveli")).thenReturn("jwt-token");

        AuthResponseDto response = authenticationService.register(registerRequest);

        assertEquals("jwt-token", response.token());
        assertEquals(1L, response.userId());
        assertEquals("aliveli", response.username());
        verify(passwordEncoder, times(1)).encode("gizli123");
        verify(roleRepository, never()).save(any()); // rol zaten vardi, yeniden yaratilmadi
    }

    @Test
    void register_shouldCreateDefaultRole_whenRoleDoesNotExistYet() {
        Role created = Role.builder().id(1L).authority("ROLE_USER").build();

        when(userRepository.existsByUsername("aliveli")).thenReturn(false);
        when(userRepository.existsByEmail("ali@test.com")).thenReturn(false);
        when(roleRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(created);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");

        authenticationService.register(registerRequest);

        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void register_shouldThrowConflict_whenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("aliveli")).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> authenticationService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowConflict_whenEmailAlreadyExists() {
        when(userRepository.existsByUsername("aliveli")).thenReturn(false);
        when(userRepository.existsByEmail("ali@test.com")).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> authenticationService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest("aliveli", "gizli123");
        User user = User.builder().id(1L).username("aliveli").password("hashed").build();

        when(userRepository.findByUsername("aliveli")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("aliveli")).thenReturn("jwt-token");

        AuthResponseDto response = authenticationService.login(loginRequest);

        assertEquals("jwt-token", response.token());
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_shouldThrowBadCredentials_whenPasswordIsWrong() {
        LoginRequest loginRequest = new LoginRequest("aliveli", "yanlis");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class,
                () -> authenticationService.login(loginRequest));
        verify(jwtUtil, never()).generateToken(anyString());
    }
}
