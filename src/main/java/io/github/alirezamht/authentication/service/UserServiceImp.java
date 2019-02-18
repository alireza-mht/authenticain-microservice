package io.github.alirezamht.authentication.service;

import io.github.alirezamht.authentication.model.User;
import io.github.alirezamht.authentication.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("userService")
@Transactional
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String userName) {
        List<User> users = userRepository.findUserByUsername(userName);
                //getAllBy();
                //getUserByUsername(userName);
                //userRepository.findUserByUsername(userName);

        return users ==null || users.size()==0 ? null: users.get(0);
    }

    public void save(User user){
        this.userRepository.save(user);
    }
}
