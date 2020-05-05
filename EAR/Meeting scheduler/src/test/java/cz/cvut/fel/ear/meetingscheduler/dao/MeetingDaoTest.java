/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.MeetingSchedulerApplication;
import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.Meeting;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.model.User;
import cz.cvut.fel.ear.meetingscheduler.service.SystemInitializer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author patrik
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = MeetingSchedulerApplication.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SystemInitializer.class)})
public class MeetingDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MeetingDao sut;

    private List<User> generateUsers() {
        final List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("test" + i + "@t.com");
            users.add(user);
        }

        return users;
    }

    @Test
    public void findUsersForMeeting() {
        final List<User> users = generateUsers();

        final Meeting meeting = new Meeting();
        meeting.setName("Test meeting");
//        meeting.setUsers(users);

        em.persist(meeting);

        final Meeting result = sut.find(meeting.getId());
        assertNotNull(result);
        assertEquals(result, meeting);
    }

    @Test
    public void addUsersToMeeting() {
        final Meeting meeting = new Meeting();
        meeting.setName("Test meeting");

        em.persist(meeting);

        Meeting result = sut.find(meeting.getId());
        assertNotNull(result);

        final User newUser = Generator.generateUnUser1();
        meeting.addUserToMeetingMap(newUser, Role.MEMBER);
        em.persist(meeting);

        result = sut.update(result);
        assertNotNull(result);
        assertEquals(result, meeting);
    }
}
