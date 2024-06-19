package pl.januaryewakasia.plxpodlasie.dto;

import java.math.BigDecimal;

public record ListingDto(Long id, Long userId, String name, String description, String image, BigDecimal price,
                         Long categoryId, boolean approved) {
    @Override
    public String toString() {
        return "ListingDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                ", approved=" + approved +
                '}';
    }
}
