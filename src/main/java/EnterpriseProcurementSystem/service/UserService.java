package EnterpriseProcurementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

   
    public User saveUser(User user) {
        return userRepository.save(user);
    }

   
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}