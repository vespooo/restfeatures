package app.feature.controller;

import app.core.apiversion.ApiVersion;
import app.feature.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserControllerV3 {

    @Autowired
    private UserService userService;

    @GetMapping(headers = {"api-version=v3"})
    public List<String> getIds(){
        return userService.getUsers().stream()
                .map(u -> u.getId().toString())
                .collect(Collectors.toList());
    }
}
