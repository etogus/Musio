package com.rpo.backend.models;

import javax.persistence.*;

@Entity // указывает, что класс - это таблица в базе
@Table(name = "songs") // указывается имя таблицы
@Access(AccessType.FIELD) //указывает на то, что мы разрешаем доступ к полям класса
public class Song {     //вместо того, чтобы для каждого поля делать методы чтения и записи

    public Song() { }
    public Song(Long id) {
        this.id = id;
    }

    @Id //Каждое поле класса связывается с полем в таблице а для поля ключа, дополнительно, указываются его свойства.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    @Column(name = "year")
    public Long year;

    @ManyToOne()
    @JoinColumn(name="artistid")
    public Artist artist;

    @ManyToOne()
    @JoinColumn(name="shopid")
    public Shop shop;


}

