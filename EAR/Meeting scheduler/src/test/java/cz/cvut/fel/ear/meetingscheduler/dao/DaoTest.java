/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.MeetingSchedulerApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author patrik
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = MeetingSchedulerApplication.class)
public class DaoTest {

    @Autowired
    protected AccountDao accDao;

    @Autowired
    protected AgendaDao agendaDao;

    @Autowired
    protected AgendaPointDao agendaPointDao;

    @Autowired
    protected ChoiceDao choiceDao;

    @Autowired
    protected DateLocationDao dateLocationDao;

    @Autowired
    protected MeetingDao meetingDao;

    @Autowired
    protected MessageDao messageDao;

    @Autowired
    protected RegisteredUserDao registeredUserDao;

    @Autowired
    protected UnregisteredUserDao unregisterduserDao;

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected UserGroupDao userGroupDao;

    //test if contains all DAO beans
    @Test
    public void testRepositoriesInApplicationContext() {
        Assert.assertNotNull(accDao);
        Assert.assertNotNull(agendaPointDao);
        Assert.assertNotNull(agendaPointDao);
        Assert.assertNotNull(choiceDao);
        Assert.assertNotNull(dateLocationDao);
        Assert.assertNotNull(meetingDao);
        Assert.assertNotNull(messageDao);
        Assert.assertNotNull(registeredUserDao);
        Assert.assertNotNull(unregisterduserDao);
        Assert.assertNotNull(userDao);
        Assert.assertNotNull(userGroupDao);
    }
}
