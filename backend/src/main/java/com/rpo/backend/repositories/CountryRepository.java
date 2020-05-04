package com.rpo.backend.repositories;

import com.rpo.backend.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

// CountryRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы countries. Надо лишь указать тип Country в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface CountryRepository extends JpaRepository<Country, Long> {

}
