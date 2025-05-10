package com.example.polling.service;

import com.example.polling.exception.AlreadyExistsException;
import com.example.polling.exception.ResourceNotFoundException;
import com.example.polling.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    // Хранилище пользователей: key = username, value = User
    private final Map<String, User> usersDb = new ConcurrentHashMap<>();

    public UserService() {
        // Инициализация тестовыми данными
        usersDb.put("Тайлер Дёрден", new User("Тайлер Дёрден", "password1", "21.02.2012"));
        usersDb.put("Кошка с третьего этажа", new User("Кошка с третьего этажа", "password2", "10.01.2024"));
        usersDb.put("Павел Дуров", new User("Павел Дуров", "password3", "01.01.2025"));
    }

    public User register(User user) {
        if (usersDb.containsKey(user.getUsername())) {
            throw new AlreadyExistsException("Пользователь с таким именем уже существует");
        }
        // Формат текущей даты dd.MM.yyyy
        String registrationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        user.setRegistrationDate(registrationDate);
        usersDb.put(user.getUsername(), user);
        return user;
    }

    public User login(String username, String password) {
        User user = usersDb.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new ResourceNotFoundException("Неверное имя пользователя или пароль");
        }
        return user;
    }

    public Map<String, User> getUsersDb() {
        return usersDb;
    }
}
