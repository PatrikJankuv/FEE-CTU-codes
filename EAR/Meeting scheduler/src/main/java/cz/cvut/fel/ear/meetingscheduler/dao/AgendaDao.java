package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.Agenda;
import org.springframework.stereotype.Repository;

@Repository
public class AgendaDao extends BaseDao<Agenda> {

    public AgendaDao() {
        super(Agenda.class);
    }
}
