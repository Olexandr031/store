package com.example.store.repository;

import com.example.store.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Integer> {

    public Category findByCategoryName(String categoryName);
}
