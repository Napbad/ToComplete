package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * 计划事项实体 - 计划的具体执行项
 */
@Entity
@Table(name = "plan_items")
public interface PlanItem extends BaseEntity {
    
    /**
     * 事项标题
     */
    @Column(name = "title")
    String title();
    
    /**
     * 事项内容
     */
    @Column(name = "content")
    @Nullable
    String content();
    
    /**
     * 开始时间
     */
    @Column(name = "start_time")
    LocalDateTime startTime();
    
    /**
     * 结束时间
     */
    @Column(name = "end_time")
    LocalDateTime endTime();
    
    /**
     * 状态：0-未开始，1-进行中，2-已完成，3-已延期，4-已取消
     */
    @Column(name = "status")
    int status();
    
    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    @Column(name = "priority")
    int priority();
    
    /**
     * 所属计划
     */
    @ManyToOne
    @JoinColumn(name = "plan_id")
    Plan plan();
    
    /**
     * 关联的备忘录（可选）
     */
    @ManyToOne
    @JoinColumn(name = "memo_id")
    @Nullable
    Memo memo();
    
    /**
     * 完成时间
     */
    @Column(name = "completed_at")
    @Nullable
    LocalDateTime completedAt();
    
    /**
     * 实际用时（分钟）
     */
    @Column(name = "actual_duration")
    @Nullable
    Integer actualDuration();
    
    /**
     * 预计用时（分钟）
     */
    @Column(name = "estimated_duration")
    @Nullable
    Integer estimatedDuration();
    
    /**
     * 备注
     */
    @Column(name = "notes")
    @Nullable
    String notes();
} 