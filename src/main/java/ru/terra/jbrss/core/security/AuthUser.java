package ru.terra.jbrss.core.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import ru.terra.jbrss.core.db.entity.JbrssUser;

public class AuthUser extends User {
    private JbrssUser jbrssUser;

    public AuthUser(String userName, JbrssUser jbrssUser) {
        super(userName, jbrssUser.getPassword(), AuthorityUtils.createAuthorityList("ADMIN"));
        this.jbrssUser = jbrssUser;
    }

    public JbrssUser getJbrssUser() {
        return jbrssUser;
    }

    public Integer getId() {
        return jbrssUser.getId();
    }
}
