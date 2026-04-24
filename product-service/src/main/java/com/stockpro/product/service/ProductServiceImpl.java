package com.stockpro.product.service;

import com.stockpro.product.dto.ProductRequest;
import com.stockpro.product.dto.ProductResponse;
import com.stockpro.product.entity.Product;
import com.stockpro.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.findBySku(request.getSku()).isPresent()) {
            throw new IllegalArgumentException("Product with SKU " + request.getSku() + " already exists");
        }

        Product product = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .brand(request.getBrand())
                .unitOfMeasure(request.getUnitOfMeasure())
                .costPrice(request.getCostPrice())
                .sellingPrice(request.getSellingPrice())
                .reorderLevel(request.getReorderLevel())
                .maxStockLevel(request.getMaxStockLevel())
                .leadTimeDays(request.getLeadTimeDays())
                .barcode(request.getBarcode())
                .imageUrl(request.getImageUrl())
                .isActive(true)
                .build();

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getById(Long productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
        return mapToResponse(product);
    }

    @Override
    public ProductResponse getBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found with sku " + sku));
        return mapToResponse(product);
    }

    @Override
    public ProductResponse getByBarcode(String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found with barcode " + barcode));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getByBrand(String brand) {
        return productRepository.findByBrand(brand).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchProducts(String name) {
        return productRepository.searchByName(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getLowStockProducts() {
        // In the microservice architecture, low stock logic actually involves Warehouse-Service to get quantities.
        // For product-service alone, this might be better implemented in an aggregation service or Report-Service.
        // Returning empty list for now.
        return List.of();
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setReorderLevel(request.getReorderLevel());
        product.setMaxStockLevel(request.getMaxStockLevel());
        product.setLeadTimeDays(request.getLeadTimeDays());
        product.setBarcode(request.getBarcode());
        product.setImageUrl(request.getImageUrl());

        return mapToResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deactivateProduct(Long productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .brand(product.getBrand())
                .unitOfMeasure(product.getUnitOfMeasure())
                .costPrice(product.getCostPrice())
                .sellingPrice(product.getSellingPrice())
                .reorderLevel(product.getReorderLevel())
                .maxStockLevel(product.getMaxStockLevel())
                .leadTimeDays(product.getLeadTimeDays())
                .barcode(product.getBarcode())
                .imageUrl(product.getImageUrl())
                .isActive(product.getIsActive())
                .build();
    }
}
