package task.usermanager.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import task.usermanager.dto.GenderDTO;
import task.usermanager.dto.UserDTO;
import task.usermanager.service.UserDetailsServiceImpl;
import task.usermanager.service.UserService;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultUserCreator implements ApplicationRunner {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    @Value("${create.default.user}")
    private Boolean CREATE_DEFAULT_USER;
    @Value("${default.login}")
    private String DEFAULT_LOGIN;
    @Value("${default.password}")
    private String DEFAULT_PASSWORD;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (CREATE_DEFAULT_USER) {
            try {
                userDetailsService.loadUserByUsername(DEFAULT_LOGIN);
                log.info("Default user '{}' already exists", DEFAULT_LOGIN);
            } catch (UsernameNotFoundException e) {
                userService.create(UserDTO.builder()
                        .login(DEFAULT_LOGIN)
                        .password(DEFAULT_PASSWORD)
                        .fullName("Default user")
                        .gender(GenderDTO.builder()
                                .id(3)
                                .build())
                        .build());
                log.info("Default user '{}' is created", DEFAULT_LOGIN);
            }
        }
    }
}
