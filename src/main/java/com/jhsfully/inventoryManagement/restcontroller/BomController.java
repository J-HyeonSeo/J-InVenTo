package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.service.BomInterface;
import com.jhsfully.inventoryManagement.service.BomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/bom")
public class BomController {

    private final BomInterface bomService;

    @GetMapping("")
    @PreAuthorize("hasRole('BOM_READ')")
    public ResponseEntity<?> getBoms(){
        return ResponseEntity.ok(bomService.getBoms());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('BOM_READ')")
    public ResponseEntity<?> getBom(@PathVariable Long id){
        return ResponseEntity.ok(bomService.getBom(id));
    }

    @GetMapping("/leaf/{id}")
    @PreAuthorize("hasRole('BOM_READ')")
    public ResponseEntity<?> getLeafProducts(@PathVariable Long id){
        return ResponseEntity.ok(bomService.getLeafProducts(id));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('BOM_MANAGE')")
    public ResponseEntity<?> addBom(@RequestBody BomDto.BomAddRequest request){
        return ResponseEntity.ok(bomService.addBom(request));
    }

    @PutMapping("")
    @PreAuthorize("hasRole('BOM_MANAGE')")
    public ResponseEntity<?> updateBom(@RequestBody BomDto.BomUpdateRequest request){
        bomService.updateBomNode(request);
        return ResponseEntity.ok(request.getCost());
    }

    @DeleteMapping("/node/{bid}")
    @PreAuthorize("hasRole('BOM_MANAGE')")
    public ResponseEntity<?> deleteBomNode(@PathVariable Long bid){
        bomService.deleteBomNode(bid);
        return ResponseEntity.ok(bid);
    }

    @DeleteMapping("/tree/{pid}")
    @PreAuthorize("hasRole('BOM_MANAGE')")
    public ResponseEntity<?> deleteBomTree(@PathVariable Long pid){
        bomService.deleteBomTree(pid);
        return ResponseEntity.ok(pid);
    }

}
