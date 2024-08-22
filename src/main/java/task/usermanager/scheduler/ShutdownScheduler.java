package task.usermanager.scheduler;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ShutdownScheduler {

    @Value("${shutdown.datetime}")
    private String shutdownDatetime;

    private LocalDateTime shutdownTime;

    @PostConstruct
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.shutdownTime = LocalDateTime.parse(shutdownDatetime, formatter);
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void checkShutdownTime() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(shutdownTime)) {
            log.info("Application reached shutdown time: {}, now: {}", shutdownTime, now);
            System.exit(0);
        }
    }
}
