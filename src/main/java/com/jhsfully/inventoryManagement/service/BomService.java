package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.exception.BomException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.entity.BOMEntity;
import com.jhsfully.inventoryManagement.entity.ProductEntity;
import com.jhsfully.inventoryManagement.repository.BomRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.type.BomErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.jhsfully.inventoryManagement.type.CacheType.*;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;

@Service
@AllArgsConstructor
@Transactional
public class BomService implements BomInterface{

    private final BomRepository bomRepository;
    private final ProductRepository productRepository;
    
    //BOM 전체 리스트 리턴
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = BOM_TOP, cacheManager = "redisCacheManager")
    public List<BomDto.BomTopResponse> getBoms() {
        return bomRepository.findByGroupParentProduct();
    }
    
    //해당 품목을 기준으로하는, BOM TREE 리턴
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = BOM_TREE, cacheManager = "redisCacheManager")
    public BomDto.BomTreeResponse getBom(Long productid){
        ProductEntity product = productRepository.findById(productid)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
        return getBomTree(null, product, 1);
    }

    //해당 품목을 기준으로하는, 최하단 품목 및 코스트 리턴.
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = BOM_LEAF, cacheManager = "redisCacheManager")
    public List<BomDto.BomLeaf> getLeafProducts(Long productId){
        List<BomDto.BomLeaf> leafs = new ArrayList<>();

        ProductEntity product = productRepository.findById(productId)
                        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        findLeafProductAndCost(leafs, product, null);
        return leafs;
    }

    @Getter
    @Setter
    class ProductSet{ //명시적인 지정을 위해 만들어진 클래스임.
        private ProductEntity parent;
        private ProductEntity child;
    }
    @Override
    @CacheEvict(cacheNames = {BOM_TOP, BOM_TREE, BOM_LEAF}, allEntries = true)
    public BomDto.BomResponse addBom(BomDto.BomAddRequest request) {

        ProductSet productSet = validateAddBom(request); //밸리데이션 검사 수행.

        BOMEntity bomEntity = BOMEntity.builder()
                .parentProduct(productSet.getParent())
                .childProduct(productSet.getChild())
                .cost(request.getCost())
                .build();

        return BOMEntity.toDto(bomRepository.save(bomEntity));
    }

    @Override
    @CacheEvict(cacheNames = {BOM_TOP, BOM_TREE, BOM_LEAF}, allEntries = true)
    public void updateBomNode(BomDto.BomUpdateRequest request){
        BOMEntity bom = validateUpdateBom(request);
        bom.setCost(request.getCost());
        bomRepository.save(bom);
    }

    //하나의 BOM Node를 제거합니다.
    @Override
    @CacheEvict(cacheNames = {BOM_TOP, BOM_TREE, BOM_LEAF}, allEntries = true)
    public void deleteBomNode(Long bid) {
        if(!bomRepository.existsById(bid)){
            throw new BomException(BomErrorType.BOM_NOT_FOUND);
        }
        bomRepository.deleteById(bid);
    }

    //선택된 BOM의 구성요소를 전부 제거합니다.
    @Override
    @CacheEvict(cacheNames = {BOM_TOP, BOM_TREE, BOM_LEAF}, allEntries = true)
    public void deleteBomTree(Long pid){
        ProductEntity product = productRepository.findById(pid)
                        .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        bomRepository.deleteByParentProduct(product);
    }

    //======================== Validates =============================

    private ProductSet validateAddBom(BomDto.BomAddRequest request) {

        //cost는 비어있으면 안되요.
        if(request.getCost() == null){
            throw new BomException(BomErrorType.COST_NULL);
        }

        //0이하의 코스트는 허용하지 않음.
        if(request.getCost() <= 0){
            throw new BomException(BomErrorType.COST_MINUS);
        }

        ProductEntity parentProduct = productRepository.findById(request.getPid())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        ProductEntity childProduct = productRepository.findById(request.getCid())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
        //순환참조 검증.
        if(!validateCirculation(parentProduct, childProduct)){
            throw new BomException(BomErrorType.HAS_SAME_PARENT);
        }

        ProductSet productSet = new ProductSet();
        productSet.setParent(parentProduct);
        productSet.setChild(childProduct);

        return productSet;
    }

    //순환 참조 체크
    private boolean validateCirculation(ProductEntity parentProduct,
                                        ProductEntity childProduct){

        //1:N 참조가 가능하기에, 부모는 여러개가 발생함.
        ArrayList<Long> parents1 = new ArrayList<>();
        ArrayList<Long> parents2 = new ArrayList<>();

        findParents(parentProduct, parents1);
        findParents(childProduct, parents2);

        parents1.retainAll(parents2);

        return parents1.size() == 0;
    }

    private BOMEntity validateUpdateBom(BomDto.BomUpdateRequest request){

        if(request.getCost() == null){
            throw new BomException(BomErrorType.COST_NULL);
        }

        if(request.getCost() <= 0){
            throw new BomException(BomErrorType.COST_MINUS);
        }

        BOMEntity bomEntity = bomRepository.findById(request.getId())
                .orElseThrow(() -> new BomException(BomErrorType.BOM_NOT_FOUND));

        return bomEntity;
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
        if(entities.isEmpty()){ //escape
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
    private void findLeafProductAndCost(List<BomDto.BomLeaf> leafs,
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
