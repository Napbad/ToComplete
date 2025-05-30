package org.napbad.tocomplete.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 分页响应结果类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    
    /**
     * 数据列表
     */
    private List<T> content;
    
    /**
     * 当前页号（从0开始）
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer size;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 是否第一页
     */
    private Boolean first;
    
    /**
     * 是否最后一页
     */
    private Boolean last;
    
    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> content, Integer page, Integer size, Long total) {
        PageResult<T> result = new PageResult<>();
        result.setContent(content);
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        result.setTotalPages((int) Math.ceil((double) total / size));
        result.setFirst(page == 0);
        result.setLast(page >= result.getTotalPages() - 1);
        return result;
    }
} 