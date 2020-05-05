package cz.cvut.fel.ear.meetingscheduler.model;

import javax.persistence.*;

@Entity
@Table(name = "unregistered_user")
@NamedQueries({
    @NamedQuery(name = "UnregisteredUser.findByEmail", query = "SELECT u FROM UnregisteredUser u WHERE u.email = :email")})
public class UnregisteredUser extends User {

    public UnregisteredUser() {

    }
}
