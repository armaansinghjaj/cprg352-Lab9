package services;

import dataaccess.RoleDB;
import dataaccess.UserDB;
import java.util.List;
import models.Role;
import models.User;

public class UserService {
    public User get(String email) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        return user;
    }
    
    public List<User> getAll() throws Exception {
        UserDB userDB = new UserDB();
        List<User> users = userDB.getAll();
        return users;
    }
    
    public void insert(String email, boolean active, String first_name, String last_name, String password, int role_id) throws Exception {
        User user = new User(email, active, first_name, last_name, password);
        
        RoleDB roleDB = new RoleDB();
        Role role = roleDB.get(role_id);
        
        user.setRole(role);
        UserDB userDB = new UserDB();
        userDB.insert(user);
    }
    
    public void update(String email, boolean active, String first_name, String last_name, String password, int role_id) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        
        user.setActive(active);
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setPassword(password);
        
        RoleDB roleDB = new RoleDB();
        user.setRole(roleDB.get(role_id));
        userDB.update(user);
    }
    
    public void delete(String email) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        userDB.delete(user);
    }
}
