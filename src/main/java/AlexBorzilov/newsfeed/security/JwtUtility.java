package AlexBorzilov.newsfeed.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtility {

    @Value("${jwt.secret}")
    public String secret;

    @Value("${jwt.bearer}")
    public String bearer;

    public String getToken(UUID id) {

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 900)); // 15 minutes
        byte[] byteSecret = secret.getBytes();

        String token;
        token = bearer + Jwts.builder()
                .claim("id", id.toString())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, byteSecret)
                .compact();
        return token;
    }

}
