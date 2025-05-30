package org.napbad.tocomplete.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 备忘录DTO
 */
public class MemoDto {
    
    /**
     * 创建备忘录DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "标题不能为空")
        private String title;
        
        private String content;
        
        @NotNull(message = "优先级不能为空")
        @Min(value = 1, message = "优先级最小为1")
        @Max(value = 4, message = "优先级最大为4")
        private Integer priority;
        
        private Long categoryId;
        private LocalDateTime remindAt;
        private LocalDateTime dueAt;
        private Boolean requirePhoto;
        private String locationRemind;
        private Double latitude;
        private Double longitude;
        private Integer remindRadius;
        private List<Long> tagIds;
    }
    
    /**
     * 更新备忘录DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String title;
        private String content;
        private Integer priority;
        private Integer status;
        private Long categoryId;
        private LocalDateTime remindAt;
        private LocalDateTime dueAt;
        private Boolean requirePhoto;
        private String locationRemind;
        private Double latitude;
        private Double longitude;
        private Integer remindRadius;
        private List<Long> tagIds;
    }
    
    /**
     * 备忘录响应DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemoInfo {
        private Long id;
        private String title;
        private String content;
        private Integer priority;
        private Integer status;
        private CategoryDto.CategoryInfo category;
        private LocalDateTime remindAt;
        private LocalDateTime dueAt;
        private LocalDateTime completedAt;
        private Boolean requirePhoto;
        private String photoUrl;
        private String locationRemind;
        private Double latitude;
        private Double longitude;
        private Integer remindRadius;
        private List<TagDto.TagInfo> tags;
        private List<SubTaskDto.SubTaskInfo> subTasks;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    /**
     * 备忘录列表查询DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryRequest {
        private Integer status;
        private Integer priority;
        private Long categoryId;
        private String keyword;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer page = 0;
        private Integer size = 20;
    }
    
    /**
     * 完成备忘录DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteRequest {
        private String photoUrl; // 如果需要拍照打卡
        private String notes;
    }
} 