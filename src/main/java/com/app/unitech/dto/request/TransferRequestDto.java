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
public class TransferRequestDto {

    @NotBlank(message = " Sender account must not be blank")
    private String fromAccountNumber;

    @NotBlank(message = " Receiver account must not be blank")
    private String toAccountNumber;

    @NotNull(message = "Amount can not be null")
    private BigDecimal amount;

}

