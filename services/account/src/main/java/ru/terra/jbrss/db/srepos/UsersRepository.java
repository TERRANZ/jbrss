package ru.terra.jbrss.db.srepos;

import org.springframework.data.repository.CrudRepository;
import ru.terra.jbrss.db.entity.JbrssUser;

public interface UsersRepository extends CrudRepository<JbrssUser, Integer> {
    JbrssUser findByLogin(String login);

    JbrssUser findByLoginAndPassword(String login, String password);
}
