package cz.cvut.fel.ear.meetingscheduler.model;

import javax.persistence.*;

@Entity
public class RoleForMeeting extends AbstractEntity {

    @ManyToOne
    private Account user;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private Meeting meeting;

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

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

}
