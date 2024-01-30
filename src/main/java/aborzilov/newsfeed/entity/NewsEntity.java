package aborzilov.newsfeed.entity;

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
    private Long id;
    private String description;
    private String image;
    private String title;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "news_tags",
            joinColumns = @JoinColumn(name = "news_entity_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id"))
    private Set<TagEntity> tags;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
