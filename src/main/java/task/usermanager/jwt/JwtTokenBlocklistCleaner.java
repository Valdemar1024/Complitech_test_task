package task.usermanager.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenBlocklistCleaner {

    private final JwtTokenValidator jwtTokenValidator;

    @Scheduled(cron = "0 */5 * * * *")
    public void triggerTokensCleanUp() {
        jwtTokenValidator.removeExpiredBlockedTokens();
    }
}
