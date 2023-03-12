package com.zerokikr.forestdb.service;

import com.zerokikr.forestdb.entity.Role;
import com.zerokikr.forestdb.entity.User;
import com.zerokikr.forestdb.repository.UserRepository;
import com.zerokikr.forestdb.security.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FDbUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String subjectName = user.getSubjectName();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new CustomUser(user.getEmail(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(user.getRoles()), firstName, lastName, subjectName);
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
      return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }



}
