package com.mypay.banking.adapter.axon.event;

import com.mypay.banking.FirmBankingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FirmbankingRequestUpdatedEvent {

    private FirmBankingStatus status;

}
