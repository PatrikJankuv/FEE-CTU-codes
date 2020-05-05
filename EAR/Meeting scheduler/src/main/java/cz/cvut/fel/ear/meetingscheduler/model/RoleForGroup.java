/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.model;

import javax.persistence.*;

@Entity
public class RoleForGroup extends AbstractEntity {

    @ManyToOne
    private Account user;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private UserGroup group;

    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

}
