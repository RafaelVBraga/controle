package br.gov.caucaia.sme.apps.controleinterno.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	UserDetailsService myUserDetailsService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authConfig -> {
			authConfig.requestMatchers(HttpMethod.GET, "/css/**").permitAll();
			authConfig.requestMatchers(HttpMethod.GET, "/js/**").permitAll();
			authConfig.requestMatchers(HttpMethod.GET, "/img/**").permitAll();
			authConfig.requestMatchers(HttpMethod.GET, "/util/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN",
					"ROLE_DEVELOPER");
			authConfig.requestMatchers(HttpMethod.GET, "/documento/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN",
					"ROLE_DEVELOPER");
			authConfig.requestMatchers(HttpMethod.POST, "/documento/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN",
					"ROLE_DEVELOPER");
			authConfig.requestMatchers(HttpMethod.GET, "/setor/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN",
					"ROLE_DEVELOPER");
			authConfig.requestMatchers(HttpMethod.POST, "/setor/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN",
					"ROLE_DEVELOPER");
			authConfig.anyRequest().authenticated();
		}).csrf(csrf -> csrf.disable()).userDetailsService(myUserDetailsService).formLogin(formLogin -> {
			formLogin.usernameParameter("username").passwordParameter("password").loginPage("/login") // onde carregar a
																										// página de
																										// login
					.loginProcessingUrl("/login") // qual o caminho está setado na página no método post
					.defaultSuccessUrl("/home", true).permitAll();

		}).logout(logout -> {
			logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll();
		})

				.httpBasic(Customizer.withDefaults());

		return http.build();

	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
