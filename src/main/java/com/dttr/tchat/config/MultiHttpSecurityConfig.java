package com.dttr.tchat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dttr.tchat.rest.CustomAccessDeniedHandler;
import com.dttr.tchat.rest.JwtAuthenticationTokenFilter;
import com.dttr.tchat.rest.RestAuthenticationEntryPoint;

@EnableWebSecurity
public class MultiHttpSecurityConfig {
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		/// jwt
		@Bean
		public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
			JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
			jwtAuthenticationTokenFilter.setAuthenticationManager(authenticationManager());
			return jwtAuthenticationTokenFilter;
		}

		@Bean
		public RestAuthenticationEntryPoint restServicesEntryPoint() {
			return new RestAuthenticationEntryPoint();
		}

		@Bean
		public CustomAccessDeniedHandler customAccessDeniedHandler() {
			return new CustomAccessDeniedHandler();
		}

		@Bean
		@Override
		protected AuthenticationManager authenticationManager() throws Exception {
			return super.authenticationManager();
		}

		protected void configure(HttpSecurity http) throws Exception {
			// Disable crsf cho đường dẫn /rest/**
			http.csrf().ignoringAntMatchers("/rest/**");

			http.authorizeRequests().antMatchers("/rest/login**").permitAll();

			http.antMatcher("/rest/**").httpBasic().authenticationEntryPoint(restServicesEntryPoint()).and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.authorizeRequests().anyRequest().authenticated().and()
					.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
					.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
		}
	}

	@Configuration
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
						.antMatchers("/register").permitAll()
						.antMatchers("/").authenticated()
					.and()
						.formLogin()
						.loginPage("/login")
						.usernameParameter("username").passwordParameter("password")
						.defaultSuccessUrl("/")
						.failureUrl("/login?error");
		}
	}
}
