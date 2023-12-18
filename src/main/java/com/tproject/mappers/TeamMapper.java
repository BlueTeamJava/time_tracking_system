package com.tproject.mappers;

import com.tproject.dto.TeamDto;
import com.tproject.entity.Team;
import org.mapstruct.Mapper;

import java.util.Collection;
@Mapper
public interface TeamMapper {
    TeamDto teamToDto(Team team);

    Team dtoToTeam(TeamDto teamDto);
    Collection<TeamDto> teamToDtoCollection(Collection<Team> teamCollection);
}
