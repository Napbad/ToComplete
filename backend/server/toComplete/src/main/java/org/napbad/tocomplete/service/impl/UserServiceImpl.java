package org.napbad.tocomplete.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.sql.JSqlClient;
import org.napbad.tocomplete.dto.UserDto;
import org.napbad.tocomplete.entity.User;
import org.napbad.tocomplete.entity.UserDraft;
import org.napbad.tocomplete.repository.UserRepository;
import org.napbad.tocomplete.service.UserService;
import org.napbad.tocomplete.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JSqlClient sqlClient;
    
    @Override
    @Transactional
    public UserDto.UserInfo register(UserDto.RegisterRequest request) {
        // 检查用户名和邮箱是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 创建用户
        User user = UserDraft.$.produce(draft -> {
            draft.setUsername(request.getUsername())
                 .setEmail(request.getEmail())
                 .setPassword(passwordEncoder.encode(request.getPassword()))
                 .setNickname(request.getNickname())
                 .setPhone(request.getPhone())
                 .setStatus(1)
                 .setCreatedAt(LocalDateTime.now());
        });
        
        User savedUser = sqlClient.save(user);
        
        return convertToUserInfo(savedUser);
    }
    
    @Override
    public String login(UserDto.LoginRequest request) {
        // 根据用户名或邮箱查找用户
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()))
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.password())) {
            throw new RuntimeException("密码错误");
        }
        
        // 检查用户状态
        if (user.status() != 1) {
            throw new RuntimeException("用户已被禁用");
        }
        
        // 更新最后登录时间
        sqlClient.update(UserDraft.$.produce(user, draft -> {
            draft.setLastLoginAt(LocalDateTime.now());
        }));
        
        // 生成JWT Token
        return jwtUtil.generateToken(user.id(), user.username());
    }
    
    @Override
    public UserDto.UserInfo getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToUserInfo(user);
    }
    
    @Override
    @Transactional
    public UserDto.UserInfo updateUser(Long id, UserDto.UpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        User updatedUser = UserDraft.$.produce(user, draft -> {
            if (request.getNickname() != null) {
                draft.setNickname(request.getNickname());
            }
            if (request.getAvatarUrl() != null) {
                draft.setAvatarUrl(request.getAvatarUrl());
            }
            if (request.getPhone() != null) {
                draft.setPhone(request.getPhone());
            }
            draft.setUpdatedAt(LocalDateTime.now());
        });
        
        User savedUser = sqlClient.update(updatedUser);
        return convertToUserInfo(savedUser);
    }
    
    @Override
    @Transactional
    public void changePassword(Long id, UserDto.ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证原密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.password())) {
            throw new RuntimeException("原密码错误");
        }
        
        // 更新密码
        sqlClient.update(UserDraft.$.produce(user, draft -> {
            draft.setPassword(passwordEncoder.encode(request.getNewPassword()))
                 .setUpdatedAt(LocalDateTime.now());
        }));
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * 转换为UserInfo DTO
     */
    private UserDto.UserInfo convertToUserInfo(User user) {
        return new UserDto.UserInfo(
                user.id(),
                user.username(),
                user.email(),
                user.nickname(),
                user.avatarUrl(),
                user.phone(),
                user.status(),
                user.lastLoginAt(),
                user.createdAt()
        );
    }
} 