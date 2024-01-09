package AlexBorzilov.newsfeed.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtilities {

    @Value("${jwt.secret}")
    public String secret;
    @Value("${jwt.bearer}")
    public String bearer;

    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    public String getToken(UUID id) {

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 30)); // 30 seconds
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
