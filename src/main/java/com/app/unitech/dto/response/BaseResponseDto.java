package com.app.unitech.dto.response;

import com.app.unitech.constants.GeneralConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponseDto<T> {

    private T data;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String message;

    public static <T> BaseResponseDto<T> success(T data) {
        return new BaseResponseDto<>(data, GeneralConstants.SUCCESS);
    }

}
