package com.mypay.money.application.port.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredBankAccountAggreateIdentifier {
    private String registeredBankAccountId;

    private String aggreateIdentifier;

    private String membershipId;

    private String bankName;

    private String bankAccountNumber;

    @Override
    public String toString() {
        return "RegisteredBankAccountAggreateIdentifier{" +
                "registeredBankAccountId='" + registeredBankAccountId + '\'' +
                ", aggreateIdentifier='" + aggreateIdentifier + '\'' +
                ", membershipId='" + membershipId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                '}';
    }
}
