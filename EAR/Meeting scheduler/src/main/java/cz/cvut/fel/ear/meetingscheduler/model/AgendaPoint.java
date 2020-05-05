package cz.cvut.fel.ear.meetingscheduler.model;

import javax.persistence.*;

@Entity
public class AgendaPoint extends AbstractEntity {

    private String description;

    private String link;

    public AgendaPoint() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
