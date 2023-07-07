package com.jhsfully.inventoryManagement.aop;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/log")
public class LogController {

    private final LogService logService;

    @GetMapping
    public ResponseEntity<?> getLogs(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                     @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate){
        return ResponseEntity.ok(logService.getLogs(startDate, endDate));
    }

    @DeleteMapping
    public void deleteLogs(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                           @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate){

    }

}
