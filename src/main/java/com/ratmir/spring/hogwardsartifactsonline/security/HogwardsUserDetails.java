//package com.ratmir.spring.hogwardsartifactsonline.security;
//
//import com.ratmir.spring.hogwardsartifactsonline.entity.HogwardsUser;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Arrays;
//import java.util.Collection;
//
//@Getter
//public class HogwardsUserDetails implements UserDetails {
//
//    private HogwardsUser hogwardsUser;
//
//    public HogwardsUserDetails(HogwardsUser hogwardsUser) {
//        this.hogwardsUser = hogwardsUser;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Arrays.stream(hogwardsUser.getRoles().split(" "))
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                .toList();
//    }
//
//    @Override
//    public String getPassword() {
//        return hogwardsUser.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return hogwardsUser.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return hogwardsUser.getEnabled();
//    }
//}
