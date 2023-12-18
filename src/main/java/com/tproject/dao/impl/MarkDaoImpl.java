package com.tproject.dao.impl;

import com.tproject.dao.MarkDao;
import com.tproject.entity.Mark;
import com.tproject.exception.CustomSQLException;

import java.util.*;
import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarkDaoImpl implements MarkDao {

    private static MarkDaoImpl instance;

    public static MarkDaoImpl getInstance() {
        MarkDaoImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (MarkDaoImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MarkDaoImpl();
                }
            }
        }
        return localInstance;
    }

    private static final Logger LOGGER =
            Logger.getLogger(MarkDaoImpl.class.getName());

    private Mark composeMark(ResultSet resultSet) throws SQLException {
        Mark mark = new Mark();
        mark.setId(resultSet.getInt("id"));
        mark.setUserProfileId(resultSet.getInt("user_id"));
        mark.setScore(resultSet.getFloat("score"));
        mark.setDate(resultSet.getDate("date"));
        mark.setDescription(resultSet.getString("description"));
        return mark;
    }


    @Override
    public Optional<Mark> findMarkById(int id) throws CustomSQLException {
        Optional<Mark> markOpt = Optional.empty();
        String sql = "SELECT * FROM marks WHERE id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                markOpt = Optional.of(composeMark(resultSet));

                LOGGER.log(Level.INFO, "Found {0} in database", markOpt.get());
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("findMark - SQL Exception");
        }
        return markOpt;
    }

    @Override
    public Optional<Mark> findMarkByUserDate(int userId, Date date) throws CustomSQLException {
        Optional<Mark> markOpt = Optional.empty();
        String sql = "SELECT * FROM marks WHERE user_id = ? AND date = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setDate(2, new java.sql.Date(date.getTime()));    //check

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                markOpt = Optional.of(composeMark(resultSet));

                LOGGER.log(Level.INFO, "Found {0} in database", markOpt.get());
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("findMark - SQL Exception");
        }
        return markOpt;
    }

    @Override
    public Collection<Mark> getAllMarksByDate(Date date) {
        Collection<Mark> marks = new ArrayList<>();
        String sql = "SELECT * FROM marks WHERE date = ? ORDER BY id";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            statement.setDate(1, sqlDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Mark mark = composeMark(resultSet);

                marks.add(mark);

                LOGGER.log(Level.INFO, "Found {0} in database", mark);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("getAllMarksByDate - SQL Exception");
        }

        return marks;
    }

    @Override
    public Map<Integer, Integer> getTotalMarksByUsers() throws CustomSQLException{
        Map<Integer, Integer> totalMarksByUser = new HashMap<>();
        String sql = "SELECT user_id, SUM(score) AS totalMarks FROM marks GROUP BY user_id";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int totalMarks = resultSet.getInt("totalMarks");
                totalMarksByUser.put(userId, totalMarks);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("getTotalMarksByUserId - SQL Exception");
        }

        return totalMarksByUser;
    }
    @Override
    public Collection<Mark> getAllMarksFromUser(int userId) {
        Collection<Mark> marks = new ArrayList<>();
        String sql = "SELECT * FROM marks ORDER BY id WHERE user_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Mark mark = composeMark(resultSet);

                marks.add(mark);

                LOGGER.log(Level.INFO, "Found {0} in database", mark);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("getAll - SQL Exception");
        }

        return marks;
    }

    @Override
    public Optional<Integer> saveMark(Mark mark) {
        String message = "The User to be added should not be null";
        Mark nonNullMark = Objects.requireNonNull(mark, message);
        String sql = "INSERT INTO "
                + "marks(user_id, score, date, description) "
                + "VALUES(?, ?, ?, ?)";
        Optional<Integer> generatedId = Optional.empty();

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, nonNullMark.getUserProfileId());
            statement.setFloat(2, nonNullMark.getScore());
            statement.setDate(3, new java.sql.Date(nonNullMark.getDate().getTime()));
            statement.setString(4, nonNullMark.getDescription());

            int numberOfInsertedRows = statement.executeUpdate();

            if (numberOfInsertedRows > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = Optional.of(resultSet.getInt(1));
                    }
                }
            }

            LOGGER.log(Level.INFO, "{0} created successfully? {1}",
                    new Object[]{nonNullMark, (numberOfInsertedRows > 0)});
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("saveMark - SQL Exception");
        }

        return generatedId;
    }

    @Override
    public Mark updateMark(Mark mark) {
        String message = "The mark to be updated should not be null";
        Mark nonNullMark = Objects.requireNonNull(mark, message);
        String sql = "UPDATE marks "
                + "SET "
                + "user_id = ?, "
                + "score = ?, "
                + "date = ?, "
                + "WHERE "
                + "user_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {


            statement.setInt(1, nonNullMark.getUserProfileId());
            statement.setFloat(2, nonNullMark.getScore());
            statement.setDate(3, new java.sql.Date(nonNullMark.getDate().getTime()));   //check
            statement.setInt(4, nonNullMark.getId());

            int numberOfUpdatedRows = statement.executeUpdate();

            LOGGER.log(Level.INFO, "Was the mark updated successfully? {0}",
                    numberOfUpdatedRows > 0);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("updateMark - SQL Exception");
        }
        return mark;
    }

    @Override
    public boolean deleteMark(int id) {
        String sql = "DELETE FROM marks WHERE id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);

            int numberOfDeletedRows = statement.executeUpdate();

            LOGGER.log(Level.INFO, "Was the mark deleted successfully? {0}",
                    numberOfDeletedRows > 0);

            return numberOfDeletedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new CustomSQLException("deleteMark - SQL Exception");
        }
    }
}
