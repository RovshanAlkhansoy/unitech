package com.app.unitech.controller;

import static com.app.unitech.constants.GeneralConstants.AUTHORIZATION;

import com.app.unitech.dto.request.TransferRequestDto;
import com.app.unitech.dto.response.TransferResponseDto;
import com.app.unitech.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transfer")
@RequiredArgsConstructor
@Validated
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponseDto> transfer(@RequestBody @Valid TransferRequestDto transferRequest,
                                                        @RequestHeader(name = AUTHORIZATION) String authentication) {
        TransferResponseDto response = transferService.doTransfer(transferRequest, authentication);
        return ResponseEntity.ok(response);
    }

}
