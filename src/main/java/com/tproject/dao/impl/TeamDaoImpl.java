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
        team.setTeamLeadId(resultSet.getInt("team_lead"));
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
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
    public Team updateTeam(Team team) throws SQLException {
        String sql = "UPDATE teams SET team_name = ?, team_lead = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = JdbcConnection.getInstance().getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            conn.setAutoCommit(false);

            statement = conn.prepareStatement(sql);
            statement.setString(1, team.getTeamName());
            statement.setInt(2, team.getTeamLeadId());
            statement.setInt(3, team.getId());
            int rowsUpdated = statement.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeResources(conn, statement, null);
        }
        return team;
    }

    @Override
    public boolean deleteTeam(int id) throws SQLException {

        String query ="Delete FROM teams Where id = ?";
        Connection con=null;
        PreparedStatement statement=null;

        try{
            con = JdbcConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(query);
            statement.setInt(1,id);
            int rowDeleted = statement.executeUpdate();
            if (rowDeleted > 0) {
                resetUserTeamIds(con, id);
                con.commit();
                return true;
            }
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
        System.out.println(team.getTeamName());
        try{
            con = JdbcConnection.getInstance().getConnection();
            con.setAutoCommit(false);
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

    private void resetUserTeamIds(Connection con, int deletedTeamId) throws SQLException {
        String updateQuery = "UPDATE user_profile SET team_id = null where team_id = ?";
        PreparedStatement updateStatement = null;

        try {
            updateStatement = con.prepareStatement(updateQuery);
            updateStatement.setInt(1, deletedTeamId);
            int rowsUpdated = updateStatement.executeUpdate();
            System.out.println(rowsUpdated + " User profiles updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeResources(null, updateStatement);
        }
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
