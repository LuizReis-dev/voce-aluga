package com.cefet.vocealuga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	@Order(1)
	public SecurityFilterChain operadorSecurity(HttpSecurity http) throws Exception {
		http
				.securityMatcher("/admin/**", "/admin/login")
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/admin/login").permitAll()
						.anyRequest().hasAnyRole("OPERADOR", "ADMINISTRADOR"))
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/admin/api/**")
				)
				.formLogin(form -> form
						.loginPage("/admin/login")
						.loginProcessingUrl("/admin/login")
						.defaultSuccessUrl("/admin/home", true)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/admin/logout")
						.logoutSuccessUrl("/admin/login")
						.permitAll())
				.exceptionHandling(ex -> ex
						.accessDeniedHandler(new AcessoNegadoHandler()));

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain clienteSecurity(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/cadastro", "/css/**", "/js/**", "/uploads/**").permitAll()
						.anyRequest().hasRole("CLIENTE"))
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/", true)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login")
						.permitAll())
				.exceptionHandling(ex -> ex
						.accessDeniedHandler(new AcessoNegadoHandler()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
}