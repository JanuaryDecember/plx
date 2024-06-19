package pl.januaryewakasia.plxpodlasie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.januaryewakasia.plxpodlasie.model.Listing;

import java.util.Date;
import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    @Query("SELECT l FROM Listing l WHERE l.category.id = ?1")
    List<Listing> findByCategoryId(Long categoryId);

    List<Listing> findByUserId(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    List<Listing> findAllByExpiryDateBefore(Date date);

    @Transactional
    void deleteByCategoryId(Long categoryId);
}
