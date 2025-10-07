package com.accuresoftech.abc.services;

import com.accuresoftech.abc.entity.auth.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getByKey(com.accuresoftech.abc.enums.RoleKey key);
}
