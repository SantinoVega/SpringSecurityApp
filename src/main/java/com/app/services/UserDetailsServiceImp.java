package com.app.services;

import com.app.entities.UserEntity;
import com.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity dataUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario: " + username + " no existe."));

        List<GrantedAuthority> authorities = new ArrayList<>();
        dataUser.getRoles().forEach( role ->
                authorities.add(
                        new SimpleGrantedAuthority("ROLE".concat(role.getRoleEnum().name()))
                )
        );

        dataUser.getRoles().stream()
                .flatMap( role -> role.getPermissions().stream())
                .forEach( permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(dataUser.getUsername(),
                dataUser.getPassword(),
                dataUser.isEnabled(),
                dataUser.isAccountNonExpired(),
                dataUser.isAccountNonLocked(),
                dataUser.isCredentialsNonExpired(),
                authorities);
    }
}
