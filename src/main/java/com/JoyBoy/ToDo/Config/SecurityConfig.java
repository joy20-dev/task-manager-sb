package com.JoyBoy.ToDo.Config;

import java.beans.BeanProperty;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.JoyBoy.ToDo.Models.User;
import com.JoyBoy.ToDo.service.CustomUserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



import org.springframework.beans.factory.annotation.Autowired;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private CustomUserDetailsService userDetailsService;

     @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
            http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                    .requestMatchers("/","/api/register","/register.html","/tasks","/login","/register","/css/**","/js/**","/h2-console/**","/h2-console").permitAll()
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/tasks")
                    .permitAll()
                )
                .logout(logout ->logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .csrf(csrf -> csrf.disable());
                // .csrf(csrf -> csrf
                //     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // );
                



        return http.build();


     }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    //     http
    //         .cors(Customizer.withDefaults())
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
    //             .requestMatchers("/", "/api/register", "/register.html", "/tasks", "/login", "/register").permitAll()
    //             .requestMatchers("/h2-console/**").permitAll()
    //             .anyRequest().authenticated()
    //         )
    //         .formLogin(form -> form
    //             .loginPage("/login")
    //             .defaultSuccessUrl("/tasks")
    //             .permitAll()
    //         )
    //         .logout(logout -> logout
    //             .logoutSuccessUrl("/login?logout")
    //             .permitAll()
    //         )
    //         .headers(headers -> headers
    //             .frameOptions(frame -> frame.disable())   // REQUIRED FOR H2 CONSOLE
    //         )
    //         .csrf(csrf -> csrf
    //             .ignoringRequestMatchers("/h2-console/**")  // REQUIRED FOR H2 CONSOLE
    //         );

    //     return http.build();
    // }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public  PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public InMemoryUserDetailsManager UserDetailsService(){
    //     PasswordEncoder  encoder = new BCryptPasswordEncoder();
    //     UserDetailsService user = User.builders()
    //                                     .username(user.getUserName())
    //                                     .password(encoder.encode(user.getPassword()))
    //                                     .build();

    //     return new InMemoryUserDetailsManager(user);
    // }

    @Bean   
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);// takes the user object we created for spring through customUserDetails
        authProvider.setPasswordEncoder(passwordEncoder); // password encoder to compare the raw password with the stored password

        return authProvider;

        
    } 

    @Bean 
    public AuthenticationManager authenticationManager(HttpSecurity http, DaoAuthenticationProvider provider) throws Exception{
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.authenticationProvider(provider);
        return authBuilder.build();
    }
}

