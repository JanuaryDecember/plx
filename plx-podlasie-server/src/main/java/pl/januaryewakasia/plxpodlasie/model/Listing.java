package pl.januaryewakasia.plxpodlasie.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Listing implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(length = 1000, nullable = false)
    private String description;
    @Column(length = 100)
    private String image;
    @Column(precision = 20, scale = 2)
    private BigDecimal price;
    private boolean approved;
    @Column(name = "expiry_date")
    private Date expiryDate;
    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public Listing() {
    }

    public Listing(User user, String name, String description, String image, BigDecimal price, Long categoryId) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.categoryId = categoryId;
        this.expiryDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        this.approved = false;
    }

    public Listing(Long id, User user, String name, String description, String image, BigDecimal price, Long categoryId,
            boolean approved) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.categoryId = categoryId;
        this.approved = approved;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", approved=" + approved +
                ", expiryDate=" + expiryDate +
                ", categoryId=" + categoryId +
                ", category=" + category +
                '}';
    }
}
