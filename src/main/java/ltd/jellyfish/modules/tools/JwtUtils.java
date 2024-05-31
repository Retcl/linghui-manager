package ltd.jellyfish.modules.tools;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

@Component
public class JwtUtils {

    @Value("${token.expire}")
    private long expire;

    @Value("${token.secret}")
    private String secret;

    private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS512;


    public String buildToken(String username) {
        Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);
        return Jwts.builder().header()
            .add("typ", "jwt")
            .add("alg", "HS512")
            .and()
            .claim("username", username)
            .id(UUID.randomUUID().toString())
            .expiration(expireDate)
            .issuedAt(new Date(System.currentTimeMillis()))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), ALGORITHM).compact();
    }

}
