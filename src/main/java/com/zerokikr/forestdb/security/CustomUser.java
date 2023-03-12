package com.zerokikr.forestdb.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUser extends org.springframework.security.core.userdetails.User {

    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private String firstName;
    private String lastName;
    private String subjectName;


    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String firstName, String lastName, String subjectName) {
        super(username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.subjectName = subjectName;

    }

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String firstName, String lastName, String subjectName) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.subjectName = subjectName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSubjectName() {
        return subjectName;
    }

}
