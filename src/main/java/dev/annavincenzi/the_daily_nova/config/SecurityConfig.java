package dev.annavincenzi.the_daily_nova.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import dev.annavincenzi.the_daily_nova.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/admin/dashboard", "/categories/create",
                                                                "/categories/delete/{id}", "/categories/update/{id}",
                                                                "/categories/edit/{id}")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/revisor/dashboard", "/revisor/detail/{id}",
                                                                "/accept", "/reject")
                                                .hasRole("REVISOR")
                                                .requestMatchers("/articles/create", "/articles/delete/{id}",
                                                                "/articles/delete/{id}", "/articles/edit/{id}",
                                                                "/writer/dashboard")
                                                .hasRole("WRITER")
                                                .requestMatchers("/register/**", "/register", "/", "/articles",
                                                                "/images/**", "/css/**", "articles/detail/**",
                                                                "/categories/search/{id}", "/articles/search")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .permitAll())
                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/error/403"))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .maximumSessions(1)
                                                .expiredUrl("/login?session-expired=true"));

                return http.build();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
}
