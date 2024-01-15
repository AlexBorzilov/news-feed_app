package AlexBorzilov.newsfeed.service;


import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.error.ErrorCodes;

import AlexBorzilov.newsfeed.mappers.UserMapper;
import AlexBorzilov.newsfeed.repository.UserRepo;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.view.PublicUserView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepo userRepo;

    public CustomSuccessResponse<List<PublicUserView>> getAllUsers() {
        List<PublicUserView> list = userRepo
                .findAll()
                .stream()
                .map(UserMapper.INSTANCE::userEntityToPublicUserView)
                .toList();
        return new CustomSuccessResponse<>(list);
    }
}
