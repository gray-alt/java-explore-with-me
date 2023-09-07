package ru.practicum.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Collection;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        if (userRepository.existsByName(user.getName())) {
            throw new ConflictException("User with name " + user.getName() + " is already exist");
        }
        user = userRepository.save(user);
        log.info("Added user " + user.getName() + " with id=" + user.getId());
        return user;
    }

    @Override
    public Collection<User> getUsersByIds(Collection<Long> userIds, int from, int size) {
        int page = from > 0 ? from / size : 0;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (userIds == null) {
            return userRepository.findAll(pageRequest).toList();
        }
        return userRepository.findByIdIn(userIds, pageRequest);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        log.info("Added user with id=" + userId);
    }

    @Override
    public User getUserById(Long userId) {
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));
        return foundUser;
    }
}
