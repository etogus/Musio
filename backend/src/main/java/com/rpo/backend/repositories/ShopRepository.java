package com.rpo.backend.repositories;

import com.rpo.backend.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

// ShopRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы shops. Надо лишь указать тип Shop в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
