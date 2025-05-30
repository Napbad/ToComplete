package org.napbad.tocomplete.repository;

import org.babyfish.jimmer.spring.repository.JRepository;
import org.napbad.tocomplete.entity.Plan;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 计划Repository
 */
@Repository
public interface PlanRepository extends JRepository<Plan, Long> {
    
    /**
     * 根据用户ID查找计划
     */
    List<Plan> findByUserId(Long userId);
    
    /**
     * 根据用户ID和计划类型查找计划
     */
    List<Plan> findByUserIdAndPlanType(Long userId, Integer planType);
    
    /**
     * 根据用户ID和状态查找计划
     */
    List<Plan> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 查找时间范围内的计划
     */
    List<Plan> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 查找父计划为空的顶级计划
     */
    List<Plan> findByUserIdAndParentIsNull(Long userId);
    
    /**
     * 根据父计划ID查找子计划
     */
    List<Plan> findByParentId(Long parentId);
    
    /**
     * 查找AI生成的计划
     */
    List<Plan> findByUserIdAndAiGenerated(Long userId, Boolean aiGenerated);
} 