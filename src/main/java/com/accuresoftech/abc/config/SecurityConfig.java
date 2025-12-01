package com.accuresoftech.abc.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.accuresoftech.abc.security.CustomUserDetailsService;
import com.accuresoftech.abc.security.JwtFilter;
import com.accuresoftech.abc.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final CustomUserDetailsService userDetailsService;

	@Bean
	public JwtFilter jwtFilter() {
		return new JwtFilter(jwtProvider, userDetailsService);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cfg = new CorsConfiguration();

		cfg.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174", "http://localhost:3000",
				"https://frontend.abc.techsofast.com", // your production frontend (set when live)
				"https://backend.abc.techsofast.com" // allow self-origin for tools like Swagger
		));

		cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
		cfg.setExposedHeaders(List.of("Authorization"));
		cfg.setAllowCredentials(true);
		cfg.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cfg);

		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());

		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
				.permitAll().requestMatchers("/api/admin/**").hasRole("ADMIN").requestMatchers("/api/manager/**")
				.hasAnyRole("ADMIN", "SUB_ADMIN").requestMatchers("/api/staff/**")
				.hasAnyRole("ADMIN", "SUB_ADMIN", "STAFF").anyRequest().authenticated());

		http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authConfig)
			throws Exception {
		return authConfig.getAuthenticationManager();
	}
}
