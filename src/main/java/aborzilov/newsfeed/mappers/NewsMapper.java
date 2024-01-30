package aborzilov.newsfeed.mappers;

import aborzilov.newsfeed.dto.GetNewsOutDto;
import aborzilov.newsfeed.dto.NewsDto;
import aborzilov.newsfeed.entity.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    @Mapping(target = "tags", ignore = true)
    NewsEntity NewsDtoToNewsEntity(NewsDto newsDto);

    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "userId", source = "user.id")
    GetNewsOutDto NewsEntityToGetNewsOutDto(NewsEntity news);
}
