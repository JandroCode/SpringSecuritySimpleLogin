package com.example.prueba.config;

;
import com.example.prueba.utils.SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SuccessHandler successHandler;

    public SecurityConfig(SuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Deshabilitar CSRF si no es necesario
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/public/**").permitAll()  // Permitir acceso público
                        .requestMatchers("/admin/**").hasRole("ADMIN")        // Solo usuarios con rol ADMIN pueden acceder a /admin/**
                        .requestMatchers("/user/**").hasRole("USER")          // Solo usuarios con rol USER pueden acceder a /user/**
                        .anyRequest().authenticated()                         // Todo lo demás requiere autenticación
                )
                .formLogin(form -> form
                        .loginPage("/login")                                  // Página de login personalizada
                        .successHandler(successHandler)                       // Maneja el login exitoso con el SuccessHandler personalizado
                        .permitAll()                                          // Permitir acceso a todos a la página de login
                )
                .logout(LogoutConfigurer::permitAll);                     // Permitir logout para todos

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build());
        userDetailsService.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build());
        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usa BCrypt para codificar contraseñas
    }
}
