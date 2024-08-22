package task.usermanager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.usermanager.dto.AuthRequestDTO;
import task.usermanager.dto.AuthResponseDTO;
import task.usermanager.jwt.JwtTokenUtil;
import task.usermanager.jwt.JwtTokenValidator;
import task.usermanager.service.UserDetailsServiceImpl;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtTokenValidator jwtTokenValidator;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Validated @RequestBody AuthRequestDTO authRequestDTO) {
        log.info("Login request received for user: {}", authRequestDTO.getLogin());
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getLogin(), authRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDTO.getLogin());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(token)
                .login(authRequestDTO.getLogin())
                .build());
    }

    @PostMapping("/logout")
    public void logout(HttpRequest request) {
        Optional<String> optionalAuthHeader = Optional.ofNullable(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        optionalAuthHeader.ifPresent(authHeader -> {
            String token = jwtTokenUtil.getTokenFromHeader(authHeader);
            jwtTokenValidator.blockToken(token);
            SecurityContextHolder.clearContext();
        });
    }
}
