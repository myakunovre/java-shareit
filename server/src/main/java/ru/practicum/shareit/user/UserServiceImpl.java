package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmailException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        emailExistingValidate(userDto);
        User userToSave = UserMapper.toUser(userDto);
        User savedUser = userRepository.save(userToSave);
        return UserMapper.toUserDto(savedUser);
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        Collection<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto updateUser(UserDto userDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            emailExistingValidate(userDto);
            user.setEmail(userDto.getEmail());
        }

        User savedUser = userRepository.save(user);

        return UserMapper.toUserDto(savedUser);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private void emailExistingValidate(UserDto userDto) {
        Optional<User> alreadyExistUser = userRepository.findByEmail(userDto.getEmail());
        if (alreadyExistUser.isPresent()) {
            log.info("User with email {} is already exists.", userDto.getEmail());
            throw new DuplicateEmailException("User with email" + userDto.getEmail() + " is already exists.");
        }
    }
}