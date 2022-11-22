package com.example.restapi.config;

import com.example.restapi.accounts.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.PathMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthenticationManager 를 만드는 AuthenticationManager Builder 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g
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * 필터 적용 여부
     * web 에서 거름
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/index.html");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 필터 적용 여부
     * 스프링 시큐리티 안에서 거름
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.anonymous()
//                .and()
//            .formLogin()
//                .and()
//            .authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/api/**").authenticated()
//                .anyRequest().authenticated();
//    }
}
