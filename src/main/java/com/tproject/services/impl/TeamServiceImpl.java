package com.tproject.services.impl;

import com.tproject.dao.impl.TeamDaoImpl;
import com.tproject.dto.TeamDto;
import com.tproject.exception.CustomSQLException;
import com.tproject.mappers.TeamMapper;
import com.tproject.services.TeamService;
import org.mapstruct.factory.Mappers;

import java.sql.SQLException;
import java.util.Collection;

public class TeamServiceImpl implements TeamService {
    private TeamMapper mapper;
    private TeamDaoImpl teamDao;

    private static TeamServiceImpl instance;
    TeamServiceImpl(){
        teamDao = TeamDaoImpl.getInstance();
        mapper = Mappers.getMapper(TeamMapper.class);
    }


    public static TeamServiceImpl getInstance() {
        TeamServiceImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (TeamServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TeamServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Collection<TeamDto> getAllTeams(){
        try {
            return mapper.teamToDtoCollection(teamDao.getAllTeams());
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean deleteTeam(int teamId) {

        try{
            return teamDao.deleteTeam(teamId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public TeamDto updateTeam(TeamDto teamDto) {
        try {
            return mapper.teamToDto(teamDao.updateTeam(mapper.dtoToTeam(teamDto)));
        } catch (CustomSQLException e) {
            throw e;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createTeam(TeamDto team) {
            return teamDao.createTeam(mapper.dtoToTeam(team));
    }
}
