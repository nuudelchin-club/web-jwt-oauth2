package nuudelchin.club.web.jwt;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    
    public String getCategory(String token) {
        
        return Jwts.parser()
        		.verifyWith(secretKey)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload()
        		.get("category", String.class);
    }

    public String getUsername(String token) {

        return Jwts.parser()
        		.verifyWith(secretKey)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload()
        		.get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser()
    			.verifyWith(secretKey)
    			.build()
    			.parseSignedClaims(token)
    			.getPayload()
    			.get("role", String.class);
    }
    
    public Boolean isExpired(String token) {
        return Jwts.parser()
    			.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date(System.currentTimeMillis()));
    }
    
    public String getExpiration(String token) {
    	Date expiration = Jwts.parser()
    			.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration();    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(expiration);
    }

    public String createJwt(String category, String username, String role, Long expiredMs) {

        return Jwts.builder()
        		.claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
