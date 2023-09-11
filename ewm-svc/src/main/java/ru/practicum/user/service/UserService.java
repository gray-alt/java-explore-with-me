package ru.practicum.user.service;

import ru.practicum.user.model.User;

import java.util.Collection;

public interface UserService {
    User addUser(User user);

    Collection<User> getUsersByIds(Collection<Long> userIds, int from, int size);

    void deleteUserById(Long userId);

    User getUserById(Long userId);
}
