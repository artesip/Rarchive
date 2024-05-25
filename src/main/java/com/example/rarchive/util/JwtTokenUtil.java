package com.example.rarchive.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private  String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateToken(UserDetails userDetails) {

        Date currentTime = new Date();
        Date tokenDeath = new Date(currentTime.getTime() + lifetime.toMillis());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(currentTime)
                .setExpiration(tokenDeath)
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public String getUsername(String token){
        return getAllClaims(token).getSubject();
    }

    private Claims getAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}