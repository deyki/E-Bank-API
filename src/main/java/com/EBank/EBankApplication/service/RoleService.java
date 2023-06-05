package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.entity.Role;
import com.EBank.EBankApplication.error.RoleNotFoundException;

import java.util.Set;

public interface RoleService {
    void seedRolesInDB();

    Set<Role> findAllRoles();

    Role findByAuthority(String authority) throws RoleNotFoundException;
}
