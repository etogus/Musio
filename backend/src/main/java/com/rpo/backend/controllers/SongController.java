package com.rpo.backend.controllers;

import com.rpo.backend.models.Song;
import com.rpo.backend.repositories.CountryRepository;
import com.rpo.backend.repositories.SongRepository;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class SongController {
    @Autowired
    SongRepository songRepository;

    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/songs")
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @PostMapping("/songs")
    public ResponseEntity<Object> createSong(@Valid @RequestBody Song song) {
        Song nc = songRepository.save(song);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable(value = "id") Long songId,
                                                   @Valid @RequestBody Song songDetails) {
        Song song = null;
        Optional<Song> mm = songRepository.findById(songId);
        if (mm.isPresent())
        {
            song = mm.get();
            song.name = songDetails.name;
            song.year = songDetails.year;
            songRepository.save(song);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "song_not_found"
            );
        }
        return ResponseEntity.ok(song);
    }

    @DeleteMapping("/songs/{id}")
    public Map<String, Boolean> deleteSong(@PathVariable(value = "id") Long songId) {
        Optional<Song> song = songRepository.findById(songId);
        Map<String, Boolean> response = new HashMap<>();
        if (song.isPresent())
        {
            songRepository.delete(song.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }
}
