package com.rpo.backend.controllers;

import com.rpo.backend.models.Shop;
import com.rpo.backend.repositories.CountryRepository;
import com.rpo.backend.repositories.ShopRepository;
import com.rpo.backend.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ShopController {
    @Autowired
    ShopRepository shopRepository;

    @Autowired
    CountryRepository countryRepository;

    // GET запрос, метод возвращает список магазинов, который будет автоматически преобразован в JSON
    @GetMapping("/shops")
    public Page<Shop> getAllShops(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return shopRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<Shop> getShop(@PathVariable(value = "id") Long shopId)
            throws DataValidationException
    {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(()-> new DataValidationException("Магазин с таким индеком не найден"));
        return ResponseEntity.ok(shop);
    }

    // POST запрос, сохраняем в таблице shops новый магазин
    @PostMapping("/shops")
    public ResponseEntity<Object> createShop(@Valid @RequestBody Shop shop) {
        Shop nc = shopRepository.save(shop);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    // PUT запрос, обновляет запись в таблице shops
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если магазин с указанным ключом отсутствует
    @PutMapping("/shops/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable(value = "id") Long shopId,
                                           @Valid @RequestBody Shop shopDetails) {
        Shop shop = null;
        Optional<Shop> mm = shopRepository.findById(shopId);
        if (mm.isPresent())
        {
            shop = mm.get();
            shop.name = shopDetails.name;
            shop.location = shopDetails.location;
            shopRepository.save(shop);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "shop_not_found"
            );
        }
        return ResponseEntity.ok(shop);
    }

    // DELETE запрос, метод удаления записи из таблицы shops
    @PostMapping("/deleteshops")
    public ResponseEntity deleteShops(@Valid @RequestBody List<Shop> shops) {
        shopRepository.deleteAll(shops);
        return new ResponseEntity(HttpStatus.OK);
    }
}

