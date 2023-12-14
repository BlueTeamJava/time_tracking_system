package com.tproject.dao;

import com.tproject.entity.Mark;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public interface MarkDao {
    Optional<Mark> findMarkById(int id) throws SQLException;
    Optional<Mark> findMarkByUserDate(int userId, Date date) throws SQLException;
    Collection<Mark> getAllMarksFromUser(int userId);
    Optional<Integer> saveMark(Mark mark);
    Mark updateMark(Mark mark);
    boolean deleteTMark(int id);
}
