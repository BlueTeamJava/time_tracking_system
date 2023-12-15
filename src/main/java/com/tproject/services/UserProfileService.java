package com.tproject.services;

import com.tproject.dto.UserProfileDto;
import com.tproject.exception.CustomSQLException;

import java.util.Optional;

public interface UserProfileService {
    <Optional> UserProfileDto getUserProfile(int id) throws CustomSQLException;

    Optional<Integer> createUserProfile(final UserProfileDto userProfileDto) throws CustomSQLException;

    UserProfileDto updateUserProfile(final UserProfileDto userProfileDto) throws CustomSQLException;

    boolean deleteUserProfile(int id) throws CustomSQLException;
}
