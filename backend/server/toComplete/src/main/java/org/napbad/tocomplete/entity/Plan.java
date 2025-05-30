package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 计划实体 - 支持长期规划
 */
@Entity
@Table(name = "plans")
public interface Plan extends BaseEntity {
    
    /**
     * 计划标题
     */
    @Column(name = "title")
    String title();
    
    /**
     * 计划描述
     */
    @Column(name = "description")
    @Nullable
    String description();
    
    /**
     * 计划类型：1-日计划，2-周计划，3-月计划，4-季度计划，5-年计划
     */
    @Column(name = "plan_type")
    int planType();
    
    /**
     * 计划状态：0-草稿，1-进行中，2-已完成，3-已暂停，4-已取消
     */
    @Column(name = "status")
    int status();
    
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
     * 所属用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user();
    
    /**
     * 父计划（支持计划层级结构）
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @Nullable
    Plan parent();
    
    /**
     * 子计划列表
     */
    @OneToMany(mappedBy = "parent")
    List<Plan> children();
    
    /**
     * 计划事项列表
     */
    @OneToMany(mappedBy = "plan")
    List<PlanItem> planItems();
    
    /**
     * 是否由AI生成
     */
    @Column(name = "ai_generated")
    boolean aiGenerated();
    
    /**
     * AI生成的元数据（JSON格式）
     */
    @Column(name = "ai_metadata")
    @Nullable
    String aiMetadata();
    
    /**
     * 完成进度（0-100）
     */
    @Column(name = "progress")
    int progress();
    
    /**
     * 计划颜色（用于日历显示）
     */
    @Column(name = "color")
    @Nullable
    String color();
} 