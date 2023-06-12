package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.service.ProductInterface;
import com.jhsfully.inventoryManagement.service.ProductService;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductInterface productService;

    //전체 품목 데이터를 리턴합니다.
    @GetMapping("")
    @PreAuthorize("hasRole('PRODUCT_READ')")
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //품목 데이터를 만듭니다. 응답결과를 리턴합니다.
    @PostMapping("")
    @PreAuthorize("hasRole('PRODUCT_MANAGE')")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto.ProductAddRequest request){
        return ResponseEntity.ok(productService.addProduct(request));
    }

    //품목 데이터를 수정합니다. 응답결과를 리턴합니다.
    @PutMapping("")
    @PreAuthorize("hasRole('PRODUCT_MANAGE')")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto.ProductUpdateRequest request){
        return ResponseEntity.ok(productService.updateProduct(request));
    }

    //비활성화 하도록함.
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('PRODUCT_MANAGE')")
    public ResponseEntity<?> disableProduct(@PathVariable Long id){
        productService.disableProduct(id);
        return ResponseEntity.ok(id);
    }

    //품목 데이터를 삭제합니다. 응답결과를 리턴합니다.
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PRODUCT_MANAGE')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(id);
    }

}
