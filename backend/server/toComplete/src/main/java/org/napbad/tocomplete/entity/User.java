package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体
 */
@Entity
@Table(name = "users")
public interface User extends BaseEntity {
    
    /**
     * 用户名（唯一）
     */
    @Column(name = "username")
    @Key
    String username();
    
    /**
     * 邮箱（唯一）
     */
    @Column(name = "email")
    @Key
    String email();
    
    /**
     * 密码（加密后）
     */
    @Column(name = "password")
    String password();
    
    /**
     * 昵称
     */
    @Column(name = "nickname")
    @Nullable
    String nickname();
    
    /**
     * 头像URL
     */
    @Column(name = "avatar_url")
    @Nullable
    String avatarUrl();
    
    /**
     * 手机号
     */
    @Column(name = "phone")
    @Nullable
    String phone();
    
    /**
     * 用户状态：0-禁用，1-正常
     */
    @Column(name = "status")
    int status();
    
    /**
     * 最后登录时间
     */
    @Column(name = "last_login_at")
    @Nullable
    LocalDateTime lastLoginAt();
    
    /**
     * 用户的备忘录列表
     */
    @OneToMany(mappedBy = "user")
    List<Memo> memos();
    
    /**
     * 用户的计划列表
     */
    @OneToMany(mappedBy = "user")
    List<Plan> plans();
    
    /**
     * 用户设置
     */
    @OneToOne(mappedBy = "user")
    @Nullable
    UserSetting userSetting();
} 