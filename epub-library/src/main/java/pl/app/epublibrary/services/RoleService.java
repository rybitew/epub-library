package pl.app.epublibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.app.epublibrary.model.user.UserRole;
import pl.app.epublibrary.repositories.user.UserRoleRepository;

@Service
public class RoleService {

    private UserRoleRepository userRoleRepository;

    @Autowired
    public RoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void saveRole(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    public void deleteRole(String username) {
        userRoleRepository.deleteById(username);
    }

    public UserRole findRoleByUsername(String username) {
        return userRoleRepository.findById(username).orElse(null);
    }
}
