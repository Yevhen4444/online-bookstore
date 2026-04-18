package com.example.bookstore.repository;

import com.example.bookstore.entity.Category;
import com.example.bookstore.util.TestDataHelper;
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
        Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category savedCategory = categoryRepository.save(category);
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Fantasy");
        assertThat(foundCategory.get().getDescription()).isEqualTo("Fantasy books");
    }

    @Test
    void shouldFindAllCategories() {
        Category firstCategory = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category secondCategory = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category savedFirstCategory = categoryRepository.save(firstCategory);
        Category savedSecondCategory = categoryRepository.save(secondCategory);
        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).isNotEmpty();
        assertThat(categories).hasSize(2);
    }

    @Test
    void shouldDeleteCategoryById() {
        Category category = TestDataHelper.createCategory("Fantasy", "Fantasy books");
        Category saved = categoryRepository.save(category);
        categoryRepository.deleteById(saved.getId());
        Optional<Category> found = categoryRepository.findById(saved.getId());

        assertThat(found).isEmpty();
    }
}
