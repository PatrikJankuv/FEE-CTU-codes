package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.Choice;
import org.springframework.stereotype.Repository;

@Repository
public class ChoiceDao extends BaseDao<Choice> {

    public ChoiceDao() {
        super(Choice.class);
    }
}
