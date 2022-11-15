package com.zerokikr.forestdb.configuration;

import com.zerokikr.forestdb.service.FDbUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class ForestDbSecurityConfig {

    @Autowired
    public FDbUserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    public ForestDbSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { // @formatter:off
//        auth.
//                inMemoryAuthentication().passwordEncoder(passwordEncoder).
//                withUser("user@mail.ru").password(passwordEncoder.encode("123")).
//                roles("USER");
//    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/users", "/subjects/reports", "/risks/reports").hasAuthority("admin")
                .antMatchers("/signup", "/users/save").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin().
                    loginPage("/login").permitAll().
                    loginProcessingUrl("/doLogin").defaultSuccessUrl("/subjects", true)

                .and()
                .logout().permitAll().logoutUrl("/doLogout")
                .and()
                .csrf().disable();;
        return http.build();
    }


}




