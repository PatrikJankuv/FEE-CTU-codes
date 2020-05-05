/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

/**
 *
 * @author patrik
 */
@Entity
public class GroupUser extends AbstractEntity implements Serializable {

    public GroupUser() {
    }
    @Column
    private String description;

    @Column
    private String name;

//    @ManyToMany
//    @JoinTable(name="roleforgroup",
//            joinColumns = @JoinColumn(name="group_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
    @MapKeyColumn(name = "groupuser_id")
    @ElementCollection
    @CollectionTable(name = "USER_ROLE_GROUP")
    @MapKeyJoinColumn(name = "USER_ID")
    @Column(name = "ROLE_OF_USER")
    private Map<RegisteredUser, Role> usersmap;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<RegisteredUser, Role> getUsersMap() {
        return usersmap;
    }

    @JsonIgnore
    public void setUsersMap(Map<RegisteredUser, Role> users) {
        this.usersmap = users;
    }

    public void addUser(RegisteredUser user, Role role) {
        if (this.usersmap == null) {
            this.usersmap = new HashMap<>();
        }

        this.usersmap.put(user, role);
    }

    public void removeUser(RegisteredUser user) {
        if (this.usersmap == null || this.usersmap.isEmpty()) {
            return;
        }
        this.usersmap.remove(user);
    }

}
