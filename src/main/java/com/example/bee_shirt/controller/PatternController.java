package com.example.bee_shirt.controller;

import com.example.bee_shirt.EntityThuocTinh.Pattern;
import com.example.bee_shirt.repository.PatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patterns")
public class PatternController {

    @Autowired
    private PatternRepository patternRepository;

    // Hiển thị danh sách mẫu với phân trang 5 phần tử
    @GetMapping("/list")
    public ResponseEntity<Page<Pattern>> getPatterns(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Pattern> patterns = patternRepository.findAllPatterns(pageable);
        return ResponseEntity.ok(patterns);
    }

    // Thêm mẫu
    @PostMapping("/add")
    public ResponseEntity<Pattern> addPattern(@RequestBody Pattern pattern) {
        Pattern savedPattern = patternRepository.save(pattern);
        return ResponseEntity.ok(savedPattern);
    }

    // Sửa mẫu theo mã
    @PutMapping("/update/{codePattern}")
    public ResponseEntity<Pattern> updatePattern(@PathVariable String codePattern, @RequestBody Pattern updatedDetails) {
        Pattern pattern = patternRepository.findByCodePattern(codePattern);

        if (pattern == null) {
            return ResponseEntity.notFound().build();
        }

        pattern.setCodePattern(updatedDetails.getCodePattern());
        pattern.setNamePattern(updatedDetails.getNamePattern());
        pattern.setStatusPattern(updatedDetails.getStatusPattern());
        Pattern updatedPattern = patternRepository.save(pattern);
        return ResponseEntity.ok(updatedPattern);
    }

    // Xóa mềm mẫu
    @PutMapping("/delete/{codePattern}")
    public ResponseEntity<Pattern> deletePattern(@PathVariable String codePattern) {
        Pattern pattern = patternRepository.findByCodePattern(codePattern);

        if (pattern == null) {
            return ResponseEntity.notFound().build();
        }

        pattern.setDeleted(true);
        Pattern updatedPattern = patternRepository.save(pattern);
        return ResponseEntity.ok(updatedPattern);
    }

    // Lấy chi tiết mẫu theo mã
    @GetMapping("/detail/{codePattern}")
    public ResponseEntity<Pattern> getPatternDetail(@PathVariable String codePattern) {
        Pattern pattern = patternRepository.findByCodePattern(codePattern);
        return ResponseEntity.ok(pattern);
    }
}