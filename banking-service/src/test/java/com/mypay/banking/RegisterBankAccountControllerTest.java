package com.mypay.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.banking.adapter.in.web.RegisterBankAccountRequest;
import com.mypay.banking.domain.RegisteredBankAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterBankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
    @Test
    public void testRegisterBankAccount() throws Exception {
        RegisterBankAccountRequest request = new RegisterBankAccountRequest("국민은행", "1234567890", true);
        RegisteredBankAccount expect = RegisteredBankAccount.generateRegisteredBankAccount(
                new RegisteredBankAccount.RegisteredBankAccountId("1"),
                new RegisteredBankAccount.MembershipId("1"),
                new RegisteredBankAccount.BankName("국민은행"),
                new RegisteredBankAccount.BankAccountNumber("1234567890"),
                new RegisteredBankAccount.LinkedStatusIsValid(true),
                new RegisteredBankAccount.AggregateIdentifier("")
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/banking/accounts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(expect)));
    }


}

