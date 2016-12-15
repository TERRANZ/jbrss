package ru.terra.jbrss.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.core.db.repos.UsersRepository;

import java.util.Collections;

@Service
public class JBRssAuthProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UsersRepository usersRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("Starting auth procedure");
        String user = authentication.getName();
        if (authentication.getCredentials() == null)
            throw new UsernameNotFoundException("JbrssUser " + user + " provided no password");
        String password = authentication.getCredentials().toString();
        AuthUser authUser = (AuthUser) userDetailsService.loadUserByUsername(user);
        try {
            boolean passwordCorrect = authUser.getPassword().equals(password);
            if (passwordCorrect)
                return new UsernamePasswordAuthenticationToken(authUser, password, Collections.singletonList((GrantedAuthority) () -> "ADMIN"));
            else
                throw new UsernameNotFoundException("JbrssUser " + user + " not found");
        } catch (Exception e) {
            throw new UsernameNotFoundException("JbrssUser " + user + " password hash failure", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        logger.info("Request for support " + authentication.getCanonicalName() + " auth class");
        return true;
    }
}
