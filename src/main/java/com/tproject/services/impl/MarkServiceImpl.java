package com.tproject.services.impl;

import com.tproject.dao.impl.MarkDaoImpl;
import com.tproject.dto.MarkDto;
import com.tproject.entity.Mark;
import com.tproject.exception.CustomSQLException;
import com.tproject.mappers.MarkMapper;
import com.tproject.services.MarkService;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class MarkServiceImpl implements MarkService {
    private MarkDaoImpl markDao;
    private MarkMapper mapper;

    private static MarkServiceImpl instance;

    private MarkServiceImpl() {
        markDao = MarkDaoImpl.getInstance();
        mapper = Mappers.getMapper(MarkMapper.class);
    }

    public static MarkServiceImpl getInstance() {
        MarkServiceImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (TaskServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MarkServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    public <Optional> MarkDto getMarkById(int id) throws CustomSQLException {
        try {
            return mapper.markToDto(markDao.findMarkById(id).orElseThrow(() -> new CustomSQLException("Mark not found")));
        } catch (CustomSQLException e) {
            throw e;
        }
    }

    @Override
    public <Optional> MarkDto getMarkByUserDate(int userId, Date date) throws CustomSQLException {
        try {
            return mapper.markToDto(markDao.findMarkByUserDate(userId, date).orElseThrow(() -> new CustomSQLException("Mark not found")));
        } catch (CustomSQLException e) {
            throw e;
        }
    }

    @Override
    public Collection<MarkDto> getAllUserMarks(int userId) throws CustomSQLException {
        try {
            return mapper.markToDtoCollection(markDao.getAllMarksFromUser(userId));
        } catch (CustomSQLException e) {
            throw e;
        }
    }

    @Override
    public Optional<Integer> createMark(MarkDto markDto) throws CustomSQLException {
        try {
            Mark mark = mapper.dtoToMark(markDto);
            return markDao.saveMark(mark);
        } catch (CustomSQLException e) {
            throw e;
        }
    }

    @Override
    public MarkDto updateMark(MarkDto markDto) throws CustomSQLException {
        try {
            return mapper.markToDto(
                    markDao.updateMark(mapper.dtoToMark(markDto)));
        } catch (CustomSQLException e) {
            throw e;
        }
    }

    @Override
    public boolean deleteMark(int id) throws CustomSQLException {
        try {
            return markDao.deleteMark(id);
        } catch (CustomSQLException e) {
            throw e;
        }
    }
}
