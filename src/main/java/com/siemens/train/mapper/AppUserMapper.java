package com.siemens.train.mapper;

import com.siemens.train.api.AppUserDTO;
import com.siemens.train.entities.AppUserBE;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUserDTO toDto(AppUserBE entity) {
        if (entity == null) return null;
        return new AppUserDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole()
        );
    }
}