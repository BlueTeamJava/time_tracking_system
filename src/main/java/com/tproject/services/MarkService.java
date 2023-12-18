package com.tproject.services;

import com.tproject.dto.MarkDto;
import com.tproject.exception.CustomSQLException;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface MarkService {
    <Optional> MarkDto getMarkById(int id) throws CustomSQLException;

    <Optional> MarkDto getMarkByUserDate(int userId, Date date) throws CustomSQLException;

    Collection<MarkDto> getMarkByDate(Date date) throws CustomSQLException;

    Collection<MarkDto> getAllUserMarks(int userId) throws CustomSQLException;
    public Map<Integer, Integer> getTotalMarksByUsers() throws CustomSQLException;

    Optional<Integer> createMark(MarkDto markDto) throws CustomSQLException;

    MarkDto updateMark(final MarkDto markDto) throws CustomSQLException;

    boolean deleteMark(int id) throws CustomSQLException;
}
