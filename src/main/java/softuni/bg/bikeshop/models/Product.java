package softuni.bg.bikeshop.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product extends BaseEntity{
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "is_favourite",nullable = false)
    private boolean isFavourite;
    @Column(nullable = false)
    private LocalDateTime createdOn;
    @ManyToOne(optional = false)
    private User seller;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Picture> pictures;
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;
    @ManyToMany
    private Set<User> isFavouriteOf;
    public Product() {
        pictures = new ArrayList<>();
        isFavourite = false;
        reviews = new ArrayList<>();
        isFavouriteOf = new HashSet<>();
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }


    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<User> getIsFavouriteOf() {
        return isFavouriteOf;
    }

    public void setIsFavouriteOf(Set<User> isFavouriteOf) {
        this.isFavouriteOf = isFavouriteOf;
    }
}
