package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.service.BomInterface;
import com.jhsfully.inventoryManagement.service.BomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/bom")
public class BomController {

    private final BomInterface bomService;

    @GetMapping("")
    public ResponseEntity<?> getBoms(){
        return ResponseEntity.ok(bomService.getBoms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBom(@PathVariable Long id){
        return ResponseEntity.ok(bomService.getBom(id));
    }

    @GetMapping("/leaf/{id}")
    public ResponseEntity<?> getLeafProducts(@PathVariable Long id){
        return ResponseEntity.ok(bomService.getLeafProducts(id));
    }

    @PostMapping("")
    public ResponseEntity<?> addBom(@RequestBody BomDto.BomAddRequest request){
        return ResponseEntity.ok(bomService.addBom(request));
    }

    @PutMapping("")
    public ResponseEntity<?> updateBom(@RequestBody BomDto.BomUpdateRequest request){
        bomService.updateBomNode(request);
        return ResponseEntity.ok(request.getCost());
    }

    @DeleteMapping("/node/{bid}")
    public ResponseEntity<?> deleteBomNode(@PathVariable Long bid){
        bomService.deleteBomNode(bid);
        return ResponseEntity.ok(bid);
    }

    @DeleteMapping("/tree/{pid}")
    public ResponseEntity<?> deleteBomTree(@PathVariable Long pid){
        bomService.deleteBomTree(pid);
        return ResponseEntity.ok(pid);
    }

}
