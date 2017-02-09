package ru.terra.jbrss.shared.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.shared.dto.UserIdDto;
import ru.terra.jbrss.shared.dto.UserIdListDto;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {
    @Autowired
    OAuth2RestOperations restTemplate;

    public List<Integer> getAllUserIds() {
        return restTemplate.getForObject(URI.create("http://localhost:2222/acc/user/ids"), UserIdListDto.class).data.stream().map(UserIdDto::getId).collect(Collectors.toList());
    }

    public Integer getUserId(String name) {
        return restTemplate.getForObject("http://localhost:2222/acc/user/{name}/id", UserIdDto.class, name).getId();
    }

    public Integer createUser(String login, String pass) {
        return null;
    }

    public Integer login(String login, String pass) {
        return null;
    }
}
