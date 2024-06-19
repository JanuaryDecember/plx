package pl.januaryewakasia.plxpodlasie.mapper;

import pl.januaryewakasia.plxpodlasie.dto.ListingDto;
import pl.januaryewakasia.plxpodlasie.model.Listing;
import pl.januaryewakasia.plxpodlasie.model.User;

public class ListingMapper {
    public static ListingDto toDto(Listing listing) {
        return new ListingDto(
                listing.getId(),
                listing.getUser().getId(),
                listing.getName(),
                listing.getDescription(),
                listing.getImage(),
                listing.getPrice(),
                listing.getCategoryId(),
                listing.isApproved());
    }

    public static Listing toListing(ListingDto listingDto, User user) {
        return new Listing(
                listingDto.id(),
                user,
                listingDto.name(),
                listingDto.description(),
                listingDto.image(),
                listingDto.price(),
                listingDto.categoryId(),
                listingDto.approved());
    }
}
