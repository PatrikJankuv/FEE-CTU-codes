package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.AgendaPoint;
import org.springframework.stereotype.Repository;

@Repository
public class AgendaPointDao extends BaseDao<AgendaPoint> {

    public AgendaPointDao() {
        super(AgendaPoint.class);
    }
}
