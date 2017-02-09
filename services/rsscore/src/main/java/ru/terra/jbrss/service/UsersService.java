package ru.terra.jbrss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import ru.terra.jbrss.dto.UserIdListDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {
    @Autowired
    OAuth2RestOperations restTemplate;

    public List<Integer> getAllUserIds() {
        UserIdListDto dto = restTemplate.getForObject(URI.create("http://localhost:2222/acc/ids"), UserIdListDto.class);
        return new ArrayList<>();
    }
}
