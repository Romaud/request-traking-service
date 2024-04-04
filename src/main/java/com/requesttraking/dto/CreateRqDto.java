package com.requesttraking.dto;

import com.requesttraking.entity.Request;
import com.requesttraking.entity.common.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRqDto {
    @NotNull
    @Size(max = 128)
    private String text;

    @Null
    private Long assigneeId;

    @NotNull
    private Long rqId;

    public Request toEntity() {
        return Request.builder()
                .text(text)
                .status(Status.DRAFT)
                .assigneeId(assigneeId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
