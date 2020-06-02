package com.rpo.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Отношение между таблицами artists и shops многие-ко-многим, но так как между
// этими таблицами находится таблица songs у которой есть собственные поля, в
// дополнение к тем ключевым полям, которые связывают таблицы artists и shops, то мы
// не можем воспользоваться аннотацией @ManyToMany, для упрощения работы с этим типом связи.
// Добавим к проекту таблицу пользователей REST сервиса users.
// Пользователи будут иметь права на изменение коллекций магазинов. У каждого пользователя будет свой
// список магазинов, коллекции которых им разрешено редактировать. Тогда между магазинами и пользователями появится
// отношение многие-комногим и между этими таблицами придется сделать связывающую их служебную таблицу usersshops.

@Entity // указывает, что класс - это таблица в базе
@Table(name = "users") // указывается имя таблицы
@Access(AccessType.FIELD) //указывает на то, что мы разрешаем доступ к полям класса
public class User {     //вместо того, чтобы для каждого поля делать методы чтения и записи

    public User() { }
    public User(Long id) {
        this.id = id;
    }

    @Id //Каждое поле класса связывается с полем в таблице а для поля ключа, дополнительно, указываются его свойства.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "login", nullable = false, unique = true)
    public String login;

    @Column(name = "email", nullable = false)
    public String email;

    //  password и salt отмечены аннотацией @JsonIgnore. Их
    //  очевидно не надо светить в JSON, за пределами REST сервиса.
    @JsonIgnore
    @Column(name = "password")
    public String password;

    @JsonIgnore
    @Column(name = "salt")
    public String salt;

    // Не будет связано с полем таблицы базы данных
    @Transient
    public String np;

    @Column(name = "token")
    public String token;

    @Column(name = "activity")
    public LocalDateTime activity;

    @ManyToMany(mappedBy ="users")
    public Set<Shop> shops = new HashSet<>();

    // addShop и removeShop - вспомогательные методы для addShops и removeShops в UserController
    // Решает проблему того, что для отношений многие-ко-многим изменения должны
    // быть сделаны сразу с двух сторон отношения.
    public void addShop(Shop m) {
        this.shops.add(m);
        m.users.add(this);
    }

    public void removeShop(Shop m) {
        this.shops.remove(m);
        m.users.remove(this);
    }

}
