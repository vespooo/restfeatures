package app.feature.service;

import app.feature.domain.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUser(Long id);

    User save(User user);

    User update(User user);
}
