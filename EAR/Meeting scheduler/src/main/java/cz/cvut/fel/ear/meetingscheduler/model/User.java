/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Matej
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User extends AbstractEntity implements Serializable {

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Account account;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
