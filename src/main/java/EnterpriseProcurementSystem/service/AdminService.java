package EnterpriseProcurementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Admin;
import EnterpriseProcurementSystem.repository.AdminRepository;
import EnterpriseProcurementSystem.util.JwtUtil;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Admin register(Admin admin) {
        return adminRepository.save(admin);
    }

    public String login(String username, String password) {

        Admin admin = adminRepository.findByUsername(username);

        if (admin != null && admin.getPassword().equals(password)) {
            return jwtUtil.generateToken(username);
        }

        return null;
    }
}