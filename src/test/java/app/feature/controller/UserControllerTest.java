package app.feature.controller;

import app.Application;
import app.feature.domain.User;
import app.feature.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getUsersShouldReturnArrayOfUsers() throws Exception {
        List<User> users = Arrays.asList(createUser("user1"));
        given(userService.getUsers()).willReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").hasJsonPath())
                .andExpect(jsonPath("$[0].name", is("user1")))
                .andReturn();
    }

    @Test
    public void getUsersShouldReturnEmptyArrayIfNoUsers() throws Exception {
        List<User> users = new ArrayList<>();
        given(userService.getUsers()).willReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }

    @Test
    public void getUserByExistingIdShouldReturnUser() throws Exception {
        User user = createUser("user1");
        given(userService.getUser(123L)).willReturn(Optional.of(user));

        mvc.perform(get("/users/123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.name", is("user1")))
                .andReturn();
    }

    @Test
    public void getUserByNonExistingIdShouldReturnNotFound() throws Exception {

        given(userService.getUser(123L)).willReturn(Optional.empty());

        mvc.perform(get("/users/123"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getUserByNonNumericalIdShouldReturnNotFound() throws Exception {

        mvc.perform(get("/users/123a"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    private User createUser(String name) {
        User user = new User();
        user.setName(name);
        return user;
    }
}
