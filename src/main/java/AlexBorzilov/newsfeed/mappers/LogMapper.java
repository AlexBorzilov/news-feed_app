package AlexBorzilov.newsfeed.mappers;

import AlexBorzilov.newsfeed.entity.LogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LogMapper {
    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);
    LogEntity logToLogEntity(String log);
}
