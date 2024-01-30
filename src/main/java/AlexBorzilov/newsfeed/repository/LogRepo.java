package AlexBorzilov.newsfeed.repository;

import AlexBorzilov.newsfeed.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LogRepo extends JpaRepository<LogEntity, Long> {

}
