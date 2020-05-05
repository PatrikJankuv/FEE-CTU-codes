/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.MeetingSchedulerApplication;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.service.SystemInitializer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
public class RegisteredUserDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RegisteredUserDao sut;

    @Test
    public void findByUsernameReturnsPersonWithMatchingUsername() {
        final RegisteredUser tUser = new RegisteredUser();
        tUser.setUsername("testUsername");
        tUser.setFirstname("First");
        tUser.setLastname("Last");
        tUser.setEmail("test1@test.com");
        tUser.setId(2);
        tUser.setPassword("Aa123");
        em.persist(tUser);

        final RegisteredUser result = sut.findByString("testUsername");
        assertNotNull(result);
        assertEquals(tUser.getId(), result.getId());
    }

    @Test
    public void findByUsernameReturnsNullForUnknownUsername() {
        assertNull(sut.findByString("adolf"));
    }

    @Test
    public void findByEmailReturnsPersonWithMatchingUsername() {
        final RegisteredUser tUser = new RegisteredUser();
        tUser.setUsername("testUsername");
        tUser.setFirstname("First");
        tUser.setLastname("Last");
        tUser.setEmail("test1@test.com");
        tUser.setId(2);
        tUser.setPassword("Aa123");
        em.persist(tUser);

        final RegisteredUser result = sut.findByString("test1@test.com");
        assertNotNull(result);
        assertEquals(tUser.getId(), result.getId());
    }

    @Test
    public void findByEmailReturnsNullForUnknownUsername() {
        assertNull(sut.findByString("adolf@lol"));
    }

}
