package app.feature.controller;

import app.feature.service.UserService;
import app.rest.apiversion.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserControllerV2 {

    @Autowired
    private UserService userService;

    @GetMapping
    @ApiVersion(versions = {"v2", "v1"})
    public List<String> getNames(){
        return userService.getUsers().stream()
                .map(u -> u.getName())
                .collect(Collectors.toList());
    }
}
