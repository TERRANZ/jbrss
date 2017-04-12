package ru.terra.jbrss.shared.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.terra.jbrss.shared.dto.UserIdDto;
import ru.terra.jbrss.shared.dto.UserIdListDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {
    @Autowired
    OAuth2RestOperations oAuth2RestOperations;

    @Value("${authserver:http://localhost:2222/acc/}")
    String authServiceUrl;

    public String authenticate(String login, String pass) {
        try {
            RestTemplate rt = new RestTemplate();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "client_credentials");
            map.add("scope", "read");
            map.add("client_id", login);
            map.add("client_secret", pass);
            rt.setMessageConverters(Arrays.asList(new FormHttpMessageConverter(), new MappingJackson2HttpMessageConverter()));
            HashMap<String, String> authDto = rt.postForObject(authServiceUrl + "oauth/token", map, HashMap.class);
            if (authDto != null) {
                return authDto.get("token_type") + " " + authDto.get("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<String> getAllUserIds() {
        UserIdListDto ret = oAuth2RestOperations.getForObject(URI.create(authServiceUrl + "user/ids"), UserIdListDto.class);
        if (ret != null) {
            if (ret.data != null) {
                return ret.data.stream().map(UserIdDto::getId).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    public String createUser(String login, String pass) {
        return oAuth2RestOperations.getForObject(authServiceUrl + "user/create?login={login}&pass={pass}", UserIdDto.class, login, pass).getId();
    }

    public String login(String login, String pass) {
        return oAuth2RestOperations.getForObject(authServiceUrl + "user/login?login={login}&pass={pass}", UserIdDto.class, login, pass).getId();
    }
}
