package org.napbad.tocomplete.service;

import org.napbad.tocomplete.dto.UserDto;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    UserDto.UserInfo register(UserDto.RegisterRequest request);
    
    /**
     * 用户登录
     */
    String login(UserDto.LoginRequest request);
    
    /**
     * 根据ID获取用户信息
     */
    UserDto.UserInfo getUserById(Long id);
    
    /**
     * 更新用户信息
     */
    UserDto.UserInfo updateUser(Long id, UserDto.UpdateRequest request);
    
    /**
     * 修改密码
     */
    void changePassword(Long id, UserDto.ChangePasswordRequest request);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
} 