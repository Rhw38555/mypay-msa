package com.mypay.remittance.adapter.out.service.membership;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipStatus {
    private String membershipId;
    private boolean isValid;
}
