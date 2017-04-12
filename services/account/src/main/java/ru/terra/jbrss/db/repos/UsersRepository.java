package ru.terra.jbrss.db.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.terra.jbrss.db.entity.JbrssUser;

public interface UsersRepository extends JpaRepository<JbrssUser, String> {
    JbrssUser findByLogin(String login);

    JbrssUser findByLoginAndPassword(String login, String password);
}
