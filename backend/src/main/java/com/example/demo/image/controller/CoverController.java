package com.example.demo.image.controller;

import com.example.demo.image.dto.CoverRequest;
import com.example.demo.image.dto.CoverResponse;
import com.example.demo.image.service.CoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cover")   // POST /api/cover
@RequiredArgsConstructor
public class CoverController {

    private final CoverService coverService;

    @PostMapping
    public ResponseEntity<CoverResponse> createCover(@RequestBody CoverRequest request) {

        try {
            String imageUrl = coverService.createCover(request);
            return ResponseEntity.ok(new CoverResponse(imageUrl));

        } catch (Exception e) {
            // 실제 프로젝트에서는 별도의 예외 처리 핸들러로 분리하는 게 좋습니다.
            return ResponseEntity.internalServerError()
                    .body(new CoverResponse("ERROR: " + e.getMessage()));
        }
    }
}
