package com.stockpro.product.service;

import com.stockpro.product.dto.ProductRequest;
import com.stockpro.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getById(Long productId);
    ProductResponse getBySku(String sku);
    ProductResponse getByBarcode(String barcode);
    List<ProductResponse> getByCategory(String category);
    List<ProductResponse> getByBrand(String brand);
    List<ProductResponse> searchProducts(String name);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getLowStockProducts(); // Since warehouse manages stock, this might need an external call later, but we'll mock it or let warehouse call product.
    ProductResponse updateProduct(Long productId, ProductRequest request);
    void deactivateProduct(Long productId);
    void deleteProduct(Long productId);
}
