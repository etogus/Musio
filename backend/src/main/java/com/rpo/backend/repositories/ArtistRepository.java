package com.rpo.backend.repositories;

import com.rpo.backend.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

// ArtistRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы artists. Надо лишь указать тип Artist в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
