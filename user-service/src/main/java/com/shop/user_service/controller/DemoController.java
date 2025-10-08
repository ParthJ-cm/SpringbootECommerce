package com.shop.user_service.controller;

import com.shop.user_service.DTO.DemoDto;
import com.shop.user_service.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/endpoint")
public class DemoController {
    private final DemoService demoService;

    @PostMapping
    public ResponseEntity<DemoDto> createNewDemo(@RequestBody DemoDto demoDto){
        return ResponseEntity.ok(demoService.createNewDemo(demoDto));
    }

    @GetMapping
    public ResponseEntity<List<DemoDto>> getAllDemo(){
        return ResponseEntity.ok(demoService.getAllDemo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemoDto> getDemoById(@PathVariable Long id){
        return ResponseEntity.ok(demoService.getDemoById(id));
    }
}