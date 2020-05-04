package com.rpo.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SampleController {

    // методу getTitle будт передаваться клиентские GET запросы
    // с локальной частью URL сложенной из “/api/v1” и “title”.
    @GetMapping("/title")
    public String getTitle() {
        return "<title>Hello from Backend</title>";
    }
}
