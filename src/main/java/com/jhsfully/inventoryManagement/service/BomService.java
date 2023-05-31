package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.exception.BomException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.model.BOMEntity;
import com.jhsfully.inventoryManagement.repository.BomRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.type.BomErrorType;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class BomService implements BomInterface{

    private final BomRepository bomRepository;
    private final ProductRepository productRepository;

    @Override
    public List<BomDto.BomResponse> getBoms() {
        return bomRepository.findAll().stream()
                .map(BOMEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BomDto.BomTree getBom(Long bid, Long pid, double cost){
        List<BOMEntity> entities = bomRepository.findByPid(pid);
        BomDto.BomTree bomTree = BomDto.BomTree.builder()
                .bid(bid)
                .pid(pid)
                .cost(cost)
                .children(new ArrayList<>())
                .build();
        System.out.println(entities);
        if(entities.size() == 0){ //escape
            return bomTree;
        }
        for(BOMEntity entity : entities){
            bomTree.getChildren().add(getBom(entity.getId(), entity.getCid(), cost * entity.getCost()));
        }
        return bomTree;
    }

    @Override
    public BomDto.BomResponse addBom(BomDto.BomAddRequest request) {

        validateAddBom(request); //밸리데이션 검사 수행.

        BOMEntity bomEntity = BOMEntity.builder()
                .pid(request.getPid())
                .cid(request.getCid())
                .cost(request.getCost())
                .build();

        return BOMEntity.toDto(bomRepository.save(bomEntity));
    }

    //하나의 BOM Node를 제거합니다.
    @Override
    public void deleteBomNode(Long bid) {
        if(!bomRepository.existsById(bid)){
            throw new BomException(BomErrorType.BOM_NOT_FOUND);
        }
        bomRepository.deleteById(bid);
    }

    //선택된 BOM의 구성요소를 전부 제거합니다.
    @Override
    public void deleteBomTree(Long pid){
        if(!bomRepository.existsByPid(pid)){
            throw new BomException(BomErrorType.BOM_NOT_FOUND);
        }
        bomRepository.deleteByPid(pid);
    }

    //======================== Validates =============================
    private void validateAddBom(BomDto.BomAddRequest request) {
        //순환참조 검증.
        if(!validateCirculation(request)){
            throw new BomException(BomErrorType.HAS_SAME_PARENT);
        }
        //pid가 productinfo에 존재하여야함.
        if(!productRepository.existsById(request.getPid())){
            throw new ProductException(ProductErrorType.PRODUCT_NOT_FOUND);
        }
        //cid가 productinfo에 존재하여야함.
        if(!productRepository.existsById(request.getCid())){
            throw new ProductException(ProductErrorType.PRODUCT_NOT_FOUND);
        }
        //0이하의 코스트는 허용하지 않음.
        if(request.getCost() <= 0){
            throw new BomException(BomErrorType.COST_MINUS);
        }
    }

    //순환 참조 체크
    private boolean validateCirculation(BomDto.BomAddRequest request){

        //1:N 참조가 가능하기에, 부모는 여러개가 발생함.
        ArrayList<Long> parents1 = new ArrayList<>();
        ArrayList<Long> parents2 = new ArrayList<>();

        findParents(request.getPid(), parents1);
        findParents(request.getCid(), parents2);

        parents1.retainAll(parents2);

        return parents1.size() == 0;
    }

    private void findParents(Long id, ArrayList<Long> out){
        //자식 ID로 부모ID를 찾을 수 없으면 최상단 부모임.
        if(!bomRepository.existsByCid(id)){
            out.add(id);
            return;
        }
        List<BOMEntity> entities = bomRepository.findByCid(id);
        for(BOMEntity entity : entities){
            findParents(entity.getPid(), out);
        }
    }

}
