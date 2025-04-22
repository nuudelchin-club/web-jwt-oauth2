package nuudelchin.club.web.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nuudelchin.club.web.dto.CustomOAuth2User;
import nuudelchin.club.web.entity.RefreshEntity;
import nuudelchin.club.web.jwt.JWTUtil;
import nuudelchin.club.web.repository.RefreshRepository;
import nuudelchin.club.web.service.SecretService;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final SecretService secretService;

    public CustomSuccessHandler(
    		JWTUtil jwtUtil, 
    		RefreshRepository refreshRepository,
    		SecretService secretService) {

        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.secretService = secretService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    	System.out.println("onAuthenticationSuccess");
    	
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", username, role, secretService.getJwtAccess());
        String refresh = jwtUtil.createJwt("refresh", username, role, secretService.getJwtRefresh());
        
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(new Date(System.currentTimeMillis() + secretService.getJwtRefresh()).toString());
        
        refreshRepository.save(refreshEntity);
        
        Cookie accessCookie = new Cookie("access", access);
        accessCookie.setMaxAge(secretService.getJwtAccessCookie());
        accessCookie.setSecure(true);		// use case is https
        accessCookie.setPath("/");			// Бүх эндпойнт дээр илгээгдэх
        accessCookie.setHttpOnly(true);		// cannot use cookie in java script
        
        Cookie refreshCookie = new Cookie("refresh", refresh);
        refreshCookie.setMaxAge(secretService.getJwtRefreshCookie());
        refreshCookie.setSecure(true);		// use case is https
        refreshCookie.setPath("/");			// Бүх эндпойнт дээр илгээгдэх
        refreshCookie.setHttpOnly(true);	// cannot use cookie in java script

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        response.setStatus(HttpStatus.OK.value());
        
        System.out.println("success, access token expire time: " + jwtUtil.getExpiration(access));
        System.out.println("success, refresh token expire time: " + jwtUtil.getExpiration(refresh));
        
        response.sendRedirect("https://nuudelchin.club/");
        //response.sendRedirect("https://localhost/");
    }
}
