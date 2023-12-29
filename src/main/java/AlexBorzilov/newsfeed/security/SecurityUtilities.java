package AlexBorzilov.newsfeed.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtilities {
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    public String getToken(UUID id) {

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 30)); // 30 seconds
        byte[] secret = "secrefryukkftjuftjdfytjcfytjfmgtjuytufytjdcytjcdytjgdftjufcytjufytjufvjftjuvfytuftfytjuftukffytjfytt".getBytes();

        String token;
        token = Jwts.builder()
                .claim("id", id.toString())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
//        String token;
//        token = Jwts
//                .builder()
//                .claim("id", id.toString())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setNotBefore(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
//                .signWith(SignatureAlgorithm.HS256, "secret".getBytes())
//                .compact();
        return token;
    }
}
