package pl.app.epublibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.app.epublibrary.exception.InvalidEmailException;
import pl.app.epublibrary.exception.InvalidUsernameException;
import pl.app.epublibrary.model.user.Admin;
import pl.app.epublibrary.model.user.User;
import pl.app.epublibrary.services.AdminService;

import java.util.List;

@RestController
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/admin/add/")
    public void addAdmin(@RequestBody Admin admin) {
        try {
            adminService.saveAdmin(admin);
        } catch (InvalidEmailException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "User with given email already exists", e);
        } catch (InvalidUsernameException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "User with given username already exists", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @DeleteMapping(value = "/admin/delete")
    public void deleteAdmin(@RequestBody Admin admin) {
        try {
            adminService.deleteAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }

    @GetMapping(value = "/admin/all/")
    public @ResponseBody
    List<Admin> getAllAdmins() {
        try {
            return adminService.findAllAdmins();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown Error", e);
        }
    }
}
