package org.napbad.tocomplete.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.napbad.tocomplete.dto.UserDto;
import org.napbad.tocomplete.entity.User;
import org.napbad.tocomplete.repository.UserRepository;
import org.napbad.tocomplete.service.impl.UserServiceImpl;
import org.napbad.tocomplete.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.babyfish.jimmer.sql.JSqlClient;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JSqlClient sqlClient;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto.RegisterRequest registerRequest;
    private UserDto.LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new UserDto.RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setNickname("测试用户");

        loginRequest = new UserDto.LoginRequest();
        loginRequest.setUsernameOrEmail("testuser");
        loginRequest.setPassword("password123");

        // 创建模拟用户对象
        user = mock(User.class);
        when(user.id()).thenReturn(1L);
        when(user.username()).thenReturn("testuser");
        when(user.email()).thenReturn("test@example.com");
        when(user.password()).thenReturn("$2a$10$encrypted");
        when(user.nickname()).thenReturn("测试用户");
        when(user.status()).thenReturn(1);
        when(user.createdAt()).thenReturn(LocalDateTime.now());
    }

    @Test
    void testRegisterSuccess() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encrypted");
        when(sqlClient.save(any())).thenReturn(user);

        // When
        UserDto.UserInfo result = userService.register(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void testRegisterWithExistingUsername() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.register(registerRequest));
        assertEquals("用户名已存在", exception.getMessage());
    }

    @Test
    void testRegisterWithExistingEmail() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.register(registerRequest));
        assertEquals("邮箱已存在", exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyLong(), anyString())).thenReturn("jwt-token");
        when(sqlClient.update(any())).thenReturn(user);

        // When
        String token = userService.login(loginRequest);

        // Then
        assertNotNull(token);
        assertEquals("jwt-token", token);
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "$2a$10$encrypted");
        verify(jwtUtil).generateToken(1L, "testuser");
    }

    @Test
    void testLoginWithInvalidUser() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.login(loginRequest));
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void testLoginWithWrongPassword() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.login(loginRequest));
        assertEquals("密码错误", exception.getMessage());
    }

    @Test
    void testLoginWithDisabledUser() {
        // Given
        when(user.status()).thenReturn(0);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.login(loginRequest));
        assertEquals("用户已被禁用", exception.getMessage());
    }

    @Test
    void testGetUserByIdSuccess() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // When
        UserDto.UserInfo result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetUserByIdNotFound() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.getUserById(1L));
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void testUpdateUserSuccess() {
        // Given
        UserDto.UpdateRequest updateRequest = new UserDto.UpdateRequest();
        updateRequest.setNickname("新昵称");
        updateRequest.setPhone("13800138000");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(sqlClient.update(any())).thenReturn(user);

        // When
        UserDto.UserInfo result = userService.updateUser(1L, updateRequest);

        // Then
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(sqlClient).update(any());
    }

    @Test
    void testChangePasswordSuccess() {
        // Given
        UserDto.ChangePasswordRequest changePasswordRequest = new UserDto.ChangePasswordRequest();
        changePasswordRequest.setOldPassword("password123");
        changePasswordRequest.setNewPassword("newpassword123");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$newencrypted");
        when(sqlClient.update(any())).thenReturn(user);

        // When
        assertDoesNotThrow(() -> userService.changePassword(1L, changePasswordRequest));

        // Then
        verify(passwordEncoder).matches("password123", "$2a$10$encrypted");
        verify(passwordEncoder).encode("newpassword123");
        verify(sqlClient).update(any());
    }

    @Test
    void testChangePasswordWithWrongOldPassword() {
        // Given
        UserDto.ChangePasswordRequest changePasswordRequest = new UserDto.ChangePasswordRequest();
        changePasswordRequest.setOldPassword("wrongpassword");
        changePasswordRequest.setNewPassword("newpassword123");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.changePassword(1L, changePasswordRequest));
        assertEquals("原密码错误", exception.getMessage());
    }

    @Test
    void testExistsByUsername() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When
        boolean exists = userService.existsByUsername("testuser");

        // Then
        assertTrue(exists);
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    void testExistsByEmail() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When
        boolean exists = userService.existsByEmail("test@example.com");

        // Then
        assertFalse(exists);
        verify(userRepository).existsByEmail("test@example.com");
    }
} 