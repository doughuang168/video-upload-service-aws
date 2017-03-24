package com.doughuang168.video.service.user;

import com.doughuang168.video.domain.UserCreateForm;
import com.doughuang168.video.domain.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);

}
