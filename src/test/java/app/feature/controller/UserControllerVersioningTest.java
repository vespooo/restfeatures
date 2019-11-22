package app.feature.controller;

import app.Application;
import app.core.apiversion.ApiVersionConfiguration;
import app.feature.domain.User;
import app.feature.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerVersioningTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void usersNoVersionShouldReturnListOfUsers() throws Exception {
        List<User> users = Arrays.asList(createUser(123L, "user1"));
        given(userService.getUsers()).willReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").hasJsonPath())
                .andExpect(jsonPath("$[0].name", is("user1")))
                .andReturn();
    }

    @Test
    public void usersV2ShouldReturnListOfNames() throws Exception {
        List<User> users = Arrays.asList(createUser(123L,"user1"));
        given(userService.getUsers()).willReturn(users);

        mvc.perform(get("/users").header("api-version", "v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").hasJsonPath())
                .andExpect(jsonPath("$[0]", is("user1")))
                .andReturn();
    }

    @Test
    public void usersV1AndV2ShouldReturnListOfNames() throws Exception {
        List<User> users = Arrays.asList(createUser(123L,"user1"));
        given(userService.getUsers()).willReturn(users);

        mvc.perform(get("/users").header("api-version", "v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").hasJsonPath())
                .andExpect(jsonPath("$[0]", is("user1")))
                .andReturn();
        mvc.perform(get("/users").header("api-version", "v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").hasJsonPath())
                .andExpect(jsonPath("$[0]", is("user1")))
                .andReturn();
    }

    @Test
    public void usersV3ShouldReturnListOfIds() throws Exception {
        List<User> users = Arrays.asList(createUser(123L,"user1"));
        given(userService.getUsers()).willReturn(users);

        mvc.perform(get("/users").header("api-version", "v3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").hasJsonPath())
                .andExpect(jsonPath("$[0]", is("123")))
                .andReturn();
    }

    private User createUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }
}
