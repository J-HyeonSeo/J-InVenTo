package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.BomDto;

import java.util.List;

public interface BomInterface {

    public List<BomDto.BomResponse> getBoms();

    public BomDto.BomTree getBom(Long bid, Long pid, double cost);

    public BomDto.BomResponse addBom(BomDto.BomAddRequest request);

    public void deleteBom(Long id);

}
