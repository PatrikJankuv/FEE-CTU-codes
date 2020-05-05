/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.MeetingSchedulerApplication;
import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.UnregisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.User;
import cz.cvut.fel.ear.meetingscheduler.service.SystemInitializer;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Matej
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = MeetingSchedulerApplication.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SystemInitializer.class)})
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Autowired
    private RegisteredUserDao rdao;

    @Autowired
    private UnregisteredUserDao udao;

    @Test
    public void findByEmailReturnsCorrectUserRegistered() {
        String email = "finding@nemo.com";
        RegisteredUser user = Generator.generateUser1();
        RegisteredUser user2 = Generator.generateUser2();
        UnregisteredUser uuuser = Generator.generateUnUser1();

        user.setEmail(email);
        rdao.persist(user);
        rdao.persist(user2);
        udao.persist(uuuser);

        User t = dao.find(2);
        assertEquals(user, dao.find(user.getId()));
        assertNotEquals(user, dao.find(user2.getId()));
        assertEquals(user2, dao.find(user2.getId()));
        assertEquals(uuuser, dao.find(uuuser.getId()));
    }
}
