package com.mypay.membership.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginMembershipRequest {
    private String membershipId;
    private String password;
}
