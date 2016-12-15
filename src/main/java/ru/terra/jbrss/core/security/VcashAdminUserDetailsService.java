package ru.terra.jbrss.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.core.db.entity.JbrssUser;
import ru.terra.jbrss.core.db.repos.UsersRepository;

@Service
public class VcashAdminUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public VcashAdminUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public AuthUser loadUserByUsername(String login) throws UsernameNotFoundException {
        JbrssUser user = usersRepository.findByLogin(login);
        if (user == null) {
            logger.info("JbrssUser " + login + " not found");
            throw new UsernameNotFoundException("JbrssUser " + login + " not found");
        }
        return new AuthUser(login, user);
    }
}