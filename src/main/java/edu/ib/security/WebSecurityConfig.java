package edu.ib.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/home").permitAll()
//        .antMatchers("/").permitAll()
//        .antMatchers("/login").permitAll()
//        .antMatchers("/employee/admin/registration").permitAll() //!!!!!!! Do usuniecia, tylko w celach testowych
//        .antMatchers("**/admin/**").hasRole("ADMIN")
//        .antMatchers("/patient/registration").permitAll()
//        .antMatchers("/patient/**").hasRole("PATIENT")
//        .antMatchers("/reception/**").hasRole("RECEPCJONISTA")
//        .antMatchers("/doctor/**").hasRole("DOCTOR")
//        .and().addFilter(new JwtFilter(authenticationManager(),signingKey)).httpBasic().and().csrf().disable().formLogin().loginPage("/moveToLogin");

        http.authorizeRequests().antMatchers("/**").permitAll();
        http.csrf().disable();// do testów
    }



}
