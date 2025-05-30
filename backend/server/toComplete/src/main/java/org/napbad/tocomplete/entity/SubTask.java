package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * 子任务实体
 */
@Entity
@Table(name = "sub_tasks")
public interface SubTask extends BaseEntity {
    
    /**
     * 子任务标题
     */
    @Column(name = "title")
    String title();
    
    /**
     * 子任务描述
     */
    @Column(name = "description")
    @Nullable
    String description();
    
    /**
     * 状态：0-待办，1-进行中，2-已完成
     */
    @Column(name = "status")
    int status();
    
    /**
     * 完成时间
     */
    @Column(name = "completed_at")
    @Nullable
    LocalDateTime completedAt();
    
    /**
     * 所属备忘录
     */
    @ManyToOne
    @JoinColumn(name = "memo_id")
    Memo parentMemo();
    
    /**
     * 排序序号
     */
    @Column(name = "sort_order")
    int sortOrder();
} 