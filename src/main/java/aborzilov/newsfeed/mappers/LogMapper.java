package aborzilov.newsfeed.mappers;

import aborzilov.newsfeed.entity.LogEntity;
import aborzilov.newsfeed.model.LogModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LogMapper {
    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);
    LogEntity logToLogModelEntity(LogModel logModel);
}
