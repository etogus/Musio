package com.rpo.backend.controllers;

import com.rpo.backend.models.User;
import com.rpo.backend.repositories.UserRepository;
import com.rpo.backend.tools.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

// Ответ серверу 3000 явно разрешаем в настройках нашего сервера 8081.
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("auth")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // Логин и пароль передаются в теле POST запроса. Далее извлекаем пользователя из базы. Если его там нет,
    // возвращаем код 401 UNAUTHORIZED. Если есть, из поля password извлекаем хеш (записанный туда при смене
    // пароля пользователя). Затем вычисляем хеш присланного пароля и salt из записи пользователя и сравниваем
    // с копией хеша в записи пользователя. Если не совпадают, отказываем в авторизации. Если совпадают, надо
    // сделать токен, который будет в дальнейшем использоваться для аутентификации запросов этого пользователя.
    // Токен - это случайная последовательность символов, которую мы получаем с помощью функции randomUUID.
    // Токен сохраняется в записи пользователя в базе данных, а также передается обратно клиенту, который
    // теперь должен вставлять его в HTTP заголовок Authorization каждый раз при обращении к REST сервису.
    // Этот токен проверяется в процедуре attemptAuthentication нашего фильтра запросов.
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String pwd = credentials.get("password");
        if (!pwd.isEmpty() && !login.isEmpty()) {
            Optional<User> uu = userRepository.findByLogin(login);
            if (uu.isPresent()) {
                User u2 = uu.get();
                String hash1 = u2.password;
                String salt = u2.salt;
                String hash2 = Utils.ComputeHash(pwd, salt);
                if (hash1.equals(hash2)) {
                    u2.token = UUID.randomUUID().toString();
                    u2.activity = LocalDateTime.now(); // хранится время, в которое мы последний раз видели пользователя
                    User u3 = userRepository.saveAndFlush(u2); // LocalDateTime.now() возвращает текущее время
                    return new ResponseEntity<Object>(u3, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String pwd = credentials.get("password");
        if (!pwd.isEmpty() && !login.isEmpty()) {
            User u2 = new User();
            u2.salt = String.valueOf(ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1));
            String salt = u2.salt;
            String hash2 = Utils.ComputeHash(pwd, salt);
            String token = UUID.randomUUID().toString();
            u2.login = login;
            u2.token = token;
            u2.password = hash2;
            User u3 = userRepository.saveAndFlush(u2);
            return new ResponseEntity<Object>(u3, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && !token.isEmpty()) {
            token = StringUtils.removeStart(token, "Bearer").trim();
            Optional<User> uu = userRepository.findByToken(token);
            if (uu.isPresent()) {
                User u = uu.get();
                u.token = null;
                userRepository.save(u);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
