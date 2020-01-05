package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.exception.InvalidEmailException;
import pl.app.epublibrary.exception.InvalidUsernameException;
import pl.app.epublibrary.model.user.Admin;
import pl.app.epublibrary.model.user.UserRole;
import pl.app.epublibrary.repositories.user.AdminRepository;

import java.util.List;
import java.util.Set;

@Service
public class AdminService {

    private AdminRepository adminRepository;
    private RoleService roleService;

    @Autowired
    public AdminService(AdminRepository adminRepository, RoleService roleService) {
        this.adminRepository = adminRepository;
        this.roleService = roleService;
    }

    //region CRUD
    public void saveAdmin(Admin admin) throws InvalidEmailException, InvalidUsernameException {
        if (findAdminByUsername(admin.getUsername()) == null) {
            if (findAdminByUsernameAndEmailAddress(admin.getUsername(), admin.getEmail()) == null) {
                adminRepository.save(admin);
                roleService.saveRole(new UserRole(admin.getUsername(), "admin"));
            } else {
                throw new InvalidEmailException();
            }
        } else {
            throw new InvalidUsernameException();
        }
    }

    public void deleteAdmin(Admin admin) {
        adminRepository.deleteByUsernameAndEmail(admin.getUsername(), admin.getEmail());
    }
//endregion

//region GETTERS
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }
//endregion

    private Admin findAdminByUsername(String username) {
        return adminRepository.findById(username).orElse(null);
    }

    private Admin findAdminByUsernameAndEmailAddress(String username, String email) {
        try {
            return adminRepository.findByUsernameAndEmail(username, email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
