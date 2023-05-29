package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    //전체 품목 데이터를 리턴합니다.
    @GetMapping("/")
    public ResponseEntity<?> getProducts(){
        return ResponseEntity.ok(productService);
    }

    //품목 데이터를 만듭니다. 응답결과를 리턴합니다.
    @PostMapping("/")
    public ResponseEntity<?> createProduct(){
        return null;
    }

    //품목 데이터를 수정합니다. 응답결과를 리턴합니다.
    @PutMapping("/")
    public ResponseEntity<?> updateProduct(){
        return null;
    }

    //품목 데이터를 삭제합니다. 응답결과를 리턴합니다.
    @DeleteMapping("/")
    public ResponseEntity<?> deleteProduct(){
        return null;
    }

}
