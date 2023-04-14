package ru.skypro.homework.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private BigDecimal price;
    private String title;
    @OneToOne
    private Image image;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;
    @OneToMany(mappedBy = "ads")
    private List<Comment> comments;

    public Ads() {
    }

    public Ads(String description, BigDecimal price, String title, Image image, User user) {
        this.description = description;
        this.price = price;
        this.title = title;
        this.image = image;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Ads{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", image=" + image +
                ", user=" + user +
                '}';
    }
}
