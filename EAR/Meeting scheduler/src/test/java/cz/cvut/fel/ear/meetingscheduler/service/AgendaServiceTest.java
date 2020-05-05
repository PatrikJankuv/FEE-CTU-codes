/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.model.Agenda;
import cz.cvut.fel.ear.meetingscheduler.model.AgendaPoint;
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
public class AgendaServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AgendaService sut;

    private Agenda agenda;

    @Before
    public void setUp() {
        agenda = new Agenda();
        em.persist(agenda);
    }

    @Test
    public void addPointToAgendaTest() {
        AgendaPoint point1 = new AgendaPoint();
        point1.setDescription("First point");
        AgendaPoint point2 = new AgendaPoint();
        point2.setDescription("Second point");

        em.persist(point1);
        em.persist(point2);

        sut.addAgendaPoint(agenda, point1);
        sut.addAgendaPoint(agenda, point2);

        final Agenda result = em.find(Agenda.class, agenda.getId());
        assertEquals(2, result.getAgendaPoints().size());
    }

    @Test
    public void removePointFromEmptyAgendaTest() {
        AgendaPoint point1 = new AgendaPoint();
        point1.setDescription("First point");
        AgendaPoint point2 = new AgendaPoint();
        point2.setDescription("Second point");

        em.persist(point1);
        em.persist(point2);

        sut.removeAgendaPoint(agenda, point1);
        sut.removeAgendaPoint(agenda, point2);

        final Agenda result = em.find(Agenda.class, agenda.getId());
        assertEquals(0, result.getAgendaPoints().size());
    }

    @Test
    public void removePointFromAgendaTest() {
        AgendaPoint point1 = new AgendaPoint();
        point1.setDescription("First point");
        AgendaPoint point2 = new AgendaPoint();
        point2.setDescription("Second point");

        em.persist(point1);
        em.persist(point2);

        sut.addAgendaPoint(agenda, point1);
        sut.addAgendaPoint(agenda, point2);

        sut.removeAgendaPoint(agenda, point1);

        final Agenda result = em.find(Agenda.class, agenda.getId());
        assertEquals(1, result.getAgendaPoints().size());
        assertEquals(point2, result.getAgendaPoints().get(0));
        assertEquals(point2.getDescription(), result.getAgendaPoints().get(0).getDescription());

        sut.removeAgendaPoint(agenda, point2);
        assertEquals(0, result.getAgendaPoints().size());
        sut.removeAgendaPoint(agenda, point2);

    }
}
