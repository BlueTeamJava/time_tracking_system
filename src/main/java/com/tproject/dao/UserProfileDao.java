package com.tproject.dao;

import com.tproject.entity.UserProfile;

import java.sql.SQLException;
import java.util.Optional;

public interface UserProfileDao {

    Optional<UserProfile> getUserProfile(int id) throws SQLException;
    Optional<Integer> saveUserProfile(UserProfile userProfile);
    UserProfile updateUserProfile(UserProfile userProfile);
    boolean deleteUserProfile(int id);
}
