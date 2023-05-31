package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.service.BomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/bom")
public class BomController {

    private final BomService bomService;

    @GetMapping("")
    public ResponseEntity<?> getBoms(){
        return ResponseEntity.ok(bomService.getBoms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBom(@PathVariable Long id){
        return ResponseEntity.ok(bomService.getBom(null ,id, 1));
    }

    @PostMapping("")
    public ResponseEntity<?> addBom(@RequestBody BomDto.BomAddRequest request){
        return ResponseEntity.ok(bomService.addBom(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBom(@PathVariable Long id){
        bomService.deleteBom(id);
        return ResponseEntity.ok(id);
    }

}
