package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.Meeting;
import org.springframework.stereotype.Repository;

@Repository
public class MeetingDao extends BaseDao<Meeting> {

    public MeetingDao() {
        super(Meeting.class);
    }
}
