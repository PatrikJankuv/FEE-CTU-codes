package cz.cvut.fel.ear.meetingscheduler.model;

import java.util.Date;
import javax.persistence.*;

@Entity
public class DateLocation extends AbstractEntity {

    @Column
    private Date datetime;

    @Column
    private String location;

    public DateLocation() {

    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
