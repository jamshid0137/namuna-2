package com.example.smartcast.controller;

import com.example.smartcast.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileStorageController {
    private final FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }
    @PostMapping("/")
    public ResponseEntity upload(@RequestParam("file")MultipartFile multipartFile){
        fileStorageService.save(multipartFile);
        return  ResponseEntity.ok(multipartFile.getOriginalFilename()+": muaffiqiyatli saqlandi !");
    }
}
