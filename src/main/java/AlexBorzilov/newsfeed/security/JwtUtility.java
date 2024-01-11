package AlexBorzilov.newsfeed.security;

import AlexBorzilov.newsfeed.error.ErrorCodes;
import AlexBorzilov.newsfeed.error.NewsFeedException;
import AlexBorzilov.newsfeed.service.UserService;
import io.jsonwebtoken.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

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

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes()).build()
                .parseClaimsJwt(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolvers.apply(claims);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
