package com.tproject.dao;

import com.tproject.entity.Team;

import java.sql.SQLException;
import java.util.Collection;

public interface TeamDao {



    Collection<Team> getAllTeams();

    boolean deleteTeam(int teamId) throws SQLException;

    boolean createTeam(Team team);

}
