package com.gdsc.boilerplate.springboot.security.mapper;

import com.gdsc.boilerplate.springboot.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role convertToRole(Long id,String name);
}
