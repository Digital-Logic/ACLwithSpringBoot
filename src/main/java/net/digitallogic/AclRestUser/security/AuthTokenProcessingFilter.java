package net.digitallogic.AclRestUser.security;

import net.digitallogic.AclRestUser.persistence.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenProcessingFilter extends OncePerRequestFilter {

    private final JwtTokenGenerator tokenGenerator;
    private final UserRepository userRepository;

    public AuthTokenProcessingFilter(JwtTokenGenerator tokenGenerator, UserRepository userRepository) {
        this.tokenGenerator = tokenGenerator;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie cookie = WebUtils.getCookie(request, SecurityConstants.ACCESS_TOKEN);

        if (cookie != null) {

            long userId = Long.parseLong(tokenGenerator.verifyToken(cookie.getValue()));

            // Setup SecurityContextHolder with this users authentication information
            userRepository.findByIdWithRolesAndAuthorities(userId).ifPresent(userEntity -> {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userEntity, cookie.getValue(), userEntity.getAuthorities())
                );
            });
        }

        filterChain.doFilter(request, response);
    }
}
