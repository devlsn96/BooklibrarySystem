package com.example.demo.image.controller;

import com.example.demo.image.dto.ImageRequest;
import org.springframework.web.bind.annotation.*;
import com.example.demo.image.service.OpenAiService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final OpenAiService openAiService;

    public ImageController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/generate")
    public String generateImage(@RequestBody ImageRequest request) {
        return openAiService.generateImage(request);   // DTO 전체 전달
    }
}
