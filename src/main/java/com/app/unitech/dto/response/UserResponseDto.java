package com.app.unitech.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String pin;
    private String password;
    private int age;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
