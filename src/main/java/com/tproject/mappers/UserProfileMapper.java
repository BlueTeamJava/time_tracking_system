package com.tproject.mappers;

import com.tproject.dto.UserProfileDto;
import com.tproject.entity.UserProfile;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface UserProfileMapper {
    UserProfileDto userProfileToDto(UserProfile userProfile);
    UserProfile dtoToUserProfile(UserProfileDto userProfileDto);
    Collection<UserProfileDto> userProfileToDtoCollection(Collection<UserProfile> userProfileCollection);
    Collection<UserProfile> dtoToUserProfileCollection(Collection<UserProfileDto> userProfileCollectionDto);
}
