package com.rpo.backend.repositories;

import com.rpo.backend.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

// SongRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы songs. Надо лишь указать тип Song в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface SongRepository extends JpaRepository<Song, Long> {
}
