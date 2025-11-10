//package com.ratmir.spring.hogwardsartifactsonline.security;
//
//import com.ratmir.spring.hogwardsartifactsonline.dto.Result;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
////@RestController
//@RequestMapping("${api.endpoint.base-url}/users")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/login")
//    public Result login(Authentication authentication) {
//
//        return Result.builder()
//                .flag(true)
//                .code(HttpStatus.OK.value())
//                .message("User Into and JWT")
//                .data(authService.createLoginInfo(authentication))
//                .build();
//    }
//}
