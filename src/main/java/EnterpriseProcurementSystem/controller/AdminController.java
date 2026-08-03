package EnterpriseProcurementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.entity.Admin;
import EnterpriseProcurementSystem.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public Admin register(@RequestBody Admin admin) {
        return adminService.register(admin);
    }

    @PostMapping("/login")
    public String login(@RequestBody Admin admin) {

        Admin existingAdmin = adminService.login(admin.getUsername(), admin.getPassword());

        if (existingAdmin != null) {
            return "Login Successful";
        }

        return "Invalid Username or Password";
    }
}