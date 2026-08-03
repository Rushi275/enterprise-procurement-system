package EnterpriseProcurementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Admin;
import EnterpriseProcurementSystem.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin register(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);

        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }

        return null;
    }
}