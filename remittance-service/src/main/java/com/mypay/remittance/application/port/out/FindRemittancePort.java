package com.mypay.remittance.application.port.out;


import com.mypay.remittance.adapter.out.persistence.RemittanceRequestJpaEntity;
import com.mypay.remittance.application.port.in.FindRemittanceCommand;
import com.mypay.remittance.application.port.in.RequestRemittanceCommand;

import java.util.List;

public interface FindRemittancePort {
    List<RemittanceRequestJpaEntity> findRemittanceHistory(FindRemittanceCommand command);
}
