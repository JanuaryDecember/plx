package pl.januaryewakasia.plxpodlasie.service.infrastructure;

import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.januaryewakasia.plxpodlasie.Role;
import pl.januaryewakasia.plxpodlasie.dto.ListingDto;
import pl.januaryewakasia.plxpodlasie.exception.NotFoundException;
import pl.januaryewakasia.plxpodlasie.exception.UnauthorizedException;
import pl.januaryewakasia.plxpodlasie.mapper.ListingMapper;
import pl.januaryewakasia.plxpodlasie.model.Category;
import pl.januaryewakasia.plxpodlasie.model.Listing;
import pl.januaryewakasia.plxpodlasie.model.User;
import pl.januaryewakasia.plxpodlasie.repository.ListingRepository;
import pl.januaryewakasia.plxpodlasie.request.ListingRequest;

import java.util.Date;
import java.util.List;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final UserService userService;
    private final EmailService emailService;

    public ListingService(ListingRepository listingRepository, UserService userService,
                          EmailService emailService) {
        this.listingRepository = listingRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    public void approveListing(Long id)
            throws UnauthorizedException, NotFoundException, AuthenticationServiceException, MessagingException {
        User user = userService.getCurrentUser();
        if (user.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(Role.ADMIN.name()))) {
            throw new UnauthorizedException("You don't have permission to approve listings!");
        }
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Listing with id " + id + " not found"));
        listing.setApproved(true);
        listing = listingRepository.save(listing);
        emailService.sendEmail(listing.getUser().getEmail(), "Listing approved",
                "Hello dear " + listing.getUser().getUsername() + "!\nYour listing with id: " + listing.getId()
                        + " got approved! Have a nice day!\nPS. listing will expire: " + listing.getExpiryDate());
    }

    public List<Listing> findAll() {
        return listingRepository.findAll();
    }

    public Listing findById(Long id) throws NotFoundException {
        return listingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Listing with id " + id + " not found"));
    }

    public List<Listing> findAllByUser() throws AuthenticationServiceException {
        User user = userService.getCurrentUser();
        return listingRepository.findByUserId(user.getId());
    }

    public Listing save(ListingRequest listingRequest, Category category) throws AuthenticationServiceException, NotFoundException {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new AuthenticationServiceException("User not logged in");
        }
        Listing listing = new Listing();
        listing.setName(listingRequest.getName());
        listing.setUser(user);
        listing.setCategory(category);
        listing.setDescription(listingRequest.getDescription());
        listing.setPrice(listingRequest.getPrice());
        listing.setImage(listingRequest.getImage());
        listing.setCategoryId(listingRequest.getCategoryId());
        listing.setExpiryDate(new Date(System.currentTimeMillis() + 1000 * 60 * 5));

        return listingRepository.save(listing);
    }

    public void deleteById(Long id) throws AuthenticationServiceException, NotFoundException {
        User user = userService.getCurrentUser();
        if (!listingRepository.existsByIdAndUserId(id, user.getId()))
            throw new NotFoundException("Listing with id " + id + " not found");
        listingRepository.deleteById(id);
    }

    @Transactional
    public void deleteByCategoryId(Long categoryId) throws AuthenticationServiceException, NotFoundException {
        listingRepository.deleteByCategoryId(categoryId);
    }

    public Listing update(ListingDto listingDto) {
        User user = userService.getCurrentUser();

        boolean found = listingRepository.existsByIdAndUserId(listingDto.id(), listingDto.userId());

        if (!found) {
            throw new NotFoundException("Listing with id " + listingDto.id() + " not found");
        }

        if (!listingDto.userId().equals(user.getId()))
            throw new UnauthorizedException("You don't have permission to update listing!");

        return listingRepository.save(ListingMapper.toListing(listingDto, user));
    }

    public void forceDeleteById(Long id) {
        listingRepository.deleteById(id);
    }

    public List<Listing> findByCategoryId(Long id) {
        return listingRepository.findByCategoryId(id);
    }

    public List<Listing> findAllByExpiryDateBefore(Date date) {
        return listingRepository.findAllByExpiryDateBefore(date);
    }
}
