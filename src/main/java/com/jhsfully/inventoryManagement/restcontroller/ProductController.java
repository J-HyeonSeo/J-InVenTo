package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.service.ProductInterface;
import com.jhsfully.inventoryManagement.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductInterface productService;

    //전체 품목 데이터를 리턴합니다.
    @GetMapping("")
    public ResponseEntity<?> getProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }

    //품목 데이터를 만듭니다. 응답결과를 리턴합니다.
    @PostMapping("")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto.ProductAddRequest request){
        return ResponseEntity.ok(productService.addProduct(request));
    }

    //품목 데이터를 수정합니다. 응답결과를 리턴합니다.
    @PutMapping("")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto.ProductUpdateRequest request){
        return ResponseEntity.ok(productService.updateProduct(request));
    }

    //품목 데이터를 삭제합니다. 응답결과를 리턴합니다.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(id);
    }

}
