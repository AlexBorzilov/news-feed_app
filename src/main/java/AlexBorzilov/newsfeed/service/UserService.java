package AlexBorzilov.newsfeed.service;


import AlexBorzilov.newsfeed.dto.PutUserDto;
import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.error.ErrorCodes;

import AlexBorzilov.newsfeed.error.NewsFeedException;
import AlexBorzilov.newsfeed.mappers.UserMapper;
import AlexBorzilov.newsfeed.repository.UserRepo;
import AlexBorzilov.newsfeed.response.CustomSuccessResponse;
import AlexBorzilov.newsfeed.response.PutUserDtoResponse;
import AlexBorzilov.newsfeed.view.PublicUserView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public CustomSuccessResponse<PutUserDtoResponse> replaceUser(PutUserDto userNewData){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserEntity user = userRepo.findByEmail(email).orElseThrow(()->
        new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage()));
        if(!userRepo.findByEmail(userNewData.getEmail()).isEmpty() && !user.getEmail().equals(userNewData.getEmail())){
            throw new NewsFeedException(ErrorCodes.USER_WITH_THIS_EMAIL_ALREADY_EXIST.getErrorMessage());
        }
        user.setEmail(userNewData.getEmail());
        user.setAvatar(userNewData.getAvatar());
        user.setName(userNewData.getName());
        user.setRole(userNewData.getRole());
        PutUserDtoResponse response = UserMapper.INSTANCE.UserEntityToPutUserDtoResponse(user);
        userRepo.save(user);
        return new CustomSuccessResponse<>(response);
    }

    public CustomSuccessResponse<PublicUserView> getUserInfoById(UUID id){
        PublicUserView view = UserMapper.INSTANCE.userEntityToPublicUserView(userRepo.findById(id).orElseThrow(()->
                new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage())));
        return new CustomSuccessResponse<>(view);
    }
    public CustomSuccessResponse<PublicUserView> getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        PublicUserView view = UserMapper.INSTANCE.userEntityToPublicUserView(userRepo.findByEmail(email).orElseThrow(()->
        new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage())));
        return new CustomSuccessResponse<>(view);
    }
}
