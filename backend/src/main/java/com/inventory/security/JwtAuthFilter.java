package com.inventory.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserDetailsService uds;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        String token = null, username = null;
        if(header!=null && header.startsWith("Bearer ")){ token=header.substring(7); try{ username=jwtUtil.extractUsername(token); }catch(Exception ignored){} }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            try{
                UserDetails ud = uds.loadUserByUsername(username);
                if(jwtUtil.validateToken(token,ud)){
                    var auth = new UsernamePasswordAuthenticationToken(ud,null,ud.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }catch(Exception ignored){}
        }
        chain.doFilter(req,res);
    }
}
