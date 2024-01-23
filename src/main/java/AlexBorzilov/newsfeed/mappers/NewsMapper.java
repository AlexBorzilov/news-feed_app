package AlexBorzilov.newsfeed.mappers;

import AlexBorzilov.newsfeed.dto.GetNewsOutDto;
import AlexBorzilov.newsfeed.dto.NewsDto;
import AlexBorzilov.newsfeed.entity.NewsEntity;
import AlexBorzilov.newsfeed.entity.UserEntity;
import AlexBorzilov.newsfeed.repository.UserRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    @Mapping(target = "tags", ignore = true)
    NewsEntity NewsDtoToNewsEntity(NewsDto newsDto);

    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "userId", source = "user.id")
    GetNewsOutDto NewsEntityToGetNewsOutDto(NewsEntity news);
}
