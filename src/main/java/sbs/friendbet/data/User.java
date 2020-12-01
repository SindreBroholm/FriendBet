package sbs.friendbet.data;

import org.hibernate.validator.constraints.UniqueElements;
import sbs.friendbet.security.auth.UserRole;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    @Size(min = 2, max = 150)
    private String name;
    @NotEmpty
    @Size(min = 3, max = 150)
    private String username;
    @NotEmpty
    @Size(min = 6, max = 300)
    private String password;
    @NotEmpty
    @Size(min = 6, max = 300)
    private String confirmPassword;

    private String role = UserRole.USER.name();

    private String credit = "1000";

    public User(){
    }

    public User(String name, String username, String password,
                String confirmPassword, String role, String credit) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public int getId() {
        return id;
    }
}
