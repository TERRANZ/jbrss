package ru.terra.jbrss.shared.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${authserver:http://localhost:1111/acc/}")
    String authServiceUrl;

    public List<String> getAllUserIds() {
        return restTemplate.getForObject(URI.create(authServiceUrl + "user/ids"), UserIdListDto.class).data.stream().map(UserIdDto::getId).collect(Collectors.toList());
    }

    public String getUserId(String name) {
        return restTemplate.getForObject(authServiceUrl + "user/{name}/id", UserIdDto.class, name).getId();
    }

    public String createUser(String login, String pass) {
        return restTemplate.getForObject(authServiceUrl + "user/create?login={login}&pass={pass}", UserIdDto.class, login, pass).getId();
    }

    public String login(String login, String pass) {
        return restTemplate.getForObject(authServiceUrl + "user/login?login={login}&pass={pass}", UserIdDto.class, login, pass).getId();
    }
}
