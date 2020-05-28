package com.rpo.backend.controllers;

import com.rpo.backend.models.Artist;
import com.rpo.backend.models.Country;
import com.rpo.backend.repositories.CountryRepository;
import com.rpo.backend.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class CountryController {
    @Autowired
    CountryRepository countryRepository;

    // GET запрос, метод возвращает список стран, который будет автоматически преобразован в JSON
    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    // GET запрос, метод возвращает запись из таблицы countries c указанным индексом.
    // Индекс указывается в URL.
    @GetMapping("/countries/{id}")
    public ResponseEntity<Country> getCountry(@PathVariable(value = "id") Long countryId)
            throws DataValidationException
    {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(()-> new DataValidationException("Страна с таким индексом не найдена"));
        return ResponseEntity.ok(country);
    }

    // POST запрос, сохраняем в таблице countries новую страну
    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@Valid @RequestBody Country country)
            throws DataValidationException {
        try {
            Country nc = countryRepository.save(country);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex)
        {
            String error;
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    // PUT запрос, обновляет запись в таблице country
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если страна с указанным ключом отсутствует
    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") Long countryId,
                                                 @Valid @RequestBody Country countryDetails)
            throws DataValidationException
    {
        try {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
            country.name = countryDetails.name;
            countryRepository.save(country);
            return ResponseEntity.ok(country);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    // POST запрос, метод удаления списка стран из таблицы countries
    @PostMapping("/deletecountries")
    public ResponseEntity deleteCountries(@Valid @RequestBody List<Country> countries) {
        countryRepository.deleteAll(countries);
        return new ResponseEntity(HttpStatus.OK);
    }

    // GET запрос для вывода списка артистов для страны
    @GetMapping("/countries/{id}/artists")
    public ResponseEntity<List<Artist>> getCountryArtists(@PathVariable(value = "id") Long countryId) {
        Optional<Country> cc = countryRepository.findById(countryId);
        if(cc.isPresent()) {
            return ResponseEntity.ok(cc.get().artists);
        }
        return ResponseEntity.ok(new ArrayList<Artist>());
    }
}
