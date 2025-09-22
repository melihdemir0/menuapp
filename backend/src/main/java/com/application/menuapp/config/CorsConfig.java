package com.application.menuapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cfg = new CorsConfiguration();
		
		cfg.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
		// API'de kullanacağın metodlar
		cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		// Authorization, Content-Type vb. header'lara izin ver
		cfg.setAllowedHeaders(List.of("*"));
		// Session cookie kullanacağımız için credentials true
		cfg.setAllowCredentials(true);
	

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cfg);
		return source;
	}
}
