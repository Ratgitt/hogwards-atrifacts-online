//package com.ratmir.spring.hogwardsartifactsonline.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Value("${api.endpoint.base-url}")
//    private String BASE_URL;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests.anyRequest().permitAll())
////                .authorizeHttpRequests(authorize -> authorize
////                        .requestMatchers(HttpMethod.GET, BASE_URL + "/artifacts/**").permitAll()
////                        .requestMatchers(HttpMethod.GET, BASE_URL + "/users/**").hasAuthority("ROLE_ADMIN")
////                        .requestMatchers(HttpMethod.POST, BASE_URL + "/users").hasAuthority("ROLE_ADMIN")
////                        .requestMatchers(HttpMethod.PUT, BASE_URL + "/users/**").hasAuthority("ROLE_ADMIN")
////                        .requestMatchers(HttpMethod.DELETE, BASE_URL + "/users/**").hasAuthority("ROLE_ADMIN")
////                        .anyRequest().authenticated()
////                )
//                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
//                .build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }
//}