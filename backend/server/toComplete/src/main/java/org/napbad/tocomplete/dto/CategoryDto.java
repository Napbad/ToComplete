package org.napbad.tocomplete.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 分类DTO
 */
public class CategoryDto {
    
    /**
     * 创建分类DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "分类名称不能为空")
        private String name;
        
        private String description;
        private String color;
        private String icon;
        private Integer sortOrder;
    }
    
    /**
     * 分类信息DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        private Long id;
        private String name;
        private String description;
        private String color;
        private String icon;
        private Integer sortOrder;
    }
} 