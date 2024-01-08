package com.mypay.remittance.application.port.in;


import com.mypay.remittance.domain.RemittanceRequest;

import java.util.List;

public interface FindRemittanceUseCase {
    List<RemittanceRequest> findRemittanceHistory(FindRemittanceCommand command);

}
