package ProgramacionNCapasNoviembre25.PokeAPI.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/registro", "/verificar-email", "/login").permitAll()
                .requestMatchers("/pokemon", "/pokemon/favoritos", "/pokemon/{idOrName}").hasAnyRole("Usuario", "Administrador")                        
                .requestMatchers("/usuarios/**", "/admin/**", "/pokemon/ranking").hasRole("Administrador")
                .requestMatchers("/pokemon", "/pokemon**").authenticated()
                .anyRequest().authenticated()
                ).exceptionHandling(exception -> exception
                .accessDeniedPage("/error")
                )
                .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/pokemon", true)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
