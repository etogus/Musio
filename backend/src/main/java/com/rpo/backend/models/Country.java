package com.rpo.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // указывает, что класс - это таблица в базе
@Table(name = "countries") // указывается имя таблицы
@Access(AccessType.FIELD) //указывает на то, что мы разрешаем доступ к полям класса
public class Country {    //вместо того, чтобы для каждого поля делать методы чтения и записи

    public Country() {}
    public Country(Long id) {
        this.id = id;
    }

    @Id //Каждое поле класса связывается с полем в таблице а для поля ключа, дополнительно, указываются его свойства.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    // Обратное отношение к модели Artist. В этом отношении надо указать поле, которое
    // в противоположной модели используется как ссылка на нас – это сountry.
    // ***
    // Модель страны содержит ссылку на artists, точно также, артисты имеют ссылку на свою страну.
    // Получается замкнутый круг. Для модели это не проблема, но это проблема для движка,
    // который сериализует модель, то есть превращает объекты модели в JSON.
    // JsonIgnore - для того, чтобы прервать цепочку ссылок
    // поле не будет включено в JSON при сериализации объекта Country и цепочка разомкнется.
    @JsonIgnore
    @OneToMany(mappedBy = "country") // один ко многим
    public List<Artist> artists = new ArrayList<>();
}
