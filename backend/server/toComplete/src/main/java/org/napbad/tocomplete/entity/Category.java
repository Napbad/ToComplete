package org.napbad.tocomplete.entity;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 分类实体
 */
@Entity
@Table(name = "categories")
public interface Category extends BaseEntity {
    
    /**
     * 分类名称
     */
    @Column(name = "name")
    String name();
    
    /**
     * 分类描述
     */
    @Column(name = "description")
    @Nullable
    String description();
    
    /**
     * 分类颜色
     */
    @Column(name = "color")
    @Nullable
    String color();
    
    /**
     * 分类图标
     */
    @Column(name = "icon")
    @Nullable
    String icon();
    
    /**
     * 所属用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user();
    
    /**
     * 排序序号
     */
    @Column(name = "sort_order")
    int sortOrder();
    
    /**
     * 该分类下的备忘录
     */
    @OneToMany(mappedBy = "category")
    List<Memo> memos();
} 