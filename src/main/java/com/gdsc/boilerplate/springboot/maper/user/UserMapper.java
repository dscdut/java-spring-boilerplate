package com.gdsc.boilerplate.springboot.maper.user;

import com.gdsc.boilerplate.springboot.security.dto.UpdateUserRequest;
import com.gdsc.boilerplate.springboot.security.dto.UserUpdateInformationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.gdsc.boilerplate.springboot.configuration.dto.user.UserDto;
import com.gdsc.boilerplate.springboot.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDto convertToUserDto(User user);
	User convertToUser(UpdateUserRequest updateUserRequest);

	User convertToUser(UserUpdateInformationRequest userUpdateInformationRequest);

}
