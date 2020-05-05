package cz.cvut.fel.ear.meetingscheduler.model;

import javax.persistence.*;

@Entity
public class Choice extends AbstractEntity {

    public Choice() {
    }

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer points;

    @ManyToOne
    private Account user;

    @ManyToOne
    private DateLocation dateLocation;

    public Choice(Integer points, Account user, Meeting meeting, DateLocation dateLocation) {
        this.points = points;
        this.user = user;
        this.dateLocation = dateLocation;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Account getUser() {
        return user;
    }

    public DateLocation getDateLocation() {
        return dateLocation;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public void setDateLocation(DateLocation dateLocation) {
        this.dateLocation = dateLocation;
    }

    @Override
    public String toString() {
        return "Choice{" + "points=" + points
                + ", user=" + user.getId()
                + ", dateLocation=" + dateLocation.getLocation() + '}';
    }

}
