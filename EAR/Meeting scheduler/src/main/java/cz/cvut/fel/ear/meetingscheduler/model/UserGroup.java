package cz.cvut.fel.ear.meetingscheduler.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "usergroup")
@NamedQueries({
    @NamedQuery(name = "UserGroup.find", query = "SELECT u FROM UserGroup u WHERE u.id = :id")})
public class UserGroup extends AbstractEntity implements Serializable {

    @Column
    private String description;

    @Column
    private String name;

    public UserGroup() {
    }

    public UserGroup(String description, String name) {
        this.description = description;
        this.name = name;
    }
}
