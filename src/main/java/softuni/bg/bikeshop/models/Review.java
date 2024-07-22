package softuni.bg.bikeshop.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review extends BaseEntity{
    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated() {
        return createdOn;
    }

    public void setCreated(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
