package pl.januaryewakasia.plxpodlasie.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.januaryewakasia.plxpodlasie.exception.ExceptionHandler;
import pl.januaryewakasia.plxpodlasie.model.Category;
import pl.januaryewakasia.plxpodlasie.service.application.CategoryDeletionService;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryDeletionService categoryDeletionService;
    private final Logger logger = LogManager.getLogger(CategoryController.class);

    public CategoryController(CategoryService categoryService, CategoryDeletionService categoryDeletionService) {
        this.categoryService = categoryService;
        this.categoryDeletionService = categoryDeletionService;
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategories() {
        try {
            logger.info("Getting all categories");
            return ResponseEntity.ok(categoryService.findAll());
        } catch (Exception e) {
            logger.error("Unable to get all categories", e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @PostMapping("/admin/category")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        try {
            logger.info("Admin adding category");
            return ResponseEntity.ok(categoryService.save(category));
        } catch (Exception e) {
            logger.error("Unable to save category", e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            logger.info("Admin deleting category");
            categoryDeletionService.deleteCategory(id);
            return ResponseEntity.ok("Deleted category successfully");
        } catch (Exception e) {
            logger.error("Unable to delete category", e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @PutMapping("/admin/category")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        try {
            logger.info("Admin updating category");
            return ResponseEntity.ok(categoryService.updateCategory(category));
        } catch (Exception e) {
            logger.error("Unable to update category", e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }
}
