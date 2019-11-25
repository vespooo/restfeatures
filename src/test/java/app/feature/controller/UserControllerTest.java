package app.feature.controller;

import app.Application;
import app.feature.domain.User;
import app.feature.service.UserService;
import app.rest.exception.AppException;
import app.rest.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
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
        List<User> users = Arrays.asList(createUser(123L, "user1"));
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
        User user = createUser(123L, "user1");
        given(userService.getUser(123L)).willReturn(user);

        mvc.perform(get("/users/123"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.name", is("user1")))
                .andReturn();
    }

    @Test
    public void getUserByNonExistingIdShouldReturnNotFound() throws Exception {

        AppException ex = new AppException(ErrorCode.NOT_FOUND);
        given(userService.getUser(123L)).willThrow(ex);

        mvc.perform(get("/users/123"))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.NOT_FOUND.getMessage())))
                .andReturn();
    }

    @Test
    public void getUserByNonNumericalIdShouldReturnBadRequest() throws Exception {

        mvc.perform(get("/users/123a"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.WRONG_PARAMETER_TYPE.getMessage())))
                .andReturn();
    }

    @Test
    public void saveShouldReturnBadRequestIfNoRequestBody() throws Exception {

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.MISSING_REQUEST_BODY.getMessage())))
                .andReturn();
    }

    @Test
    public void saveShouldReturnBadRequestIfJsonIsEmpty() throws Exception {

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_REQUEST_BODY.getMessage())))
                .andReturn();
    }

    @Test
    public void saveWithExistedIdShouldReturnConflict() throws Exception {

        User user = createUser(123L, "user1");
        AppException ex = new AppException(ErrorCode.CONFLICT);
        given(userService.save(ArgumentMatchers.any(User.class))).willThrow(ex);

        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.CONFLICT.getMessage())))
                .andReturn();
    }

    @Test
    public void saveWithNotExistedIdShouldReturnCreated() throws Exception {

        User user = createUser(123L, "user1");
        given(userService.save(ArgumentMatchers.any(User.class))).willReturn(user);

        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.name", is("user1")))
                .andReturn();
    }

    @Test
    public void saveWithNullIdShouldReturnCreated() throws Exception {

        User user = createUser(null, "user1");
        given(userService.save(ArgumentMatchers.any(User.class))).willReturn(user);

        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.name", is("user1")))
                .andReturn();
    }


    @Test
    public void updateShouldReturnBadRequestIfNoRequestBody() throws Exception {

        mvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.MISSING_REQUEST_BODY.getMessage())))
                .andReturn();
    }

    @Test
    public void updateShouldReturnBadRequestIfJsonIsEmpty() throws Exception {

        mvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_REQUEST_BODY.getMessage())))
                .andReturn();
    }

    @Test
    public void updateWithNullIdShouldReturnBadRequest() throws Exception {

        User user = createUser(null, "user1");

        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.INVALID_REQUEST_BODY.getMessage())))
                .andReturn();
    }

    @Test
    public void updateShouldReturnNotFoundIfUserIsNew() throws Exception {

        User user = createUser(123L, "user1");
        AppException ex = new AppException(ErrorCode.NOT_FOUND);
        given(userService.update(ArgumentMatchers.any(User.class))).willThrow(ex);

        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.message", is(ErrorCode.NOT_FOUND.getMessage())))
                .andReturn();
    }

    @Test
    public void updateShouldReturnOk() throws Exception {

        User user = createUser(123L, "user1");
        given(userService.update(ArgumentMatchers.any(User.class))).willReturn(user);

        String userJson = objectMapper.writeValueAsString(user);

        mvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$.name", is("user1")))
                .andReturn();
    }

    private User createUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }
}
