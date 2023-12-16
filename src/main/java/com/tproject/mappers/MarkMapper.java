package com.tproject.mappers;

import com.tproject.dto.MarkDto;
import com.tproject.entity.Mark;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface MarkMapper {
    MarkDto markToDto(Mark mark);
    Mark dtoToMark(MarkDto markDto);
    Collection<MarkDto> markToDtoCollection(Collection<Mark> markCollection);
    Collection<Mark> dtoToMarkCollection(Collection<MarkDto> markDtoCollection);
}
