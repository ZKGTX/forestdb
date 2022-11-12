package com.zerokikr.forestdb.entity;



import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column (name = "email")
    @NotNull(message = "введите адрес электронной почты")
    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$", message = "некорректный адрес электронной почты")
    private String email;

    @Column(name = "first_name")
    @NotNull (message = "введите имя")
    @Pattern(regexp="[а-яёА-ЯЁ ,.()-]+", message = "имя только из русских букв")
    private String firstName;
    @Column(name = "last_name")
    @NotNull (message = "введите фамилию")
    @Pattern(regexp="[а-яёА-ЯЁ ,.()-]+", message = "фамилия только из русских букв")
    private String lastName;

    @Column(name = "password")
    @NotNull (message = "введите пароль")
    private String password;

    @Column(name = "password_confirm")
    @NotNull (message = "повторите пароль")
    private String confirmedPassword;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable (name = "users_roles",
                joinColumns = @JoinColumn(name="user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    public User() {
    }

    public User(String email, String firstName, String last_name, String password, String passwordConfirm, List<Role> roles) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = last_name;
        this.password = password;
        this.confirmedPassword = passwordConfirm;
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getConfirmedPassword(), user.getConfirmedPassword()) && Objects.equals(getRoles(), user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getFirstName(), getLastName(), getPassword(), getConfirmedPassword(), getRoles());
    }


}
