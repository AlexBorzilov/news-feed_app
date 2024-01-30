package aborzilov.newsfeed.repository;

import java.util.Optional;
import java.util.UUID;

import aborzilov.newsfeed.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, UUID> {

    @Query("from UserEntity u where u.email = ?1")
    Optional<UserEntity> findByEmail(String email);
}
