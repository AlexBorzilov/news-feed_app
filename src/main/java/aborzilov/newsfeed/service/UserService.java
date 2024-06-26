package aborzilov.newsfeed.service;

import java.util.List;
import java.util.UUID;

import aborzilov.newsfeed.dto.PutUserDto;
import aborzilov.newsfeed.entity.UserEntity;
import aborzilov.newsfeed.error.ErrorCodes;
import aborzilov.newsfeed.error.NewsFeedException;
import aborzilov.newsfeed.mappers.UserMapper;
import aborzilov.newsfeed.repository.UserRepo;
import aborzilov.newsfeed.response.BaseSuccessResponse;
import aborzilov.newsfeed.response.CustomSuccessResponse;
import aborzilov.newsfeed.response.PutUserDtoResponse;
import aborzilov.newsfeed.view.PublicUserView;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;


    public CustomSuccessResponse<List<PublicUserView>> getAllUsers() {
        List<PublicUserView> list = userRepo
                .findAll()
                .stream()
                .map(UserMapper.INSTANCE::userEntityToPublicUserView)
                .toList();
        return new CustomSuccessResponse<>(list);
    }

    @Transactional
    public CustomSuccessResponse<PutUserDtoResponse> replaceUser(PutUserDto userNewData, String id) {
        UserEntity user = userRepo.findById(UUID.fromString(id)).orElseThrow(() ->
                new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage()));
        if (!userRepo.findByEmail(userNewData.getEmail()).isEmpty() && !user.getEmail().equals(userNewData.getEmail())) {
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

    public CustomSuccessResponse<PublicUserView> getUserInfoById(UUID id) {
        PublicUserView view = UserMapper.INSTANCE.userEntityToPublicUserView(userRepo.findById(id).orElseThrow(() ->
                new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage())));
        return new CustomSuccessResponse<>(view);
    }

    public CustomSuccessResponse<PublicUserView> getUserInfo(String id) {
        PublicUserView view = UserMapper.INSTANCE.userEntityToPublicUserView(userRepo.findById(UUID.fromString(id)).orElseThrow(() ->
                new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage())));
        return new CustomSuccessResponse<>(view);
    }

    public BaseSuccessResponse deleteUser(String id) {
        userRepo.delete(userRepo.findById(UUID.fromString(id)).orElseThrow(() ->
                new NewsFeedException(ErrorCodes.USER_NOT_FOUND.getErrorMessage())));
        return new BaseSuccessResponse();
    }
}
