package AlexBorzilov.newsfeed.repository;
import AlexBorzilov.newsfeed.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, UUID> {

    @Query(value = "from UserEntity u where u.email = ?1")
    Optional<UserEntity> findByEmail(String email);

}
