package com.app.services;

import com.app.dto.AuthCreateUserRequest;
import com.app.dto.AuthLoginRequest;
import com.app.dto.AuthResponse;
import com.app.entities.RoleEntity;
import com.app.entities.UserEntity;
import com.app.repositories.RoleRepository;
import com.app.repositories.UserRepository;
import com.app.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity dataUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario: " + username + " no existe."));

        List<GrantedAuthority> authorities = new ArrayList<>();
        dataUser.getRoles().forEach( role ->
                authorities.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))
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

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String userName = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(userName, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse response = new AuthResponse(userName,"User Logged successfully", accessToken,true);
        return response;
    }

    public Authentication authenticate(String userName, String password) {
        UserDetails userDetails = loadUserByUsername(userName);

        if(userDetails == null) {
            new BadCredentialsException("Invalid username or password.");
        }

        if(!passwordEncoder.matches(password,userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password.");
        }
        return new UsernamePasswordAuthenticationToken(userName,userDetails.getPassword(),userDetails.getAuthorities());
    }

    public AuthResponse createuser(AuthCreateUserRequest request){
        String userName = request.username();
        String password = request.password();
        List<String> roleRequests = request.roleRequest().roleListName();

        Set<RoleEntity> roleEntitySet = roleRepository.findRoleEntitiesByRoleEnumIn(roleRequests)
                .stream().collect(Collectors.toSet());

        if (roleEntitySet.isEmpty()){
            throw new IllegalArgumentException("the role specified not exist");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(userName)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .build();

        UserEntity userCreated = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userCreated.getRoles().forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        userCreated.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(),userCreated.getPassword(),authorityList);

        String accessToken = jwtUtils.createToken(authentication);
        AuthResponse response = new AuthResponse(userName,"User created successfully",accessToken,true);

        return response;

    }


}
