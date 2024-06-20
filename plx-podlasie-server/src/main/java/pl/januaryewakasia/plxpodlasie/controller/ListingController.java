package pl.januaryewakasia.plxpodlasie.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.januaryewakasia.plxpodlasie.dto.ListingDto;
import pl.januaryewakasia.plxpodlasie.exception.ExceptionHandler;
import pl.januaryewakasia.plxpodlasie.mapper.ListingMapper;
import pl.januaryewakasia.plxpodlasie.model.Category;
import pl.januaryewakasia.plxpodlasie.model.Listing;
import pl.januaryewakasia.plxpodlasie.request.ListingRequest;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.CategoryService;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.ListingService;

@RestController
@RequestMapping("/api/v1")
public class ListingController {
    private final ListingService listingService;
    private final CategoryService categoryService;
    private final Logger logger = LogManager.getLogger(ListingController.class);

    public ListingController(ListingService listingService, CategoryService categoryService) {
        this.categoryService = categoryService;
        this.listingService = listingService;
    }

    @GetMapping("/listing")
    public ResponseEntity<?> getApprovedListings() {
        try {
            logger.info("Getting approved listings");
            return ResponseEntity.ok(
                    listingService.findAll().stream().filter(Listing::isApproved).map(ListingMapper::toDto).toList());
        } catch (Exception e) {
            logger.error("Unable to get approved listings", e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @GetMapping("/premium/listing")
    public ResponseEntity<?> getListingsUsersListings() {
        try {
            logger.info("User getting listings");
            return ResponseEntity.ok(listingService.findAllByUser().stream().map(ListingMapper::toDto).toList());
        } catch (Exception e) {
            logger.error("Unable to get listings", e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @GetMapping("/admin/listing")
    public ResponseEntity<?> getAllListings() {
        try {
            logger.info("Admin getting listings");
            return ResponseEntity.ok(listingService.findAll().stream().map(ListingMapper::toDto).toList());
        } catch (Exception e) {
            logger.error("Unable to get listings", e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @GetMapping("/listing/{id}")
    public ResponseEntity<?> getListingById(@PathVariable Long id) {
        try {
            logger.info("Getting listing with id {}", id);
            return ResponseEntity.ok(ListingMapper.toDto(listingService.findById(id)));
        } catch (Exception e) {
            logger.error("Unable to get listing with id {}", id, e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @PostMapping("/premium/listing")
    public ResponseEntity<?> addListing(@RequestBody ListingRequest listingRequest) {
        try {
            logger.info("Adding listing {}", listingRequest);
            Category category = categoryService.findById(listingRequest.getCategoryId());
            return ResponseEntity.ok(ListingMapper.toDto(listingService.save(listingRequest, category)));
        } catch (Exception e) {
            logger.error("Unable to save listing {}", listingRequest, e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @PutMapping("/premium/listing")
    public ResponseEntity<?> updateListing(@RequestBody ListingDto listingDto) {
        try {
            logger.info("Updating listing {}", listingDto);
            return ResponseEntity.ok(ListingMapper.toDto(listingService.update(listingDto)));
        } catch (Exception e) {
            logger.error("Unable to update listing {}", listingDto, e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @PostMapping("/admin/listing/{id}")
    public ResponseEntity<?> approveListing(@PathVariable Long id) {
        try {
            logger.info("Approving listing with id {}", id);
            listingService.approveListing(id);
            return ResponseEntity.ok("Successfully approved listing!");
        } catch (Exception e) {
            logger.error("Unable to approve listing {}", id, e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @DeleteMapping("/premium/listing/{id}")
    public ResponseEntity<?> deleteListing(@PathVariable Long id) {
        try {
            logger.info("Deleting listing with id {}", id);
            listingService.deleteById(id);
            return ResponseEntity.ok("Deleted listing with id " + id);
        } catch (Exception e) {
            logger.error("Unable to delete listing {}", id, e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

    @DeleteMapping("/admin/listing/{id}")
    public ResponseEntity<?> adminDeleteListing(@PathVariable Long id) {
        try {
            logger.info("Admin deleting listing with id {}", id);
            listingService.forceDeleteById(id);
            return ResponseEntity.ok("Deleted listing with id " + id);
        } catch (Exception e) {
            logger.error("Unable to delete listing {}", id, e);
            return ExceptionHandler.exceptionHandler(e);
        }
    }

}
