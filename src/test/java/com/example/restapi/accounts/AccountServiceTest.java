package com.example.restapi.accounts;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static com.example.restapi.accounts.AccountRole.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("Test")
class AccountServiceTest {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    /**
     * name 으로 찾은 인증 계정과 저장된 계정이 같아야함
     */
    @Test
    public void findByUsername() {
        // Given
        String email = "yki2k@naver.com";
        String password = "adfs";

        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Set.of(ADMIN, USER)) // 계층화 가능
                .build();

        accountService.saveAccount(account);

        // When
        UserDetailsService userDetailsService = (UserDetailsService) accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertThat(passwordEncoder.matches(password, userDetails.getPassword()));
    }

    @Test
    public void findByUsernameFail() {
        String username = "random@gmail.com";
        assertThatThrownBy(() -> accountService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(username);
    }
}