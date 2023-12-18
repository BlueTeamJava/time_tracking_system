package com.tproject.dao.impl;

import com.tproject.dao.UserProfileDao;
import com.tproject.entity.UserProfile;
import com.tproject.exception.CustomSQLException;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProfileDaoImpl implements UserProfileDao {
    private static UserProfileDaoImpl instance;

    public static UserProfileDaoImpl getInstance() {
        UserProfileDaoImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (UserProfileDaoImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserProfileDaoImpl();
                }
            }
        }
        return localInstance;
    }

    private static final Logger LOGGER =
            Logger.getLogger(UserProfileDaoImpl.class.getName());

    private UserProfile composeUserProfile(ResultSet resultSet) throws SQLException {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(resultSet.getInt("user_id"));
        userProfile.setTeamId(resultSet.getInt("team_id"));
        userProfile.setName(resultSet.getString("name"));
        return userProfile;
    }

    //get by user id
    @Override
    public Optional<UserProfile> getUserProfile(int id) throws CustomSQLException {
        Optional<UserProfile> userProfileOpt = Optional.empty();
        String sql = "SELECT * FROM user_profile WHERE user_id = ?";
        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userProfileOpt = Optional.of(composeUserProfile(resultSet));

                LOGGER.log(Level.INFO, "Found {0} in database", userProfileOpt.get());
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("findUserProfile - SQL Exception");
        }
        return userProfileOpt;
    }

    @Override
    public Collection<UserProfile> getAllUserProfiles() throws CustomSQLException {
        Collection<UserProfile> userProfiles = new ArrayList<>();
        String sql = "SELECT * FROM user_profile";
        try (Connection conn = JdbcConnection.getInstance().getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                UserProfile userProfile = composeUserProfile(resultSet);
                userProfiles.add(userProfile);
            }

            LOGGER.log(Level.INFO, "Found {0} profiles in the database", userProfiles.size());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("getAllUserProfiles - SQL Exception");
        }
        return userProfiles;
    }


    @Override
    public Optional<Integer> saveUserProfile(UserProfile userProfile) {
        String message = "The UserProfile to be added should not be null";
        UserProfile nonNullUserProfile = Objects.requireNonNull(userProfile, message);
        String sql = "INSERT INTO "
                + "user_profile(team_id, name) "
                + "VALUES(?, ?)";
        Optional<Integer> generatedId = Optional.empty();

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, nonNullUserProfile.getTeamId());
            statement.setString(2, nonNullUserProfile.getName());

            int numberOfInsertedRows = statement.executeUpdate();

            if (numberOfInsertedRows > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = Optional.of(resultSet.getInt(1));
                    }
                }
            }

            LOGGER.log(Level.INFO, "{0} created successfully? {1}",
                    new Object[]{nonNullUserProfile, (numberOfInsertedRows > 0)});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("saveUserProfile - SQL Exception");
        }

        return generatedId;
    }

    @Override
    public UserProfile updateUserProfile(UserProfile userProfile) {
        String message = "The user profile to be updated should not be null";
        UserProfile nonNullUserProfile = Objects.requireNonNull(userProfile, message);
        String sql = "UPDATE user_profile SET team_id = ?, name = ? WHERE user_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            System.out.println(nonNullUserProfile);
            statement.setInt(1, nonNullUserProfile.getTeamId());
            statement.setString(2, nonNullUserProfile.getName());
            statement.setInt(3, nonNullUserProfile.getId());

            int numberOfUpdatedRows = statement.executeUpdate();

            LOGGER.log(Level.INFO, "Was the user profile updated successfully? {0}",
                    numberOfUpdatedRows > 0);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("updateUserProfile - SQL Exception");
        }
        return userProfile;
    }

    @Override
    public boolean deleteUserProfile(int id) {
        String sql = "DELETE FROM user_profile WHERE id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);

            int numberOfDeletedRows = statement.executeUpdate();

            LOGGER.log(Level.INFO, "Was the user profile deleted successfully? {0}",
                    numberOfDeletedRows > 0);

            return numberOfDeletedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("deleteUserProfile - SQL Exception");
        }
    }
}
