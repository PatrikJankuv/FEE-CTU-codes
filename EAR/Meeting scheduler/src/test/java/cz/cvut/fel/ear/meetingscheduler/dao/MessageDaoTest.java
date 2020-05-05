/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.MeetingSchedulerApplication;
import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.Message;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.service.SystemInitializer;
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
public class MessageDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MessageDao sut;

    @Test
    public void findConversation() {
        RegisteredUser tUser = Generator.generateUser1();
        em.persist(tUser);

        RegisteredUser tUser2 = Generator.generateUser2();
        em.persist(tUser2);

        RegisteredUser tUser3 = Generator.generateUser3();
        em.persist(tUser3);

        final Message message1 = new Message("Ahoj", "Ahoj", tUser, tUser2);
        final Message message2 = new Message("Cau", "", tUser2, tUser);

        em.persist(message1);
        em.persist(message2);

        List<Message> result = sut.findConversation(tUser2, tUser);

        assertNotNull(result);
        assertEquals(2, result.size());

        final Message message3 = new Message("Ako sa mas", "", tUser2, tUser);
        em.persist(message3);

        result = sut.findConversation(tUser2, tUser);
        assertEquals(3, result.size());

        final Message message4 = new Message("Serus", "", tUser2, tUser3);
        em.persist(message4);

        result = sut.findConversation(tUser2, tUser);
        assertEquals(3, result.size());

        result = sut.findConversation(tUser2, tUser3);
        assertEquals(1, result.size());
    }

    @Test
    public void findMyMessages() {
        RegisteredUser tUser = Generator.generateUser1();
        em.persist(tUser);

        RegisteredUser tUser2 = Generator.generateUser2();
        em.persist(tUser);

        RegisteredUser tUser3 = Generator.generateUser3();
        em.persist(tUser3);

        final Message message1 = new Message("Ako sa mas", "", tUser2, tUser);
        em.persist(message1);

        final Message message2 = new Message("Ako sa mas", "", tUser2, tUser);
        em.persist(message2);

        final Message message3 = new Message("Ako sa mas", "", tUser2, tUser3);
        em.persist(message3);

        final Message message4 = new Message("Ako sa mas", "", tUser3, tUser);
        em.persist(message4);

        List<Message> result = sut.findMyMessages(tUser);
        assertEquals(3, result.size());

        result = sut.findMyMessages(tUser3);
        assertEquals(2, result.size());

        result = sut.findMyMessages(tUser2);
        assertEquals(3, result.size());
    }
}
