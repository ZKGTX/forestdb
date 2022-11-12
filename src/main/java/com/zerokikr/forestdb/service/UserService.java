package com.zerokikr.forestdb.service;


import com.zerokikr.forestdb.entity.Role;
import com.zerokikr.forestdb.entity.User;
import com.zerokikr.forestdb.exception.PasswordsMatchException;
import com.zerokikr.forestdb.exception.RoleNotFoundException;
import com.zerokikr.forestdb.exception.UserExistsException;
import com.zerokikr.forestdb.exception.UserNotFoundException;
import com.zerokikr.forestdb.repository.RoleRepository;
import com.zerokikr.forestdb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers () {
        return userRepository.findAllByOrderByEmailAsc();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUserById (Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) throws UserExistsException {

        Role userRole = roleRepository.findByName("user");

        if (userRole == null) {
            throw new RoleNotFoundException("user");
        }

        if (!user.getRoles().contains(userRole)) {
            user.addRole(userRole);
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmedPassword(passwordEncoder.encode(user.getConfirmedPassword()));

        return userRepository.save(user);
    }

    public User updateUser (User user) {
        Role userRole = roleRepository.findByName("user");
        if (userRole == null) {
            throw new RoleNotFoundException("user");
        }

        if (!user.getRoles().contains(userRole)) {
            user.addRole(userRole);
        }

        return userRepository.save(user);
    }

    public String passwordsMatch(User user) {
        String message = "";
        if (user.getPassword() != null && user.getConfirmedPassword() !=null && !user.getPassword().equals(user.getConfirmedPassword())) {
            message = "пароль и подтверждение пароля не совпадают";
        }
        return message;
    }

    public String emailExists(String email) {
        String message = "";
        if (userRepository.findByEmail(email) != null) {
            message = "Пользователь с электронным адресом " + email + " уже существует";
        }
        return message;
    }
}
