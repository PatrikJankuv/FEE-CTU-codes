/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.GroupUser;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.security.SecurityUtils;
import cz.cvut.fel.ear.meetingscheduler.security.model.UserDetails;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class GroupServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GroupUserService sut;

    @Autowired
    private RegisteredUserService userSut;

    private GroupUser group;

    @Before
    public void setUp() {
        final RegisteredUser tUser1 = Generator.generateUser1();
        SecurityUtils.setCurrentUser(new UserDetails(tUser1));
        em.persist(tUser1);

        group = sut.createNewGroup();
        em.persist(group);
    }

    @Test
    public void addUserToGroupByUserWith() {

        final RegisteredUser tUser2 = Generator.generateUser2();
        final RegisteredUser tUser3 = Generator.generateUser3();

        userSut.persist(tUser2);
        userSut.persist(tUser3);

        sut.addUserToGroup(group, tUser2, Role.MEMBER);
        sut.addUserToGroup(group, tUser3, Role.EDITOR);

        assertEquals(3, group.getUsersMap().size());
    }

    @Test
    public void removeUserFromGroupWithUser() {
        final RegisteredUser tUser2 = Generator.generateUser2();

        userSut.persist(tUser2);

        sut.addUserToGroup(group, tUser2, Role.MEMBER);
        assertEquals(2, group.getUsersMap().size());

        sut.removeUserFromGroup(group, tUser2);
        assertEquals(1, group.getUsersMap().size());

        sut.addUserToGroup(group, tUser2, Role.EDITOR);
        assertEquals(2, group.getUsersMap().size());

        sut.removeUserFromGroup(group, tUser2);
        assertEquals(1, group.getUsersMap().size());

        sut.removeUserFromGroup(group, userSut.findById(1));
        assertEquals(0, group.getUsersMap().size());
    }
}
