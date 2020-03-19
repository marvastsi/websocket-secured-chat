package com.marvastsi.securedwebsocketchat.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.marvastsi.securedwebsocketchat.auth.AuthFilter;
import com.marvastsi.securedwebsocketchat.auth.UnauthorizedAuth;

@Configuration
@Order(1)
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("unauthorizedAuth")
	private UnauthorizedAuth unauthorizedHandler;

	@Bean
	public AuthFilter authFilterConfig() throws Exception {
		return new AuthFilter();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO configurar matchers para rotas rest
//		http.authorizeRequests().antMatchers("/index.html", "/webjars/**", "/js/**").permitAll();
		
		http.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("**", "/index.html", "/webjars/**", "/js/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	
		http.addFilterBefore(authFilterConfig(), UsernamePasswordAuthenticationFilter.class);
		http.headers().frameOptions().disable();
		http.httpBasic().disable();
	}
}
