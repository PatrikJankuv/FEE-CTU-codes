package cz.cvut.fel.ear.meetingscheduler.model;

import java.io.Serializable;
import javax.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "registered_user")
@NamedQueries({
    @NamedQuery(name = "RegisteredUser.findByUsername", query = "SELECT u FROM RegisteredUser u WHERE u.username = :username")
    ,
    @NamedQuery(name = "RegisteredUser.findByEmail", query = "SELECT u FROM RegisteredUser u WHERE u.email = :email")
})
public class RegisteredUser extends User implements Serializable {

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String userPassword;

    public String getFirstname() {
        return firstname;
    }

    public RegisteredUser() {

    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void encodePassword(PasswordEncoder encoder) {
        this.userPassword = encoder.encode(userPassword);
    }

    public void erasePassword() {
        this.userPassword = null;
    }

    public String getPassword() {
        return userPassword;
    }

    public void setPassword(String password) {
        this.userPassword = password;
    }
}
