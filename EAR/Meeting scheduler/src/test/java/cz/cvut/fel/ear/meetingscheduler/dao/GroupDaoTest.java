/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.MeetingSchedulerApplication;
import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.GroupUser;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.service.SystemInitializer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class GroupDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GroupUserDao sut;

    @Test
    public void findTest() {
        final GroupUser g = new GroupUser();
        final RegisteredUser tUser1 = Generator.generateUser1();
        final RegisteredUser tUser2 = Generator.generateUser2();
        final RegisteredUser tUser3 = Generator.generateUser3();

        g.addUser(tUser3, Role.MEMBER);
        g.addUser(tUser1, Role.CREATOR);
        g.addUser(tUser2, Role.EDITOR);

        em.persist(g);
        GroupUser r = sut.find(g.getId());
        assertNotNull(r);
    }
}
