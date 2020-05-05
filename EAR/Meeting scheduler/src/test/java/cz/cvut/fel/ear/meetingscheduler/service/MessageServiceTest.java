/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.Message;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.security.SecurityUtils;
import cz.cvut.fel.ear.meetingscheduler.security.model.UserDetails;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class MessageServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MessageService sut;

    @Autowired
    private RegisteredUserService userSut;

    @Test
    public void testFindMessage() {
        final RegisteredUser tUser2 = Generator.generateUser2();
        final RegisteredUser tUser3 = Generator.generateUser3();
        em.persist(tUser2);
        em.persist(tUser3);

        SecurityUtils.setCurrentUser(new UserDetails(tUser3));

        final Message message = new Message("ahoj", "co", tUser3, tUser2);
        em.persist(message);

        final Message result = sut.find(message.getId());
        assertNotNull(result);
        assertEquals(result.getBody(), message.getBody());
    }
}
