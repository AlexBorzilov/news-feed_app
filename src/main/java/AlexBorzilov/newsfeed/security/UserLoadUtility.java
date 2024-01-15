package AlexBorzilov.newsfeed.security;

import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.error.ErrorCodes;
import AlexBorzilov.newsfeed.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserLoadUtility implements UserDetailsService {
    final private UserRepo userRepo;
    @Bean
    public UserDetailsService userDetailsService() {
        return this::loadUserByUsername;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserEntity user = userRepo.findById(UUID.fromString(id)).orElseThrow(() -> new UsernameNotFoundException(
                String.format(ErrorCodes.USER_NOT_FOUND.getErrorMessage())
        ));
        org.springframework.security.core.userdetails.User userDetails = new User(
                user.getUsername(), user.getPassword(), user.getAuthorities());
        return userDetails;
    }
}
