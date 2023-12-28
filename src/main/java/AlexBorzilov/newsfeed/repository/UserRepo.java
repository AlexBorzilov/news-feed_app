package AlexBorzilov.newsfeed.repository;

import AlexBorzilov.newsfeed.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

}
