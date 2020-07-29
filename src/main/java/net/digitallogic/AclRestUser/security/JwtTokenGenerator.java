package net.digitallogic.AclRestUser.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.digitallogic.AclRestUser.persistence.entity.UserEntity;

import java.util.Date;

public class JwtTokenGenerator {

    private final String tokenSecret;
    private final String iss;
    private final long expiration;

    public JwtTokenGenerator(String tokenSecret, long expiration, String iss) {
        this.tokenSecret = tokenSecret;
        this.expiration = expiration;
        this.iss = iss;
    }

    public long getExpiration() {
        return expiration;
    }

    public String allocateToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuer(iss)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public String verifyToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
