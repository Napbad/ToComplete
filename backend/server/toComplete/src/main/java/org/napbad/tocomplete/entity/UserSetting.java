package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

/**
 * 用户设置实体
 */
@Entity
@Table(name = "user_settings")
public interface UserSetting extends BaseEntity {
    
    /**
     * 所属用户
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    User user();
    
    /**
     * 时区
     */
    @Column(name = "timezone")
    @Nullable
    String timezone();
    
    /**
     * 语言设置
     */
    @Column(name = "language")
    @Nullable
    String language();
    
    /**
     * 主题设置：light, dark, auto
     */
    @Column(name = "theme")
    @Nullable
    String theme();
    
    /**
     * 是否开启推送通知
     */
    @Column(name = "push_notification")
    boolean pushNotification();
    
    /**
     * 是否开启邮件通知
     */
    @Column(name = "email_notification")
    boolean emailNotification();
    
    /**
     * 是否开启短信通知
     */
    @Column(name = "sms_notification")
    boolean smsNotification();
    
    /**
     * 提醒提前时间（分钟）
     */
    @Column(name = "reminder_advance")
    int reminderAdvance();
    
    /**
     * 工作时间开始
     */
    @Column(name = "work_start_time")
    @Nullable
    String workStartTime();
    
    /**
     * 工作时间结束
     */
    @Column(name = "work_end_time")
    @Nullable
    String workEndTime();
    
    /**
     * 专注模式设置（JSON格式）
     */
    @Column(name = "focus_mode_settings")
    @Nullable
    String focusModeSettings();
    
    /**
     * AI助手设置（JSON格式）
     */
    @Column(name = "ai_settings")
    @Nullable
    String aiSettings();
} 