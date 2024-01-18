package AlexBorzilov.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "news")
@Getter
@Setter
public class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String description;
    String image;
    String title;
    @ManyToMany
    Set<TagEntity> tags;
    @ManyToOne
    UserEntity user;
}
