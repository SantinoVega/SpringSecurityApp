package com.app.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${spring.security.jwt.secret}")
    private String privateKey;
    @Value("${spring.security.jwt.userGenerator}")
    private String userGenerator;

    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer(userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
        return jwtToken;
    }

    public DecodedJWT validateToken(String jwtToken){
        try{
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                   .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
            return decodedJWT;
        }catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token invalido ");
        }
    }

     public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getClaim(DecodedJWT decodedJWT, String nameClaim) {
        return decodedJWT.getClaim(nameClaim);
    }

    public Map<String,Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
