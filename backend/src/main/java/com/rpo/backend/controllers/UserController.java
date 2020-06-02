package com.rpo.backend.controllers;

import com.rpo.backend.models.Shop;
import com.rpo.backend.models.User;
import com.rpo.backend.repositories.ShopRepository;
import com.rpo.backend.repositories.UserRepository;
import com.rpo.backend.tools.DataValidationException;
import com.rpo.backend.tools.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ShopRepository shopRepository;

    // GET запрос, метод возвращает список пользователей, который будет автоматически преобразован в JSON.
    @GetMapping("/users")
    public Page<User> getAllUsers(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return userRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "login")));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") Long userId)
            throws DataValidationException
    {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new DataValidationException("Пользователь с таким индексом не найдена"));
        return ResponseEntity.ok(user);
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
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                              @Valid @RequestBody User userDetails)
            throws DataValidationException
    {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new DataValidationException(" Пользователь с таким индексом не найден"));
            user.email = userDetails.email;
            String np = userDetails.np;
            if (np != null  && !np.isEmpty()) {
                byte[] b = new byte[32];
                new Random().nextBytes(b);
                String salt = new String(Hex.encode(b));
                user.password = Utils.ComputeHash(np, salt);
                user.salt = salt;
            }
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("users.email_UNIQUE"))
                throw new DataValidationException("Пользователь с такой почтой уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    // Метод удаления записи из таблицы users
    @PostMapping("/deleteusers")
    public ResponseEntity deleteUsers(@Valid @RequestBody List<User> users) {
        userRepository.deleteAll(users);
        return new ResponseEntity(HttpStatus.OK);
    }
}

