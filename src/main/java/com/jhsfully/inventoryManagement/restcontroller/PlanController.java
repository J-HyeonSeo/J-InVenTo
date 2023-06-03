package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.PlanDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/plan")
public class PlanController {

    //시작일은 필수!, 종료일은 선택!
    @GetMapping("")
    public ResponseEntity<?> getPlans(
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate
    ){
        return ResponseEntity.ok(endDate);
    }

    @PostMapping("")
    public ResponseEntity<?> addPlan(@RequestBody PlanDto.PlanAddRequest request){
        return null;
    }

    @PutMapping("")
    public ResponseEntity<?> updatePlan(@RequestBody PlanDto.PlanUpdateRequest request){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id){
        return null;
    }

}
