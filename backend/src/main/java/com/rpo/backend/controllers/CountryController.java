package com.rpo.backend.controllers;

import com.rpo.backend.models.Artist;
import com.rpo.backend.models.Country;
import com.rpo.backend.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

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

    // POST запрос, сохраняем в таблице countries новую страну
    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@Valid @RequestBody Country country) {
        // чтобы не было повторящихся названий стран
        try {
            Country nc = countryRepository.save(country);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        } catch (Exception ex) {
            String error;
            if(ex.getMessage().contains("countries.name_UNIQUE"))
                error = "country_already_exists";
            else
                error = "undefined_error";
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }

    // PUT запрос, обновляет запись в таблице country
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если страна с указанным ключом отсутствует
    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") Long countryId,
                                                 @Valid @RequestBody Country countryDetails) {
        Country country = null;
        Optional<Country> optionalCountry = countryRepository.findById(countryId);
        if(optionalCountry.isPresent()) {
            country = optionalCountry.get();
            country.name = countryDetails.name;
            countryRepository.save(country);
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "country_not_found"
            );
        }
        return ResponseEntity.ok(country);
    }

    // DELETE запрос, метод удаления записи из таблицы countries
    @DeleteMapping("/countries/{id}")
    public Map<String, Boolean> deleteCountry(@PathVariable(value = "id") Long countryId) {
        Optional<Country> country = countryRepository.findById(countryId);
        Map<String, Boolean> response = new HashMap<>();
        if(country.isPresent()) {
            countryRepository.delete(country.get());
            response.put("deleted", Boolean.TRUE);
        }
        else {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
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
