package AlexBorzilov.newsfeed.repository;

import AlexBorzilov.newsfeed.entity.NewsEntity;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface NewsRepo extends JpaRepository<NewsEntity, Long>, JpaSpecificationExecutor<NewsEntity> {

    @Query(value = "from NewsEntity n where n.title ilike %?1%")
    Optional<List<NewsEntity>> findByTitleLike(String title);

    @Query(value = "from NewsEntity n where n.user.name ilike ?1")
    Optional<List<NewsEntity>> findByAuthor(String author);

    @Query(value = "from TagEntity t where t.title in :tags")
    Optional<List<NewsEntity>> findByTagsLike(@Param("tags") Set<String> tags);

    @Override
    @EntityGraph(attributePaths = {"tags", "user"})
    List<NewsEntity> findAll(@Nullable Specification spec);

}
