package com.requesttraking.dto;

import com.requesttraking.entity.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRqDto extends CreateRqDto {
    @NotNull
    private Long id;

    @Override
    public Request toEntity() {
        Request request = super.toEntity();
        request.setId(id);
        return request;
    }
}
