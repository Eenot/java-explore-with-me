package ru.practicum.ewm.main.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.exception.ConflictDataException;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.mapper.UserMapper;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.mapper.UserMapper.toUser;
import static ru.practicum.ewm.main.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IncorrectDataException("Field: email. Error: не должно быть пустым. Value: null");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IncorrectDataException("Field: name. Error: не должно быть пустым. Value: null");
        }
        if (user.getName().length() < 2 || user.getName().length() > 250) {
            throw new IncorrectDataException("Field: name. Error: должно быть > 2 && < 250. Value: " + user.getName().length());
        }
        if (user.getEmail().length() < 6 || user.getEmail().length() > 254) {
            throw new IncorrectDataException("Field: email. Error: должно быть > 6 && < 254. Value: " + user.getEmail().length());
        }

        if (userRepository.findUserByName(user.getName()).isPresent()) {
            throw new ConflictDataException("Field: name. Error: имя должно быть уникальным");
        }
        User userFromDb = userRepository.save(toUser(user));
        return toUserDto(userFromDb);
    }

    @Override
    public UserDto deleteUser(long userId) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataException("Пользователь с id = " + userId + " не найден"));
        userRepository.delete(userFromDb);
        return toUserDto(userFromDb);
    }

    @Override
    public List<UserDto> getUsers(long[] ids, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<User> result;
        if (ids == null) {
            result = userRepository.getAllUsersWithPageable(page);
        } else {
            result = userRepository.getUsersByIdsWithPageable(ids, page);
        }
        return result.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
