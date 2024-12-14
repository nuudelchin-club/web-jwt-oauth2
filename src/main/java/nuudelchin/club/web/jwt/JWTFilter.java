package nuudelchin.club.web.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nuudelchin.club.web.dto.CustomOAuth2User;
import nuudelchin.club.web.dto.UserDTO;

public class JWTFilter extends OncePerRequestFilter {
	
	private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("JWTFilter");
		
		String accessToken = null;
        Cookie[] cookies = request.getCookies();
        
        if(cookies == null ) {
        	
        	filterChain.doFilter(request, response);
		    
		    return;
        }
        
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("access")) {

            	accessToken = cookie.getValue();
            	
            	break;
            }
        }

		// 토큰이 없다면 다음 필터로 넘김
		if (accessToken == null) {

		    filterChain.doFilter(request, response);
		    
		    return;
		}

		// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
		try {
		    
			jwtUtil.isExpired(accessToken);
		    
		} catch (ExpiredJwtException e) {

		    //response body
		    PrintWriter writer = response.getWriter();
		    writer.print("access token expired");

		    //response status code
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return;
		}

		// 토큰이 access인지 확인 (발급시 페이로드에 명시)
		String category = jwtUtil.getCategory(accessToken);

		if (!category.equals("access")) {

		    //response body
		    PrintWriter writer = response.getWriter();
		    writer.print("invalid access token");

		    //response status code
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return;
		}

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
        
	}
    
}
