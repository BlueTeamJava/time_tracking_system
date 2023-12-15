package com.tproject.dao.impl;

import com.tproject.dao.TeamDao;
import com.tproject.entity.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class TeamDaoImpl implements TeamDao {

    private static TeamDaoImpl instance;

    public static TeamDaoImpl getInstance() {
        TeamDaoImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (TeamDaoImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TeamDaoImpl();
                }
            }
        }
        return localInstance;
    }




    private Team composeTeam(ResultSet resultSet) throws SQLException {
        Team team = new Team();
        team.setId(resultSet.getInt("id"));
        team.setTeamName(resultSet.getString("team_name"));
        return team;
    }

    @Override
   public Collection<Team> getAllTeams(){

        Collection<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM teams";
        Connection con =null;
        ResultSet resultSet=null;

        try{
            con = JdbcConnection.getInstance().getConnection();
            Statement statement = con.createStatement();
             resultSet = statement.executeQuery(query);

             while(resultSet.next()){
                 teams.add(composeTeam(resultSet));
             }

        }catch (SQLException e){
            System.out.println("While fetching All teams "+e.getMessage());
        }



        return teams;
    }

    @Override
    public boolean deleteTeam(int id) throws SQLException {

        String query ="Delete FROM teams Where id = ?";
        Connection con=null;
        ResultSet resultSet = null;

        try{
            con = JdbcConnection.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,id);
            int rowDeleted = statement.executeUpdate();
            System.out.println(rowDeleted+"Team deleted succesfully");
            return rowDeleted>0;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            if(con != null){
                con.close();
            }
        }
        return false;
    }
    @Override
    public boolean createTeam(Team team){
        String query = "INSERT INTO teams (team_name) VALUES (?)";
        Connection con =null;
        int rowsEffected;

        try{
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,team.getTeamName());
            rowsEffected = statement.executeUpdate();
            return rowsEffected >0;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }

}
