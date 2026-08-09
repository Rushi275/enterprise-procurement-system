package EnterpriseProcurementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin register(Admin admin) {

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return adminRepository.save(admin);
    }

    public String login(String username, String password) {

        Admin admin = adminRepository.findByUsername(username);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return jwtUtil.generateToken(username, "ADMIN");
        }

        return null;
    }
}