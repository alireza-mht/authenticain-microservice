package io.github.alirezamht.authentication.service;

import io.github.alirezamht.authentication.model.User;

public interface UserService {

    User getUser(String StdNo);

    void save(User user);
}
