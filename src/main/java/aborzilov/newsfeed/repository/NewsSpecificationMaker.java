package aborzilov.newsfeed.repository;

import java.util.Set;
import java.util.stream.Collectors;

import aborzilov.newsfeed.entity.NewsEntity;
import jakarta.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;

import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class NewsSpecificationMaker {

    public static Specification<NewsEntity> makeSpec(String author, String keyWords, Set<String> tags) {
        return Specification.allOf(makeAuthorSpec(author), makeTagSpec(tags), makeKeywordSpec(keyWords));
    }

    private static Specification<NewsEntity> makeAuthorSpec(String author) {
        if (author == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("user").get("name")), "%" + author.toLowerCase() + "%");
    }

    private static Specification<NewsEntity> makeKeywordSpec(String keyWords) {
        if (keyWords == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                                "%" + keyWords.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                                "%" + keyWords.toLowerCase() + "%"));
    }

    private static Specification<NewsEntity> makeTagSpec(Set<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Specification.where(null);
        }
        else {
            return (root, query, criteriaBuilder) -> {
                var join = root.join("tags", JoinType.LEFT);
                return criteriaBuilder.lower(join.get("title"))
                        .in(tags.stream().map(String::toLowerCase).collect(Collectors.toSet()));
            };
        }
    }
}
