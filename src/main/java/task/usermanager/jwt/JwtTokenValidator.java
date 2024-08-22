package task.usermanager.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final Set<String> blockedTokens = ConcurrentHashMap.newKeySet();

    private final JwtTokenUtil jwtTokenUtil;

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            return jwtTokenUtil.getUsername(token).equals(userDetails.getUsername())
                    && !isTokenExpired(token)
                    && !blockedTokens.contains(token);
        } catch (TokenExpiredException ex) {
            return false;
        }
    }

    public void blockToken(String token) {
        blockedTokens.add(token);
    }

    void removeExpiredBlockedTokens() {
        blockedTokens.removeIf(this::isTokenExpired);
    }

    private boolean isTokenExpired(String token) {
        try {
            return jwtTokenUtil.decodeJWT(token).getExpiresAtAsInstant().isBefore(Instant.now());
        } catch (TokenExpiredException ex) {
            return true;
        }
    }
}
