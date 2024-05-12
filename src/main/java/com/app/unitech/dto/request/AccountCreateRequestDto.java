package com.app.unitech.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRequestDto {

    @NotBlank(message = "Account number id must not be blank")
    private String accountNumber;

    @NotNull(message = "Balance must not be null")
    private BigDecimal balance;

}
