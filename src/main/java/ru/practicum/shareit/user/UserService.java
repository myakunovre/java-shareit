package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmailException;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserDto userDto) {
        emailExistingValidate(userDto);
        User user = UserMapper.toUser(userDto);
        return userRepository.save(user);
    }

    private void emailExistingValidate(UserDto userDto) {
        Optional<User> alreadyExistUser = userRepository.findByEmail(userDto.getEmail());
        if (alreadyExistUser.isPresent()) {
            log.info("User with email {} is already exists.", userDto.getEmail());
            throw new DuplicateEmailException("User with email" + userDto.getEmail() + " is already exists.");
        }
    }

    public Optional<User> getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return Optional.of(user);
    }

    public Collection<UserDto> getAllUsers() {
        Collection<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public User updateUser(UserDto userDto, long userId) {
        emailExistingValidate(userDto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}