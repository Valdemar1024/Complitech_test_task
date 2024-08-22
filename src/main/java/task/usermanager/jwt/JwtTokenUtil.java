package task.usermanager.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class JwtTokenUtil {

    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration.minutes}")
    private Long EXPIRATION;

    public String generateToken(UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTCreator.Builder jwtBuilder = JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("user-manager-app")
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(EXPIRATION, ChronoUnit.MINUTES));

        return jwtBuilder.sign(algorithm);
    }

    public String getTokenFromHeader(String authHeader) {
        return authHeader.substring(JWT_TOKEN_PREFIX.length());
    }

    public DecodedJWT decodeJWT(String token) throws TokenExpiredException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String getUsername(String token) {
        return decodeJWT(token).getSubject();
    }
}
