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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserService {

    @Autowired
    private JavaMailSender mailSender;

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

//        if (!user.getRoles().contains(userRole)) {
//            user.addRole(userRole);
//        }

        return userRepository.save(user);
    }

    public String passwordsMatch(User user) {
        String message = "";
        if (user.getPassword() != null && user.getConfirmedPassword() != null
                && !user.getPassword().equals(user.getConfirmedPassword())) {
            message = "пароль и подтверждение пароля не совпадают";
        }
        if (user.getPassword() != null && user.getConfirmedPassword() != null
        && user.getPassword().isEmpty() || user.getConfirmedPassword().isEmpty()) {
            message = "пароль и/или подтверждение пароля не могут быть пустыми";
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

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void sendEmail(String email, String link) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("rusforestdb@gmail.com", "RiskyForest Support");
        helper.setTo(email);

        String subject = "Ссылка на сброс вашего пароля";
        String content = "<p>Здравствуйте!</p>"
                + "<p>Вы запросили сброс пароля.</p>"
                + "<p>Кликните ссылку внизу для сброса пароля:</p>"
                + "<p><a href=\"" + link + "\">Сменить пароль</a></p>"
                + "<br>"
                + "<p>Игнорируйте это письмо, если вспомнили пароль, "
                + "или если запрос отправляли не вы.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
