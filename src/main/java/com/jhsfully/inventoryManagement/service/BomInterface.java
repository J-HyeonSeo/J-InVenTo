package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.BomDto;

import java.util.List;

public interface BomInterface {

    List<BomDto.BomTopResponse> getBoms();

    BomDto.BomTreeResponse getBom(Long pid);

    List<BomDto.BomLeaf> getLeafProducts(Long id);

    BomDto.BomResponse addBom(BomDto.BomAddRequest request);

    void updateBomNode(BomDto.BomUpdateRequest request);

    void deleteBomNode(Long bid);

    void deleteBomTree(Long pid);

}
