package ru.terra.jbrss.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Vadim_Korostelev on 4/13/2017.
 */
@Service
public class OAuthDataService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void addOAuthUser(String login, String pass) {
        String sql = "INSERT INTO `oauth_client_details` (`client_id`, `client_secret`, `scope`, `authorized_grant_types`) VALUES (? , ? , 'read', 'client_credentials');";
        jdbcTemplate.update(sql,
                preparedStatement -> {
                    preparedStatement.setString(1, login);
                    preparedStatement.setString(2, pass);
                }
        );
    }
}
