package com.rpo.backend.auth;

import com.rpo.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

// @Component позволяет получить доступ к экземпляру класса через контекст приложения
// То есть экземпляр класса AuthenticationProvider будет встроен в схему аутентификации spring boot security
@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserRepository userRepository;

    @Value("${private.session-timeout}") // в application.properties (private там не ключевое слово, а просто так :)
    private int sessionTimeout;

    //  метод родителя, реализация необходима, так как в базовом классе он абстрактный
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {

    }

    // метод родителя
    // нужен для извлечения информации о пользователе, в нашем случае из таблицы users в базе данных
    // ***
    // в этом коде встречаются два различных типа с одним именем User: наш в models и в import
    // поэтому один и них импортируется, а другой указывается с полным именем
    // ***
    // Провайдер получает имя пользователя и токен, извлекает из базы данных пользователя с заданными
    // логином и токеном, и возвращает информацию о пользователе в нужном формате. Если пользователь
    // не найден потому что его нет в базе или его токен отличается от заданного или отсутствует,
    // метод генерирует исключение. Следствие этого – отказ в авторизации.
    @Override
    protected UserDetails retrieveUser(String userName,
                                       UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        Object token = usernamePasswordAuthenticationToken.getCredentials();
        Optional<com.rpo.backend.models.User> uu = userRepository.findByToken(String.valueOf(token));
        if (!uu.isPresent())
            throw new UsernameNotFoundException("user_is_not_found");
        com.rpo.backend.models.User u = uu.get();
        // сравниваем время в поле activity с текущим и, если разница
        // между ними больше минуты, закрываем сессию удаляя токен из базы. Если нет, то надо
        // обновить в базе время последнего запроса пользователя
        boolean timeout = true;
        LocalDateTime dt  = LocalDateTime.now();
        if (u.activity != null)
        {
            LocalDateTime nt = u.activity.plusMinutes(sessionTimeout);
            if (dt.isBefore(nt))
                timeout = false;
        }
        if (timeout) {
            u.token = null;
            userRepository.save(u);
            throw new NonceExpiredException("session_is_expired");
        }
        else {
            u.activity = dt;
            userRepository.save(u);
        }

        return new User(u.login, u.password,
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList("USER"));
    }
}
