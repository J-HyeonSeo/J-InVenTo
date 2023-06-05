package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.BomDto;

import java.util.List;

public interface BomInterface {

    public List<BomDto.BomTopResponse> getBoms();

    public BomDto.BomTreeResponse getBom(Long pid);

    public List<BomDto.BomLeaf> getLeafProducts(Long id);

    public BomDto.BomResponse addBom(BomDto.BomAddRequest request);

    public void deleteBomNode(Long bid);

    public void deleteBomTree(Long pid);

}
