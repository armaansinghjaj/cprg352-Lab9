package servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Role;
import models.User;
import services.RoleService;
import services.UserService;

public class UserServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Create UserService object
        UserService us = new UserService();
        
        // if user clicked edit link
        if(request.getParameter("action") != null && request.getParameter("action").equals("edit")){
            User users = null;
            String emailLink = request.getParameter("emailLink").replace(" ", "+");
            
            try {
                users = us.get(emailLink);
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("message", "error");
            }
            
            HttpSession edit_session = request.getSession();
            
            edit_session.setAttribute("edit_user", emailLink);
            request.setAttribute("update_f_name", users.getFirstName());
            request.setAttribute("update_l_name", users.getLastName());
            request.setAttribute("update_password", users.getPassword());
            request.setAttribute("update_active", users.getActive());
            request.setAttribute("update_role", users.getRole());
        }
        // if user clicked delete link
        else if(request.getParameter("action") != null && request.getParameter("action").equals("delete")){
            
            try {
                String emailLink = request.getParameter("emailLink").replace(" ", "+");
                us.delete(emailLink);
                request.setAttribute("message", "delete");
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("message", "error");
            }
        }
        
        // show all the users currently in the database
        try {
            List<User> users = us.getAll();
            request.setAttribute("users", users);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("message", "error");
        }
        
        // Load the JSP and stop the code call
        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // create UserService and RoleService objects
        UserService us = new UserService();
        RoleService rs = new RoleService();
        
        // if user pressed add button
        if(request.getParameter("action").equals("add")){
            
            String email = request.getParameter("email").trim();
            String fName = request.getParameter("f_name");
            String lName = request.getParameter("l_name");
            String password = request.getParameter("password");
            String role_id = request.getParameter("role");
            boolean isActive = (request.getParameter("isActive") != null);
            
            // check if any of the fields is null
            if(email == null || email.equals("") || fName == null || fName.equals("") || lName == null || lName.equals("") || password == null || password.equals("") || role_id == null || role_id.equals("") ){
                request.setAttribute("add_email", email);
                request.setAttribute("add_f_name", fName);
                request.setAttribute("add_l_name", lName);
                request.setAttribute("add_password", password);
                request.setAttribute("add_role", role_id);
                request.setAttribute("message", "inputError");
                
                try {
                    List<User> users = us.getAll();
                    request.setAttribute("users", users);
                } catch (Exception ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                    request.setAttribute("message", "error");
                }
                
                // Load the JSP and stop the code call
                getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
                return;
            }
            
            try {
                if(us.get(email) != null){
                    request.setAttribute("message", "exists");
                    // show all the users
                    try {
                        List<User> users = us.getAll();
                        request.setAttribute("users", users);
                    } catch (Exception ex) {
                        Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                        request.setAttribute("message", "error");
                    }

                    // Load the JSP and stop the code call
                    getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                us.insert(email, isActive, fName, lName, password, Integer.parseInt(role_id));
                request.setAttribute("message", "create");
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("message", "error");
            }
        }
        
        // if user pressed update button
        else if(request.getParameter("action").equals("update")){
            
            String fName = request.getParameter("f_name");
            String lName = request.getParameter("l_name");
            String role_id = request.getParameter("role");
            String password = request.getParameter("password");
            boolean isActive = (request.getParameter("isActive") != null);
            
            // check if any of the fields is null
            if(fName == null || fName.equals("") || lName == null || lName.equals("") || role_id == null || role_id.equals("") ){
                request.setAttribute("update_f_name", fName);
                request.setAttribute("update_l_name", lName);
                request.setAttribute("update_role", role_id);
                request.setAttribute("message", "inputError");
                
                try {
                List<User> users = us.getAll();
                request.setAttribute("users", users);
                } catch (Exception ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                    request.setAttribute("message", "error");
                }
                
                // Load the JSP and stop the code call
                getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
                return;
            }
            
            try {
                HttpSession edit_session = request.getSession(true);
                String email = edit_session.getAttribute("edit_user").toString();
                us.update(email, isActive, fName, lName, password, Integer.parseInt(role_id));
                request.setAttribute("message", "update");
                edit_session.invalidate();
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("message", "error");
            }
        }
        
        // show all the users
        try {
            List<User> users = us.getAll();
            request.setAttribute("users", users);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("message", "error");
        }
        
        // Load the JSP and stop the code call
        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        return;       
    }
}