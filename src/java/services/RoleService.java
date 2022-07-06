package services;

import dataaccess.RoleDB;
import java.util.List;
import models.Role;

public class RoleService {
    public List<Role> getAll(int role_id) throws Exception {
        RoleDB roleDB = new RoleDB();
        List<Role> role = roleDB.getAll();
        return role;
    }
    
    public Role get(int role_id) throws Exception {
        RoleDB roleDB = new RoleDB();
        Role role = roleDB.get(role_id);
        return role;
    }
}
