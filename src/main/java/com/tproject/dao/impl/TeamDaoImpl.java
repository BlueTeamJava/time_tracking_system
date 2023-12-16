package com.tproject.dao.impl;

import com.tproject.dao.TeamDao;
import com.tproject.entity.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger LOGGER =
            Logger.getLogger(TaskDaoImpl.class.getName());




    private Team composeTeam(ResultSet resultSet) throws SQLException {
        Team team = new Team();
        team.setId(resultSet.getInt("id"));
        team.setTeamName(resultSet.getString("team_name"));
        return team;
    }

    @Override
   public Collection<Team> getAllTeams() throws SQLException {

        Collection<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM teams";
        Connection con =null;
        Statement statement=null;
        ResultSet resultSet=null;


        try{
            con = JdbcConnection.getInstance().getConnection();
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            con.setAutoCommit(false);
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            con.commit();

             while(resultSet.next()){
                 teams.add(composeTeam(resultSet));
             }

        }catch (SQLException e){
            System.out.println("While fetching All teams "+e.getMessage());
        } finally {
            assert con != null;
            con.close();
        }
        return teams;
    }

    @Override
    public boolean deleteTeam(int id) throws SQLException {

        String query ="Delete FROM teams Where id = ?";
        Connection con=null;
        PreparedStatement statement=null;

        try{
            con = JdbcConnection.getInstance().getConnection();
            statement = con.prepareStatement(query);
            statement.setInt(1,id);
            int rowDeleted = statement.executeUpdate();
            con.commit();
            System.out.println(rowDeleted+"Team deleted succesfully");
            return rowDeleted>0;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeResources(con,statement);

        }
        return false;
    }
    @Override
    public boolean createTeam(Team team){
        String query = "INSERT INTO teams (team_name) VALUES (?)";
        Connection con =null;
        PreparedStatement statement=null;
        int rowsEffected;

        try{
            con = JdbcConnection.getInstance().getConnection();
            statement = con.prepareStatement(query);
            statement.setString(1,team.getTeamName());
            rowsEffected = statement.executeUpdate();
            con.commit();
            return rowsEffected >0;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeResources(con,statement);
        }

        return false;
    }


    public void closeResources(Connection connection, PreparedStatement statement){

        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing PreparedStatement: " + e.getMessage());
        }

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing Connection: " + e.getMessage());
        }

    }


    public void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing ResultSet: " + e.getMessage());
        }

        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing PreparedStatement: " + e.getMessage());
        }

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing Connection: " + e.getMessage());
        }
    }

}
