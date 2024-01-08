package com.mypay.taskconsumer.application.port.out;


public interface BankingTaskPort {
    public boolean validBankingAccountTask(String membershipId);
    public boolean getExternalBankAccountInfoTask();
}
