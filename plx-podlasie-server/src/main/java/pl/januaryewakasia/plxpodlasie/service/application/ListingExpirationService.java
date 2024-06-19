package pl.januaryewakasia.plxpodlasie.service.application;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.januaryewakasia.plxpodlasie.model.Listing;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.EmailService;
import pl.januaryewakasia.plxpodlasie.service.infrastructure.ListingService;

import java.util.Date;
import java.util.List;

@Service
public class ListingExpirationService {
    private final ListingService listingService;
    private final EmailService emailService;

    public ListingExpirationService(ListingService listingService, EmailService emailService) {
        this.listingService = listingService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeExpiredListings() throws MessagingException {
        List<Listing> expiredListings = listingService.findAllByExpiryDateBefore(new Date(System.currentTimeMillis()));
        for (Listing listing : expiredListings) {
            String emailContent = "Hello " + listing.getUser().getUsername() + ",\n\nYour listing with ID " + listing.getId() + " has expired.";
            emailService.sendEmail(listing.getUser().getEmail(), "Listing Expired", emailContent);
            listingService.deleteById(listing.getId());
        }
    }
}
