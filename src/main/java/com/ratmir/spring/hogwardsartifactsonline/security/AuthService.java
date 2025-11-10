//package com.ratmir.spring.hogwardsartifactsonline.security;
//
//import com.ratmir.spring.hogwardsartifactsonline.entity.HogwardsUser;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
////@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
//
//    private final JwtProvider jwtProvider;
//
//    public Map<String, Object> createLoginInfo(Authentication authentication) {
//
//        // Create user info
//        HogwardsUserDetails principal = (HogwardsUserDetails) authentication.getPrincipal();
//        HogwardsUser hogwardsUser = principal.getHogwardsUser();
//
//
//        // Create JWT
//
//
//        LOGGER.debug("Authenticated user: '{}'", authentication.getName());
//        return null;
//    }
//
//
//}
