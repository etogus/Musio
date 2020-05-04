package com.rpo.backend.controllers;

import com.rpo.backend.models.Shop;
import com.rpo.backend.models.User;
import com.rpo.backend.repositories.ShopRepository;
import com.rpo.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ShopRepository shopRepository;

    // GET запрос, метод возвращает список пользователей, который будет автоматически преобразован в JSON.
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // POST запрос, сохраняем в таблице users нового пользователя.
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если такое имя пользователя уже существует.
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        try {
            User nc = userRepository.save(user);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            String error;
            if (ex.getMessage().contains("users.name_UNIQUE"))
                error = "user_already_exists";
            else
                error = "undefined_error";
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }

    // POST запрос, метод добавляет список магазинов пользователю
    @PostMapping("/users/{id}/addshops")
    public ResponseEntity<Object> addShops(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody Set<Shop> shops) {
        Optional<User> uu = userRepository.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            User u = uu.get();
            for (Shop m : shops) {
                // извлекаем каждый магазин из базы прежде чем его
                // добавить к списку, т.к. в параметрах запроса передаются только ключи магазинов
                Optional<Shop> mm = shopRepository.findById(m.id);
                if (mm.isPresent()) {
                    u.addShop(mm.get());
                    cnt++;
                }
            }
            userRepository.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    // POST запрос, метод удаляет список магазинов пользователя
    @PostMapping("/users/{id}/removeshops")
    public ResponseEntity<Object> removeShops(@PathVariable(value = "id") Long userId,
                                              @Valid @RequestBody Set<Shop> shops) {
        Optional<User> uu = userRepository.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            User u = uu.get();
            for (Shop m : u.shops) {
                u.removeShop(m);
                cnt++;
            }
            userRepository.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    // PUT запрос, обновляет запись в таблице users.
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если пользователь с указанным ключом отсутствует.
    // Копируются только два поля. С остальными, разберемся потом. //TODO
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody User userDetails) {
        User user = null;
        Optional<User> uu = userRepository.findById(userId);
        if (uu.isPresent())
        {
            user = uu.get();
            user.login = userDetails.login;
            user.email = userDetails.email;
            userRepository.save(user);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user_not_found"
            );
        }
        return ResponseEntity.ok(user);
    }

    // DELETE запрос, метод удаления записи из таблицы users
    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Map<String, Boolean> response = new HashMap<>();
        if (user.isPresent())
        {
            userRepository.delete(user.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }
}

