package pl.januaryewakasia.plxpodlasie.service.application;

import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.CategoryService;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.EmailService;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.ListingService;

@Service
public class CategoryDeletionService {
    private final CategoryService categoryService;
    private final ListingService listingService;
    private final EmailService emailService;

    public CategoryDeletionService(CategoryService categoryService, ListingService listingService, EmailService emailService) {
        this.categoryService = categoryService;
        this.listingService = listingService;
        this.emailService = emailService;
    }

    @Transactional
    public void deleteCategory(Long id) throws MessagingException {
        emailService.sendCategoryRemovalEmail(listingService.findByCategoryId(id));
        listingService.deleteByCategoryId(id);
        categoryService.deleteById(id);
    }
}
