package cz.cvut.fel.ear.meetingscheduler.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Agenda extends AbstractEntity {

    @OneToMany
    private List<AgendaPoint> agendaPoints;

    public void setAgendaPoints(List<AgendaPoint> agendaPoints) {
        this.agendaPoints = agendaPoints;
    }

    public List<AgendaPoint> getAgendaPoints() {
        return agendaPoints;
    }

    public void addAgendaPoint(AgendaPoint point) {
        if (agendaPoints == null) {
            agendaPoints = new ArrayList<>();
        }
        this.agendaPoints.add(point);
    }

    public void removeAgendaPoint(AgendaPoint point) {
        if (agendaPoints == null || agendaPoints.isEmpty()) {
            return;
        }
        this.agendaPoints.remove(point);
    }

}
