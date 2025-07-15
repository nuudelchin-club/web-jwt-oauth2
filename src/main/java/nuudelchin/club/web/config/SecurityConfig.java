package nuudelchin.club.web.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import nuudelchin.club.web.jwt.CustomLogoutFilter;
import nuudelchin.club.web.jwt.JWTFilter;
import nuudelchin.club.web.jwt.JWTUtil;
import nuudelchin.club.web.oauth2.CustomClientRegistrationRepository;
import nuudelchin.club.web.oauth2.CustomOAuth2AuthorizedClientService;
import nuudelchin.club.web.oauth2.CustomSuccessHandler;
import nuudelchin.club.web.repository.RefreshRepository;
import nuudelchin.club.web.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomClientRegistrationRepository customClientRegistrationRepository;
	private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    
    @Value("${app.url}")
    private String appUrl;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService
    		, CustomClientRegistrationRepository customClientRegistrationRepository
    		, CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService
    		, JdbcTemplate jdbcTemplate
    		, CustomSuccessHandler customSuccessHandler
    		, JWTUtil jwtUtil
    		, RefreshRepository refreshRepository) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customClientRegistrationRepository = customClientRegistrationRepository;
        this.customOAuth2AuthorizedClientService = customOAuth2AuthorizedClientService;
        this.jdbcTemplate = jdbcTemplate;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	http
				.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
		
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					
						CorsConfiguration configuration = new CorsConfiguration();
						
						configuration.setAllowedOrigins(Collections.singletonList(appUrl));
						configuration.setAllowedMethods(Collections.singletonList("*"));
						configuration.setAllowCredentials(true);
						configuration.setAllowedHeaders(Collections.singletonList("*"));
						configuration.setMaxAge(3600L);
						
						configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
						configuration.setExposedHeaders(Collections.singletonList("access"));
						
						return configuration;
					}
				}));

        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());

        http
        		.oauth2Login((oauth2) -> oauth2
        				.clientRegistrationRepository(customClientRegistrationRepository.clientRegistrationRepository())    			
            			.authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepository.clientRegistrationRepository()))
        				.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
        						.userService(customOAuth2UserService))
        				.successHandler(customSuccessHandler)
				);        
        
        http
		        .authorizeHttpRequests((auth) -> auth
		        		.requestMatchers("reissue").permitAll()
		                .anyRequest().authenticated());
        
        http
				.addFilterAfter(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        
        http
        		.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
