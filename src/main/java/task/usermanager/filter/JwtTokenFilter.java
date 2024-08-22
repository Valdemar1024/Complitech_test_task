package task.usermanager.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import task.usermanager.jwt.JwtTokenUtil;
import task.usermanager.jwt.JwtTokenValidator;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtTokenValidator jwtTokenValidator;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional<String> optionalAuthHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));

        optionalAuthHeader.ifPresent(authHeader -> {
            String jwtToken = jwtTokenUtil.getTokenFromHeader(authHeader);
            Optional<String> optionalUsername = Optional.ofNullable(jwtTokenUtil.getUsername(jwtToken));

            if (optionalUsername.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Security context was null, so authorizing user");
                log.info("User details request received for user: {}", optionalUsername);
                UserDetails userDetails = userDetailsService.loadUserByUsername(optionalUsername.get());

                if (jwtTokenValidator.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        });

        filterChain.doFilter(request, response);
    }
}
