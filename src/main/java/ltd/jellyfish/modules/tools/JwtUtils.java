package ltd.jellyfish.modules.tools;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
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

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token);
    }

    public String getTokenUUID(String token) {
        Claims claims = getClaims(token).getPayload();
        return claims.getId();
    }

    public String getToeknUsername(String token) {
        Claims claims = getClaims(token).getPayload();
        return claims.get("username").toString();
    }

    public Date getIssueTime(String token) {
        Claims claims = getClaims(token).getPayload();
        return claims.getIssuedAt();
    }

    public boolean isTokenExpire(String token) {
        return getClaims(token).getPayload().getExpiration().after(new Date());
    }
}
