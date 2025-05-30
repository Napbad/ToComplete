package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 标签实体
 */
@Entity
@Table(name = "tags")
public interface Tag extends BaseEntity {
    
    /**
     * 标签名称
     */
    @Column(name = "name")
    String name();
    
    /**
     * 标签颜色
     */
    @Column(name = "color")
    @Nullable
    String color();
    
    /**
     * 所属用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user();
    
    /**
     * 使用该标签的备忘录
     */
    @ManyToMany(mappedBy = "tags")
    List<Memo> memos();
} 