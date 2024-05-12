package com.app.unitech.service;

import com.app.unitech.dto.request.TransferRequestDto;
import com.app.unitech.dto.response.TransferResponseDto;

public interface TransferService {

    TransferResponseDto doTransfer(TransferRequestDto transferRequest, String authentication);

}
