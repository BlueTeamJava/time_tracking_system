package com.tproject.dao;

import com.tproject.entity.UserProfile;
import com.tproject.exception.CustomSQLException;

import java.util.Collection;
import java.util.Optional;

public interface UserProfileDao {

    Optional<UserProfile> getUserProfile(int id) throws CustomSQLException;
    Optional<Integer> saveUserProfile(UserProfile userProfile);
    UserProfile updateUserProfile(UserProfile userProfile);
    Collection<UserProfile> getAllUserProfiles() throws CustomSQLException;
    boolean deleteUserProfile(int id);
}
