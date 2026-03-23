package com.inventory.security;
import com.inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found: "+username));
        return new User(u.getUsername(), u.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole().name())));
    }
}
