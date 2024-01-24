package AlexBorzilov.newsfeed.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import AlexBorzilov.newsfeed.entity.NewsEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepo extends JpaRepository<NewsEntity, Long>, JpaSpecificationExecutor<NewsEntity> {

    @Query("from NewsEntity n where n.title ilike %?1%")
    Optional<List<NewsEntity>> findByTitleLike(String title);

    @Query("from NewsEntity n where n.user.name ilike ?1")
    Optional<List<NewsEntity>> findByAuthor(String author);

    @Query("from TagEntity t where t.title in :tags")
    Optional<List<NewsEntity>> findByTagsLike(@Param("tags") Set<String> tags);

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"tags", "user"})
    Page<NewsEntity> findAll(@Nullable Specification spec, @Nonnull Pageable pageable);

    void deleteById(long id);
}
