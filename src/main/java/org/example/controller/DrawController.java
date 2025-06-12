package org.example.controller;

import org.example.model.Draw;
import org.example.service.DrawService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/draws")
public class DrawController {

    private final DrawService drawService;

    public DrawController(DrawService drawService) {
        this.drawService = drawService;
    }

    // 1. Создание нового тиража
    @PostMapping
    public ResponseEntity<?> createDraw() {
        try {
            Draw created = drawService.createDraw();
            return ResponseEntity.ok(created);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2. Закрытие тиража с генерацией чисел
    @PostMapping("/{id}/close")
    public ResponseEntity<?> closeDraw(@PathVariable("id") Long id) {
        try {
            Draw closed = drawService.closeDraw(id);
            return ResponseEntity.ok(closed);
        } catch (NoSuchElementException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 3. Получение результатов тиража
    @GetMapping("/{id}/results")
    public ResponseEntity<?> getDrawResults(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(drawService.getDrawResults(id));
        } catch (NoSuchElementException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
