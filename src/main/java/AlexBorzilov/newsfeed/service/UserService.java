package AlexBorzilov.newsfeed.service;


import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.error.ErrorCodes;
import AlexBorzilov.newsfeed.error.NewsFeedException;

import AlexBorzilov.newsfeed.mappers.UserMapper;
import AlexBorzilov.newsfeed.repository.UserRepo;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.view.PublicUserView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;

    public CustomSuccessResponse<List<PublicUserView>> all() {
        List<PublicUserView> list = userRepo
                .findAll()
                .stream()
                .map(UserMapper.INSTANCE::userEntityToPublicUserView)
                .toList();
        return new CustomSuccessResponse<>(list);
    }

    public UserDetailsService userDetailsService(){
        return this::loadUserByUsername;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
            UserEntity user = userRepo.findById(UUID.fromString(id)).orElseThrow(()->new UsernameNotFoundException(
                    String.format(ErrorCodes.USER_NOT_FOUND.getErrorMessage())
            ));
            org.springframework.security.core.userdetails.User userDetails = new User(
                    user.getUsername(), user.getPassword(), user.getAuthorities());
            return userDetails;
    }
//
//    public CustomSuccessResponse<PutUserDtoResponse> replaceUser (PutUserDto userNewData){
//
//    }
}
