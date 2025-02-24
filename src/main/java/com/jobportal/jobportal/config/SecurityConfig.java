package com.jobportal.jobportal.config;

import com.jobportal.jobportal.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler){
        this.customUserDetailsService=customUserDetailsService;
        this.customAuthenticationSuccessHandler=customAuthenticationSuccessHandler;
    }
    private final String[] publicUrls={
            "/",
            "/register/**",
            "/error",
            "/global-search/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/fonts/**",
            "/js/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/webjars/**"
    };

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(config->config.requestMatchers(publicUrls).permitAll()
                                                 .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider());
        http.formLogin(form->{form.loginPage("/login").permitAll();
                              form.successHandler(customAuthenticationSuccessHandler);});
        http.csrf(csrf->csrf.disable());
        http.cors(Customizer.withDefaults());

        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
