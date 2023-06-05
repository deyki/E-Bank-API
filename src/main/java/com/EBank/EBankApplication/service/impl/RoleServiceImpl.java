package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.Role;
import com.EBank.EBankApplication.error.RoleNotFoundException;
import com.EBank.EBankApplication.repository.RoleRepository;
import com.EBank.EBankApplication.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void seedRolesInDB() {

        Role roleAdmin = new Role();
        roleAdmin.setAuthority("ADMIN");

        Role roleUser = new Role();
        roleUser.setAuthority("USER");

        roleRepository.saveAll(new HashSet<>(List.of(roleAdmin, roleUser)));
    }

    @Override
    public Set<Role> findAllRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

    @Override
    public Role findByAuthority(String authority) throws RoleNotFoundException {
        return roleRepository
                .findByAuthority(authority)
                .orElseThrow(() -> new RoleNotFoundException(String.format("Role %s not found!", authority)));
    }
}
