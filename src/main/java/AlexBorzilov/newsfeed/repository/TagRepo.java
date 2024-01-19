package AlexBorzilov.newsfeed.repository;

import AlexBorzilov.newsfeed.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepo extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTitle(String title);
    boolean existsByTitle(String string);
}
