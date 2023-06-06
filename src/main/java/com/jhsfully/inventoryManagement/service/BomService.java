package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.exception.BomException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.model.BOMEntity;
import com.jhsfully.inventoryManagement.model.ProductEntity;
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

import static com.jhsfully.inventoryManagement.type.ProductErrorType.*;

@Service
@AllArgsConstructor
@Transactional
public class BomService implements BomInterface{

    private final BomRepository bomRepository;
    private final ProductRepository productRepository;
    
    //BOM 전체 리스트 리턴
    @Override
    public List<BomDto.BomTopResponse> getBoms() {
        return bomRepository.findByGroupParentProduct();
    }
    
    //해당 품목을 기준으로하는, BOM TREE 리턴
    @Override
    public BomDto.BomTreeResponse getBom(Long productid){
        ProductEntity product = productRepository.findById(productid)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
        return getBomTree(null, product, 1);
    }

    //해당 품목을 기준으로하는, 최하단 품목 및 코스트 리턴.
    @Override
    public List<BomDto.BomLeaf> getLeafProducts(Long productId){
        List<BomDto.BomLeaf> leafs = new ArrayList<>();

        ProductEntity product = productRepository.findById(productId)
                        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        findLeafProductAndCost(leafs, product, null);
        return leafs;
    }

    @Override
    @Transactional
    public BomDto.BomResponse addBom(BomDto.BomAddRequest request) {

        validateAddBom(request); //밸리데이션 검사 수행.

        ProductEntity parentProduct = productRepository.findById(request.getPid())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        ProductEntity childProduct = productRepository.findById(request.getCid())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        BOMEntity bomEntity = BOMEntity.builder()
                .parentProduct(parentProduct)
                .childProduct(childProduct)
                .cost(request.getCost())
                .build();

        return BOMEntity.toDto(bomRepository.save(bomEntity));
    }

    //하나의 BOM Node를 제거합니다.
    @Override
    @Transactional
    public void deleteBomNode(Long bid) {
        if(!bomRepository.existsById(bid)){
            throw new BomException(BomErrorType.BOM_NOT_FOUND);
        }
        bomRepository.deleteById(bid);
    }

    //선택된 BOM의 구성요소를 전부 제거합니다.
    @Override
    @Transactional
    public void deleteBomTree(Long pid){
        ProductEntity product = productRepository.findById(pid)
                        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        bomRepository.deleteByParentProduct(product);
    }

    //======================== Validates =============================
    @Transactional
    private void validateAddBom(BomDto.BomAddRequest request) {
        //순환참조 검증.
        if(!validateCirculation(request)){
            throw new BomException(BomErrorType.HAS_SAME_PARENT);
        }
        //pid가 productinfo에 존재하여야함.
        if(!productRepository.existsById(request.getPid())){
            throw new ProductException(PRODUCT_NOT_FOUND);
        }
        //cid가 productinfo에 존재하여야함.
        if(!productRepository.existsById(request.getCid())){
            throw new ProductException(PRODUCT_NOT_FOUND);
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

        ProductEntity parentProduct = productRepository.findById(request.getPid())
                        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        ProductEntity childProduct = productRepository.findById(request.getCid())
                        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        findParents(parentProduct, parents1);
        findParents(childProduct, parents2);

        parents1.retainAll(parents2);

        return parents1.size() == 0;
    }

    //========================== Utils ========================================
    private BomDto.BomTreeResponse getBomTree(Long id,
                                              ProductEntity product,
                                              double cost){
        List<BOMEntity> entities = bomRepository.findByParentProduct(product);
        BomDto.BomTreeResponse bomTree = BomDto.BomTreeResponse.builder()
                .id(id)
                .productId(product.getId())
                .productName(product.getName())
                .cost(cost)
                .children(new ArrayList<>())
                .build();
        System.out.println(entities);
        if(entities.size() == 0){ //escape
            return bomTree;
        }
        for(BOMEntity entity : entities){
            bomTree.getChildren().add(getBomTree(entity.getId(),
                    entity.getChildProduct(),
                    entity.getCost()));
        }
        return bomTree;
    }

    private void findParents(ProductEntity product, ArrayList<Long> out){
        //자식에 현재 프로덕트가 존재하지 않으면, 최상단 부모라고 볼 수 있음.
        if(!bomRepository.existsByChildProduct(product)){
            out.add(product.getId());
            return;
        }
        //현재 프로덕트를 자식으로 포함하는 부모 프로덕트를 가져옴.
        List<BOMEntity> entities = bomRepository.findByChildProduct(product);
        for(BOMEntity entity : entities){
            findParents(entity.getParentProduct(), out);
        }
    }

    //최하단 품목 및 코스트 찾기
    public void findLeafProductAndCost(List<BomDto.BomLeaf> leafs,
                                       ProductEntity product, Double cost){
        //check leaf node
        if(!bomRepository.existsByParentProduct(product)){
            if(cost == null){ //bom에 존재하지 않는 경우.
                leafs.add(new BomDto.BomLeaf(product.getId(), product.getName(), 1D));
            }else{
                leafs.add(new BomDto.BomLeaf(product.getId(), product.getName(), cost));
            }
            return;
        }
        if(cost == null){
            cost = 1D;
        }

        List<BOMEntity> bomEntities = bomRepository.findByParentProduct(product);
        for(BOMEntity bomEntity : bomEntities){
            findLeafProductAndCost(leafs, bomEntity.getChildProduct(), cost * bomEntity.getCost());
        }
    }
}
