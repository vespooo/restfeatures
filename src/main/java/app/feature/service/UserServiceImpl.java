package app.feature.service;

import app.feature.domain.User;
import app.feature.repository.UserRepository;
import app.message.AppMessage;
import app.rest.exception.ErrorCode;
import app.rest.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppMessage message;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }

    @Override
    public User save(User user) {
        if(user.getId() != null && userRepository.existsById(user.getId())){
            throw new AppException(ErrorCode.CONFLICT);
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        Optional<User> oldUser = userRepository.findById(user.getId());
        if (oldUser.isPresent()){
            return userRepository.save(oldUser.get());
        }
        throw new AppException(ErrorCode.NOT_FOUND);
    }
}
