package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 备忘录实体
 */
@Entity
@Table(name = "memos")
public interface Memo extends BaseEntity {
    
    /**
     * 标题
     */
    @Column(name = "title")
    String title();
    
    /**
     * 内容
     */
    @Column(name = "content")
    @Nullable
    String content();
    
    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    @Column(name = "priority")
    int priority();
    
    /**
     * 状态：0-待办，1-进行中，2-已完成，3-已取消
     */
    @Column(name = "status")
    int status();
    
    /**
     * 分类ID
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    @Nullable
    Category category();
    
    /**
     * 所属用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user();
    
    /**
     * 提醒时间
     */
    @Column(name = "remind_at")
    @Nullable
    LocalDateTime remindAt();
    
    /**
     * 截止时间
     */
    @Column(name = "due_at")
    @Nullable
    LocalDateTime dueAt();
    
    /**
     * 完成时间
     */
    @Column(name = "completed_at")
    @Nullable
    LocalDateTime completedAt();
    
    /**
     * 是否需要拍照打卡
     */
    @Column(name = "require_photo")
    boolean requirePhoto();
    
    /**
     * 打卡照片URL
     */
    @Column(name = "photo_url")
    @Nullable
    String photoUrl();
    
    /**
     * 地理位置提醒
     */
    @Column(name = "location_remind")
    @Nullable
    String locationRemind();
    
    /**
     * 纬度
     */
    @Column(name = "latitude")
    @Nullable
    Double latitude();
    
    /**
     * 经度
     */
    @Column(name = "longitude")
    @Nullable
    Double longitude();
    
    /**
     * 提醒半径（米）
     */
    @Column(name = "remind_radius")
    @Nullable
    Integer remindRadius();
    
    /**
     * 标签
     */
    @ManyToMany
    @JoinTable(
        name = "memo_tags",
        joinColumnName = "memo_id",
        inverseJoinColumnName = "tag_id"
    )
    List<Tag> tags();
    
    /**
     * 子任务
     */
    @OneToMany(mappedBy = "parentMemo")
    List<SubTask> subTasks();
} 