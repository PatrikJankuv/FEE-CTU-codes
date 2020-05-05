package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.DateLocation;
import org.springframework.stereotype.Repository;

@Repository
public class DateLocationDao extends BaseDao<DateLocation> {

    public DateLocationDao() {
        super(DateLocation.class);
    }
}
