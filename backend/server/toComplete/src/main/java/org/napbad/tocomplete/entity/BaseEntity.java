package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * 基础实体，包含通用字段
 */
@MappedSuperclass
public interface BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id();
    
    /**
     * 创建时间
     */
    @Column(name = "created_at")
    LocalDateTime createdAt();
    
    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    @Nullable
    LocalDateTime updatedAt();
    
    /**
     * 是否删除（软删除标记）
     */
    @Column(name = "is_deleted")
    @LogicalDeleted("true")
    boolean deleted();
} 