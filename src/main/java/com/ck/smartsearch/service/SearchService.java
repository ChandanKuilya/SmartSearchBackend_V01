package com.ck.smartsearch.service;

import com.ck.smartsearch.customstructlayer.Trie;
import com.ck.smartsearch.entity.Product;
import com.ck.smartsearch.repository.IProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final IProductRepository productRepository;
    private final Trie trie;

    // Load all products into Trie on startup
    @PostConstruct
    public void initTrie() {
        log.info("Loading products into Trie...");
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            trie.insert(product.getName());
        }
        log.info("Loaded {} products into Trie.", products.size());
    }

    // Add single product to Trie (called after saving to DB)
    public void addToIndex(String productName) {
        trie.insert(productName);
    }

    public List<String> searchPrefix(String prefix) {
        return trie.autocomplete(prefix);
    }
}
