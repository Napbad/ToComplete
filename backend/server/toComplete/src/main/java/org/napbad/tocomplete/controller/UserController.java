package org.napbad.tocomplete.controller;

import lombok.RequiredArgsConstructor;
import org.napbad.tocomplete.common.Result;
import org.napbad.tocomplete.dto.UserDto;
import org.napbad.tocomplete.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserDto.UserInfo> register(@Validated @RequestBody UserDto.RegisterRequest request) {
        try {
            UserDto.UserInfo userInfo = userService.register(request);
            return Result.success("注册成功", userInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody UserDto.LoginRequest request) {
        try {
            String token = userService.login(request);
            return Result.success("登录成功", token);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<UserDto.UserInfo> getUserById(@PathVariable Long id) {
        try {
            UserDto.UserInfo userInfo = userService.getUserById(id);
            return Result.success(userInfo);
        } catch (Exception e) {
            return Result.notFound(e.getMessage());
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<UserDto.UserInfo> updateUser(@PathVariable Long id, 
                                              @Validated @RequestBody UserDto.UpdateRequest request) {
        try {
            UserDto.UserInfo userInfo = userService.updateUser(id, request);
            return Result.success("更新成功", userInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/{id}/password")
    public Result<Void> changePassword(@PathVariable Long id, 
                                      @Validated @RequestBody UserDto.ChangePasswordRequest request) {
        try {
            userService.changePassword(id, request);
            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }
    
    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return Result.success(exists);
    }
} 