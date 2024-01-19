package AlexBorzilov.newsfeed.repository;

import AlexBorzilov.newsfeed.entity.NewsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepo extends JpaRepository<NewsEntity, Long> {
}
