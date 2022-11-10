package com.example.restapi.config;

import com.example.restapi.accounts.Account;
import com.example.restapi.accounts.AccountRole;
import com.example.restapi.accounts.AccountService;
import com.example.restapi.common.RestDocsConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthServerConfigTest{
    @Autowired
    AccountService accountService;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("인증 토큰을 발급받는 테스트")
    @Test
    public void getAuthToken() throws Exception {
        String email = "kkk@naver.com";
        String password = "asf!@";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        accountService.saveAccount(account);


        String clientId = "myApp";
        String clientSecret = "Pass";
        mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret))
                        .param("username", account.getEmail())
                        .param("password", account.getPassword())
                        .param("grant_type", "password")
                        .contentType("application/x-www-form-urlencoded"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());

    }

}