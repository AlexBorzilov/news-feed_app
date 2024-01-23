package AlexBorzilov.newsfeed.mappers;

import AlexBorzilov.newsfeed.dto.GetNewsOutDto;
import AlexBorzilov.newsfeed.dto.NewsDto;
import AlexBorzilov.newsfeed.entity.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {
    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    @Mapping(target = "tags", ignore = true)
    NewsEntity NewsDtoToNewsEntity(NewsDto newsDto);

    GetNewsOutDto NewsEntityToGetNewsOutDto(NewsEntity news);
}
