package com.tproject.services;
import com.tproject.dto.TeamDto;

import java.util.Collection;

public interface TeamService {

    Collection<TeamDto> getAllTeams();
    boolean deleteTeam(int teamId);
    TeamDto updateTeam(final TeamDto teamDto);

    boolean createTeam(final TeamDto team);


}
