package com.app.config;

import com.app.services.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Cuando se realiza una implementacion de spring security para consultar usuarios en BD deben existir 5 componentes
     1-filterChain, 2-authenticationManager, 3-authenticationProvider, 4-passwordEncode, 5-userDetailsService-(capa para la consulta en BD).
     Debe existir una clase de @Configuration donde se crean los beans de los componetes anteriores
     1-Recibe como parametro HttpSecurity y se configuran los filtros que se aplicaran a todas las peticiones
     2-Recibe como parametro AuthenticationConfiguration y devuelve AuthenticationManager
     3-No recibe parametros pero contiene la logica para devolver AuthenticationProvider, utililiza los componentes(4 y 5)
     4-Codifica el password con los diferentes metodos. (Para pruebas locales no estamos codificando el password)
     5-Realiza operaciones en DB. (Para pruebas locales estamos cargando en memoria dos usuarios)
     **/

    // 1-filterChain: Configuracion basica del los filtros
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(cr -> cr.disable())
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorization -> {
//                    authorization.requestMatchers(HttpMethod.GET, "/auth/get").permitAll();
//                    authorization.requestMatchers(HttpMethod.GET, "/auth/post").hasAnyAuthority("CREATE","READ");
//                    authorization.requestMatchers(HttpMethod.GET, "/auth/patch").hasAnyRole("REFACTOR");
//                    authorization.anyRequest().denyAll();
//                })
//                .build();
//    }

    // 1-filterChain: Configuracion de filtros con la notacion @Preauthorize() en el controlador y se quita .authorizeHttpRequests de ese metodo
    // Se debe agregar @EnableMethodSecurity en esta clase (clase de configuracion) para habilitar la notacion @Preauthorize() en el controlador
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(cr -> cr.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // 2-Recibe como parametro un AuthenticationConfiguration y devuelve un AuthenticationManager.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 3-No recibe parametros pero contiene la logica para devolver AuthenticationProvider, utililiza los componentes(4 y 5)
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImp userDetails) throws Exception {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordEncoder());
        dao.setUserDetailsService(userDetails);
        return dao;
    }

    // 4-Codifica el password con los diferentes metodos. (Para pruebas locales no estamos codificando el password)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 5-Realiza operaciones en DB. (Para pruebas locales estamos cargando en memoria dos usuarios). Se hara en un Service
    /*
    public UserDetailsService userDetailsService(){
        List<UserDetails> ListUserDetails = new ArrayList<>();
        ListUserDetails.add(User.withUsername("daniel")
                .password("1234")
                .authorities("READ","CREATE")
                .roles("ADMIN","USER")
                .build());
        ListUserDetails.add(User.withUsername("pedro")
                .password("1234")
                .authorities("READ")
                .roles("USER")
                .build());

        return new InMemoryUserDetailsManager(ListUserDetails);
    }
     */

}
