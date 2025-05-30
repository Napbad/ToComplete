package org.napbad.tocomplete.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 子任务DTO
 */
public class SubTaskDto {
    
    /**
     * 创建子任务DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "子任务标题不能为空")
        private String title;
        
        private String description;
        private Integer sortOrder;
    }
    
    /**
     * 子任务信息DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubTaskInfo {
        private Long id;
        private String title;
        private String description;
        private Integer status;
        private LocalDateTime completedAt;
        private Integer sortOrder;
    }
} 