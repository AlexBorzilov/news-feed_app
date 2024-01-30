package aborzilov.newsfeed.repository;

import java.util.Optional;

import aborzilov.newsfeed.entity.TagEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTitle(String title);
    boolean existsByTitle(String string);
}
