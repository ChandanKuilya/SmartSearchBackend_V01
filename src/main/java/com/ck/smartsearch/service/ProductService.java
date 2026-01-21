package com.ck.smartsearch.service;

import com.ck.smartsearch.entity.Product;
import com.ck.smartsearch.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final IProductRepository productRepository;
    private final SearchService searchService; // Inject SearchService

    @Transactional
    // When a product is created/updated, we might want to clear the 'allProducts' cache
    // We don't cache the individual create result because it's rarely read immediately
    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product) {
        log.info("Saving product to MySQL: {}", product.getName());
        Product savedProduct = productRepository.save(product);
        searchService.addToIndex(savedProduct.getName());
        return savedProduct;
    }

    // Cache the result of this method in the 'product' bucket with key = id
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        log.info("Fetching product from MySQL DB for ID: {}", id);
        // This log will only appear if it was a Cache MISS
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Cache the entire list. Note: In real apps, be careful caching large lists.
    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        log.info("Fetching all products from MySQL DB");
        return productRepository.findAll();
    }

   /* we should build this like a production-grade system from the start.

    The Problem with Caching getAllProducts()
    In a "Hello World" app with 10 products, caching the whole list is fine. But in a real-world scenario (e.g., Amazon or Flipkart) with 1 million products:

    Memory Explosion: Redis is in-memory. Storing a list of 1 million objects in a single Redis key will crash your cache or evict everything else.

    Network Latency: Fetching a 50MB JSON payload from Redis is slow and blocks the network thread.

    Stale Data Nightmare: If one product changes price, you have to invalidate the entire list of 1 million items.

    The Real-World Solution: Pagination Caching
    Instead of caching "All Products," we cache "Page 1", "Page 2", etc.

    If user A looks at Page 1, we cache products::page=0&size=10.

    If user B looks at Page 50, we cache products::page=49&size=10.

    This keeps cache keys small, fast, and specific.
    */

}