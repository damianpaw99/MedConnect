package edu.ib.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/home").permitAll()
//        .antMatchers("/").permitAll()
//        .antMatchers("/login").permitAll()
//        .antMatchers("/employee/admin/registration").permitAll() //!!!!!!! Do usuniecia, tylko w celach testowych
//        .antMatchers("**/admin/**").hasRole("ADMIN")
//        .antMatchers("/patient/registration").permitAll()
//        .antMatchers("/patient/**").hasRole("PATIENT")
//        .antMatchers("/reception/**").hasRole("RECEPTION")
//        .antMatchers("/administrator/**").hasRole("ADMINISTRATOR")
//        .antMatchers("/doctor/**").hasRole("DOCTOR")
//        .and().addFilter(new JwtFilter(authenticationManager())).httpBasic().and().csrf().disable().formLogin().disable();

        http.authorizeRequests().antMatchers("/**").permitAll(); // do testów
    }



}
