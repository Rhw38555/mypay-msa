package com.mypay.banking.adapter.out.persistence.external.firmbanking;

import com.mypay.banking.FirmBankingStatus;
import com.mypay.banking.application.port.out.GetExternalBankAccountInfoPort;
import com.mypay.banking.application.port.out.RequestExternalFirmbankingPort;
import com.mypay.common.ExternalAdapter;
import lombok.RequiredArgsConstructor;

@ExternalAdapter
@RequiredArgsConstructor
public class RequestFirmbankingServiceAdapter implements GetExternalBankAccountInfoPort, RequestExternalFirmbankingPort {


    @Override
    public GetExternalBankAccountInfoResponse getExternalBankAccountInfo(GetExternalBankAccountInfoRequest request) {

        // 실제 외부 http를 통해 은행계좌 정보를 가져와 비교해야됨
        // 외부 통신이 없어 외부 펌뱅킹 서비스는 계좌 확인이 무조건 true 라고 가정
        return new GetExternalBankAccountInfoResponse(request.getBankName(), request.getBankAccountNumber(), true);
    }

    @Override
    public FirmbankingResult requestExternalFirmbanking(ExternalFirmbankingReqeust request) {
        // 실제 외부 은행에 http 통신해서
        // 펌뱅킹 요청을 하고

        // 외부 은행의 실제 결과를 FirmbankingResult로 파싱한다
        return new FirmbankingResult(FirmBankingStatus.REQUEST);
    }
}
