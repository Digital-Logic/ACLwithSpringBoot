package net.digitallogic.AclRestUser.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.digitallogic.AclRestUser.mapper.UserMapper;
import net.digitallogic.AclRestUser.persistence.entity.UserEntity;
import net.digitallogic.AclRestUser.web.Routes;
import net.digitallogic.AclRestUser.web.request.LoginRequest;
import net.digitallogic.AclRestUser.web.response.UserResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class UserLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator tokenGenerator;
    private final UserMapper userMapper;

    public UserLoginFilter(AuthenticationManager authenticationManager,
                           JwtTokenGenerator tokenGenerator,
                           UserMapper userMapper) {

        super(new AntPathRequestMatcher(Routes.LOGIN_ROUTE, HttpMethod.POST.name()));

        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
        this.userMapper = userMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("Authentication Attempt");
        try {
            LoginRequest loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("Authentication Success!");

        UserEntity user = (UserEntity) authResult.getPrincipal();

        Cookie cookie = new Cookie(SecurityConstants.ACCESS_TOKEN, tokenGenerator.allocateToken(user));
        cookie.setMaxAge( (int) tokenGenerator.getExpiration() / 1000);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        UserResponse userResponse = userMapper.toResponse(
            userMapper.toDto(user)
        );

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), userResponse);
    }
}
