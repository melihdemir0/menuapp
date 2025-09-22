package com.application.menuapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.text.Normalizer;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    // ✅ Sadece ADMIN yükleyebilir
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            // Proje içindeki static/images klasörü
            Path uploadPath = Paths.get("src/main/resources/static/images");

            // Klasör yoksa oluştur
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Orijinal dosya adını al
            String originalName = file.getOriginalFilename();

            if (originalName == null || originalName.isBlank()) {
                return ResponseEntity.badRequest().body("Geçersiz dosya adı");
            }

            // ✅ Dosya adını normalize et (boşluk ve Türkçe karakterlerden kurtul)
            String safeName = Normalizer.normalize(originalName, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .replaceAll("[^a-zA-Z0-9._-]", "_");

            // Kaydetme path’i
            Path filePath = uploadPath.resolve(safeName);

            // Dosyayı static/images içine yaz
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Frontend'e dönecek URL
            String imageUrl = "/images/" + safeName;

            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Dosya yüklenemedi: " + e.getMessage());
        }
    }
}
