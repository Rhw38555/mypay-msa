package com.mypay.money.application.port.in;


import com.mypay.money.domain.MemberMoney;

import java.util.List;

public interface FindMemberMoneyUseCase {

    List<MemberMoney> findMemberMoneyListByMembershipIds(FindMemberMoneyListByMembershipIdsCommand command);
}
