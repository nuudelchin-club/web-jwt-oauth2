package nuudelchin.club.web.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nuudelchin.club.web.entity.RefreshEntity;
import nuudelchin.club.web.jwt.JWTUtil;
import nuudelchin.club.web.service.RefreshService;
import nuudelchin.club.web.service.SecretService;

@Controller
@ResponseBody
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;
    private final SecretService secretService;

    public ReissueController(
    		JWTUtil jwtUtil, 
    		RefreshService refreshService,
    		SecretService secretService) {

        this.jwtUtil = jwtUtil;
        this.refreshService = refreshService;
        this.secretService = secretService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    	
    	System.out.println("reissue");

        //get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        
        if(cookies == null ) {
        	
        	//response status code
        	return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }
        
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

            	refreshToken = cookie.getValue();
            	
            	break;
            }
        }

        if (refreshToken == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            
        	jwtUtil.isExpired(refreshToken);
            
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }
        
        //DB에 저장되어 있는지 확인
    	RefreshEntity refreshEntity = refreshService.findByRefresh(refreshToken);
    	if (refreshEntity == null) {
    		
    		//response body
    		return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
    	}

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        //make new JWT
        String newAccessToken = jwtUtil.createJwt("access", username, role, secretService.getJwtAccess());
        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, secretService.getJwtRefresh());
        
        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshService.delete(refreshToken);
    	
    	refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(newRefreshToken);
        refreshEntity.setExpiration(new Date(System.currentTimeMillis() + secretService.getJwtRefresh()).toString());
        
        refreshService.save(refreshEntity);

        //response
        Cookie accessCookie = new Cookie("access", newAccessToken);
        accessCookie.setMaxAge(secretService.getJwtAccessCookie());
        accessCookie.setSecure(true);		// use case is https
        accessCookie.setPath("/");			// Бүх эндпойнт дээр илгээгдэх
        accessCookie.setHttpOnly(true);		// cannot use cookie in java script
        
        Cookie refreshCookie = new Cookie("refresh", newRefreshToken);
        refreshCookie.setMaxAge(secretService.getJwtRefreshCookie());
        refreshCookie.setSecure(true);		// use case is https
        refreshCookie.setPath("/");			// Бүх эндпойнт дээр илгээгдэх
        refreshCookie.setHttpOnly(true);	// cannot use cookie in java script

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        response.setStatus(HttpStatus.OK.value());
        
        System.out.println("reissue, access token expire time: " + jwtUtil.getExpiration(newAccessToken));
        System.out.println("reissue, refresh token expire time: " + jwtUtil.getExpiration(newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
