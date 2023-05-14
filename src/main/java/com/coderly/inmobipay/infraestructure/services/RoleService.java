package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.core.entities.RoleEntity;
import com.coderly.inmobipay.core.repositories.RolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class RoleService {
    private RolRepository rolRepository;

    private static final String[] DEFAULT_ROLES = {"USER", "ADMIN"};

    public void seedRol() {
        Arrays.stream(DEFAULT_ROLES).forEach(name -> {
            if(!rolRepository.existsByName(name)) {
                rolRepository.save(new RoleEntity().withName(name));
            }
        } );

    }
}
