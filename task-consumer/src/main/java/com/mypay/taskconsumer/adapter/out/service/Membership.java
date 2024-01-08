package com.mypay.taskconsumer.adapter.out.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Membership {

    private String membershipId;
    private String password;
    private String name;
    private String email;
    private String address;
    private String refreshToken;
    private boolean isValid;
    private boolean isCorp;

}