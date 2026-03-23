package com.inventory.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private Long expiration;
    private Key key(){ return Keys.hmacShaKeyFor(secret.getBytes()); }
    public String generateToken(UserDetails ud){
        return Jwts.builder().setSubject(ud.getUsername()).setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis()+expiration))
            .signWith(key(),SignatureAlgorithm.HS256).compact();
    }
    public String extractUsername(String token){ return extractClaim(token,Claims::getSubject); }
    public boolean validateToken(String token, UserDetails ud){
        try{ return extractUsername(token).equals(ud.getUsername()) && !extractClaim(token,Claims::getExpiration).before(new Date()); }
        catch(Exception e){ return false; }
    }
    private <T> T extractClaim(String token, Function<Claims,T> fn){
        return fn.apply(Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody());
    }
}
