package com.rpo.backend.models;

import javax.persistence.*;

@Entity // указывает, что класс - это таблица в базе
@Table(name = "artists") // указывается имя таблицы
@Access(AccessType.FIELD) //указывает на то, что мы разрешаем доступ к полям класса
public class Artist {     //вместо того, чтобы для каждого поля делать методы чтения и записи

    public Artist() {}
    public Artist(Long id) {
        this.id = id;
    }

    @Id //Каждое поле класса связывается с полем в таблице а для поля ключа, дополнительно, указываются его свойства.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    @Column(name = "century", nullable = false)
    public String century;

    // Отношение в модели Artist необходимо для того чтобы можно было задать, изменить или получить страну артиста
    @ManyToOne // - многие к одному, которая задает зависимость между таблицами artists и countries
    @JoinColumn(name = "countryid")
    public Country country;

}
