package com.rpo.backend.repositories;

import com.rpo.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// UserRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы users. Надо лишь указать тип User в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String token); // для аутентификации пользователя, при обращении к ресурсу сервиса
    Optional<User> findByLogin(String login); // для поиска пользователя при входе
}
