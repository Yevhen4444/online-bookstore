package com.example.bookstore.repository;

import com.example.bookstore.entity.Category;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldSaveAndFindCategoryById() {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        Category savedCategory = categoryRepository.save(category);
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Fantasy");
        assertThat(foundCategory.get().getDescription()).isEqualTo("Fantasy books");
    }

    @Test
    void shouldFindAllCategories() {
        Category category1 = new Category();
        category1.setName("Fantasy");
        category1.setDescription("Fantasy books");
        Category category2 = new Category();
        category2.setName("Drama");
        category2.setDescription("Drama books");

        Category savedCategory = categoryRepository.save(category1);
        Category savedCategory2 = categoryRepository.save(category2);
        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).isNotEmpty();
        assertThat(categories).hasSize(2);
    }

    @Test
    void shouldDeleteCategoryById() {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        Category saved = categoryRepository.save(category);
        categoryRepository.deleteById(saved.getId());
        Optional<Category> found = categoryRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}