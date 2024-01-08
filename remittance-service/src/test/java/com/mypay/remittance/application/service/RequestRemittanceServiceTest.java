package com.mypay.remittance.application.service;

import com.mypay.common.CountDownLatchManager;
import com.mypay.remittance.RemittanceType;
import com.mypay.remittance.adapter.in.web.RequestRemittanceRequest;
import com.mypay.remittance.adapter.out.persistence.RemittanceMapper;
import com.mypay.remittance.adapter.out.persistence.SpringDataRemittanceRequestRepository;
import com.mypay.remittance.adapter.out.service.membership.MembershipStatus;
import com.mypay.remittance.application.port.in.RequestRemittanceCommand;
import com.mypay.remittance.application.port.out.RequestRemittancePort;
import com.mypay.remittance.application.port.out.banking.BankingPort;
import com.mypay.remittance.application.port.out.membership.GetMembershipPort;
import com.mypay.remittance.application.port.out.money.MoneyInfo;
import com.mypay.remittance.application.port.out.money.MoneyPort;
import com.mypay.remittance.domain.RemittanceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RequestRemittanceServiceTest {

    // 테스트 로직 Inject Mocks
    @InjectMocks
    private RequestRemittanceService requestRemittanceService;

    // Inject
    // @Mock
    @Mock
    private RequestRemittancePort requestRemittancePort;
    @Mock
    private RemittanceMapper remittanceMapper;
    @Mock
    private GetMembershipPort membershipPort;
    @Mock
    private MoneyPort moneyPort;
    @Mock
    private BankingPort bankPort;

    @BeforeEach
    public void setUp() {
        // injectMocks 통해 mock 주입
        MockitoAnnotations.openMocks(this);

        /**
         * private final 필드의 경우 setter를 통해 주입할 수 없어
         * Reflection or constructor로 주입해야한다
         */
        requestRemittanceService =
                new RequestRemittanceService(requestRemittancePort, remittanceMapper, membershipPort, moneyPort, bankPort);
    }

    private static Stream<RequestRemittanceCommand> providerRequestRemittanceCommand() {
        return Stream.of(
                RequestRemittanceCommand
                        .builder()
                        .fromMembershipId("5")
                        .toMembershipId("4")
                        .toBankName("mybank")
                        .remittanceType(RemittanceType.BANK)
                        .toBankAccountNumber("123456")
                        .amount(152000)
                        .build()
        );
    }

    // 송금 고객이 유효하지 않은 경우
    @ParameterizedTest
    @MethodSource("providerRequestRemittanceCommand")
    public void testRequestRemittanceServiceWhenFromMembershipInvalid(RequestRemittanceCommand testCommand){

        // 1. 먼저 어떤 결과가 나올지에 대해 정의한다
        RemittanceRequest want = null;

        // 2. Mocking을 위한 dummy data가 있다면 그 data는 먼저 만들어준다
//        membershipPort.getMembership(testCommand.getFromMembershipId());

        // 3. 그 결과를 위해 Mocking
        when(requestRemittancePort.createRemittanceRequestHistory(testCommand))
                .thenReturn(null);

        when(membershipPort.getMembership(testCommand.getFromMembershipId()))
                .thenReturn(new MembershipStatus(testCommand.getFromMembershipId(), false));

        // 4. mocking 된 mock을 사용해 테스트 진행
        RemittanceRequest got = requestRemittanceService.requestRemittance(testCommand);

        // 5. verify를 통해 테스트 잘 진행되었나, 1번만 호출되었는지 검증
        verify(requestRemittancePort, times(1)).createRemittanceRequestHistory(testCommand);

        // 6. Assert를 통해 최종적으로 이 테스트가 잘 진행되었는지 확인
        assertEquals(want, got);
    }

    // 잔액이 충분하지 않은 경우
    @ParameterizedTest
    @MethodSource("providerRequestRemittanceCommand")
    public void testRequestRemittanceServiceWhenNotEnoughMoney(RequestRemittanceCommand testCommand){

        // 1. 먼저 어떤 결과가 나올지에 대해 정의한다
        RemittanceRequest want = null;

        // 2. 잔액 10000원
        MoneyInfo dummyMoneyInfo = new MoneyInfo(
                testCommand.getFromMembershipId(),
                10000
        );

        // 3. 10000원 잔액 부족
        when(requestRemittancePort.createRemittanceRequestHistory(testCommand))
                .thenReturn(null);

        when(membershipPort.getMembership(testCommand.getFromMembershipId()))
                .thenReturn(new MembershipStatus(testCommand.getFromMembershipId(), true));

        when(moneyPort.getMoneyInfo(testCommand.getFromMembershipId()))
                .thenReturn(dummyMoneyInfo);

        int rechargeAmount = (int) Math.ceil((testCommand.getAmount() - dummyMoneyInfo.getBalance()) / 10000.0) * 10000;

        when(moneyPort.requestMoneyRecharging(testCommand.getFromMembershipId(), rechargeAmount))
                .thenReturn(false);

        // 4. mocking 된 mock을 사용해 테스트 진행
        RemittanceRequest got = requestRemittanceService.requestRemittance(testCommand);

        // 5. verify를 통해 테스트 잘 진행되었나, 1번만 호출되었는지 검증
        verify(requestRemittancePort, times(1)).createRemittanceRequestHistory(testCommand);
        verify(membershipPort, times(1)).getMembership(testCommand.getFromMembershipId());
        verify(moneyPort, times(1)).getMoneyInfo(testCommand.getFromMembershipId());
        verify(moneyPort, times(1)).requestMoneyRecharging(testCommand.getFromMembershipId(), rechargeAmount);

        // 6. Assert를 통해 최종적으로 이 테스트가 잘 진행되었는지 확인
        assertEquals(want, got);
    }

    // 송금타입이 고객인 경우

    // 송금타입이 외부은행인 경우

}