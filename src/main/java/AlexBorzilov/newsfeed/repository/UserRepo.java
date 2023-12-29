package AlexBorzilov.newsfeed.repository;

import AlexBorzilov.newsfeed.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    @Query(value = "from UserEntity u where u.email = ?1")
    public UserEntity findByEmail(String findEmail);
}
