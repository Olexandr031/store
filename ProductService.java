package com.example.store.service;

import com.example.store.dto.product.ProductDTO;
import com.example.store.exception.ProductNotExistException;
import com.example.store.model.Category;
import com.example.store.model.Product;
import com.example.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> listProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();
        for(Product product : products) {
            ProductDTO productDTO = getDTOFromProduct(product);
            productDTOs.add(productDTO);
        }
        return productDTOs;
    }

    public static ProductDTO getDTOFromProduct(Product product) {
        ProductDTO productDTO = new ProductDTO(product);
        return productDTO;
    }

    public static Product getProductFromDTO(ProductDTO productDTO, Category category) {
        Product product = new Product(productDTO, category);
        return product;
    }

    public void addProduct(ProductDTO productDTO, Category category) {
        Product product = getProductFromDTO(productDTO, category);
        productRepository.save(product);
    }

    public void updateProduct(Integer productID, @Valid ProductDTO productDTO, Category category) {
        Product product = getProductFromDTO(productDTO, category);
        product.setId(productID);
        productRepository.save(product);
    }


    public Product getProductById(Integer productId) throws ProductNotExistException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent())
            throw new ProductNotExistException("Product id is invalid " + productId);
        return optionalProduct.get();
    }
}
