package org.napbad.tocomplete.repository;

import org.babyfish.jimmer.spring.repository.JRepository;
import org.napbad.tocomplete.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 备忘录Repository
 */
@Repository
public interface MemoRepository extends JRepository<Memo, Long> {
    
    /**
     * 根据用户ID查找备忘录
     */
    Page<Memo> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和状态查找备忘录
     */
    List<Memo> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据用户ID和优先级查找备忘录
     */
    List<Memo> findByUserIdAndPriority(Long userId, Integer priority);
    
    /**
     * 查找需要提醒的备忘录
     */
    List<Memo> findByRemindAtBetweenAndStatus(LocalDateTime start, LocalDateTime end, Integer status);
    
    /**
     * 查找即将到期的备忘录
     */
    List<Memo> findByDueAtBetweenAndStatus(LocalDateTime start, LocalDateTime end, Integer status);
    
    /**
     * 根据分类查找备忘录
     */
    List<Memo> findByCategoryId(Long categoryId);
    
    /**
     * 根据标题模糊查询
     */
    List<Memo> findByTitleContainingAndUserId(String title, Long userId);
} 