package com.tproject.dao;

import com.tproject.entity.Mark;
import com.tproject.exception.CustomSQLException;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public interface MarkDao {
    Optional<Mark> findMarkById(int id) throws CustomSQLException;
    Optional<Mark> findMarkByUserDate(int userId, Date date) throws CustomSQLException;
    Collection<Mark> getAllMarksFromUser(int userId);
    Optional<Integer> saveMark(Mark mark);
    Mark updateMark(Mark mark);
    boolean deleteMark(int id);
}
