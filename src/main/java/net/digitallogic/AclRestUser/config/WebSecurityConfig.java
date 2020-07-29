package net.digitallogic.AclRestUser.config;

import net.digitallogic.AclRestUser.mapper.UserMapper;
import net.digitallogic.AclRestUser.persistence.repository.UserRepository;
import net.digitallogic.AclRestUser.security.AccessToken;
import net.digitallogic.AclRestUser.security.AuthTokenProcessingFilter;
import net.digitallogic.AclRestUser.security.JwtTokenGenerator;
import net.digitallogic.AclRestUser.security.UserLoginFilter;
import net.digitallogic.AclRestUser.web.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String accessTokenSecret;
    private final long accessTokenExpires;
    private final String iss;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;



    @Autowired
    public WebSecurityConfig(@Value("${token.access.secret}") String accessTokenSecret,
                             @Value("${token.access.expires}") long accessTokenExpires,
                             @Value("${token.iss}") String iss,
                             UserRepository userRepository,
                             UserMapper userMapper,
                             AuthenticationManagerBuilder authenticationManagerBuilder,
                             Environment environment,
                             UserDetailsService userDetailsService) throws Exception {

        this.accessTokenSecret = accessTokenSecret;
        this.accessTokenExpires = accessTokenExpires;
        this.iss = iss;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;

        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                    .disable()
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .addFilterAt(new UserLoginFilter(authenticationManager(), generateAccessToken(), userMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new AuthTokenProcessingFilter(generateAccessToken(), userRepository), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, Routes.LOGIN_ROUTE, Routes.SIGN_UP_ROUTE)
                .permitAll()

                .anyRequest()
                .authenticated()
        ;
    }


    @Bean
    @AccessToken
    public JwtTokenGenerator generateAccessToken() {
        return new JwtTokenGenerator(accessTokenSecret, accessTokenExpires, iss);
    }

}
