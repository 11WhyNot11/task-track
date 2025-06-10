package com.arthur.tasktrackerapi.email;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-email")
public class EmailTestController {
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<String> testEmail(@RequestParam String to) {
        emailService.send(to, "Тест від таск трекер", "Лист дійшов");
        return ResponseEntity.ok("Лист надіслано");
    }

}
