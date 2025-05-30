package org.napbad.tocomplete.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 标签DTO
 */
public class TagDto {
    
    /**
     * 创建标签DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "标签名称不能为空")
        private String name;
        
        private String color;
    }
    
    /**
     * 标签信息DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagInfo {
        private Long id;
        private String name;
        private String color;
    }
} 