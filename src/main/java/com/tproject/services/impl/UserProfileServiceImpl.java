package com.tproject.services.impl;

import com.tproject.dao.impl.UserProfileDaoImpl;
import com.tproject.dto.UserProfileDto;
import com.tproject.entity.UserProfile;
import com.tproject.exception.CustomSQLException;
import com.tproject.mappers.UserProfileMapper;
import com.tproject.services.UserProfileService;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

public class UserProfileServiceImpl implements UserProfileService {
    private UserProfileDaoImpl userProfileDao;
    private UserProfileMapper mapper;

    private static UserProfileServiceImpl instance;

    private UserProfileServiceImpl() {
        userProfileDao = UserProfileDaoImpl.getInstance();
        mapper = Mappers.getMapper(UserProfileMapper.class);
    }

    public static UserProfileServiceImpl getInstance() {
        UserProfileServiceImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (TaskServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserProfileServiceImpl();
                }
            }
        }
        return localInstance;
    }

    @Override
    public <Optional> UserProfileDto getUserProfile(int id) throws CustomSQLException {
        try {
            return mapper.userProfileToDto(userProfileDao.getUserProfile(id).orElseThrow(() -> new CustomSQLException("UserProfile not found")));
        } catch (CustomSQLException e) {
            throw e;
        }
    }

    @Override
    public Optional<Integer> createUserProfile(UserProfileDto userProfileDto) throws CustomSQLException {
        try {
            UserProfile userProfile = mapper.dtoToUserProfile(userProfileDto);
            return userProfileDao.saveUserProfile(userProfile);
        } catch (CustomSQLException e) {
            throw e;
        }
    }

    @Override
    public UserProfileDto updateUserProfile(UserProfileDto userProfileDto) throws CustomSQLException {
        try {
            return mapper.userProfileToDto(
                    userProfileDao.updateUserProfile(mapper.dtoToUserProfile(userProfileDto)));
        } catch (CustomSQLException e) {
            throw e;
        }
    }

    @Override
    public boolean deleteUserProfile(int id) throws CustomSQLException {
        try {
            return userProfileDao.deleteUserProfile(id);
        } catch (CustomSQLException e) {
            throw e;
        }
    }
}
