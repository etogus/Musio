package com.rpo.backend.controllers;

import com.rpo.backend.models.Artist;
import com.rpo.backend.models.Country;
import com.rpo.backend.repositories.ArtistRepository;
import com.rpo.backend.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    CountryRepository countryRepository;

    // GET запрос, метод возвращает список артистов, который будет автоматически преобразован в JSON
    @GetMapping("/artists")
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    // POST запрос, сохраняем в таблице artists нового артиста
    // В теле метода проверяем, что существует country с заданным id,
    // Если да, то artist.country = <Название_страны>
    // Без проверки было бы null
    @PostMapping("artists")
    public ResponseEntity<Object> createArtist(@Valid @RequestBody Artist artist) {
        Optional<Country> cc = countryRepository.findById(artist.country.id);
        if(cc.isPresent()) {
            artist.country = cc.get();
        }
        Artist nc = artistRepository.save(artist);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    // PUT запрос, обновляет запись в таблице artists
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если артист с указанным ключом отсутствует
    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long artistId,
                                               @Valid @RequestBody Artist artistDetails) {
        Artist artist = null;
        Optional<Artist> aa = artistRepository.findById(artistId);
        if (aa.isPresent())
        {
            artist = aa.get();
            artist.name = artistDetails.name;
            artist.century = artistDetails.century;
            artist.country = artistDetails.country;
            artistRepository.save(artist);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "artist_not_found"
            );
        }
        return ResponseEntity.ok(artist);
    }

    // DELETE запрос, метод удаления записи из таблицы artists
    @DeleteMapping("/artists/{id}")
    public Map<String, Boolean> deleteArtist(@PathVariable(value = "id") Long artistId) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        Map<String, Boolean> response = new HashMap<>();
        if (artist.isPresent())
        {
            artistRepository.delete(artist.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }
}
