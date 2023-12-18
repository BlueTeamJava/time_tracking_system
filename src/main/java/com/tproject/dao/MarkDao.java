package com.tproject.dao;

import com.tproject.entity.Mark;
import com.tproject.exception.CustomSQLException;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface MarkDao {
    Optional<Mark> findMarkById(int id) throws CustomSQLException;
    Optional<Mark> findMarkByUserDate(int userId, Date date) throws CustomSQLException;
    Collection<Mark> getAllMarksByDate(Date date);
    Collection<Mark> getAllMarksFromUser(int userId);
    Map<Integer, Integer> getTotalMarksByUsers() throws CustomSQLException;
    Optional<Integer> saveMark(Mark mark);
    Mark updateMark(Mark mark);
    boolean deleteMark(int id);
}
