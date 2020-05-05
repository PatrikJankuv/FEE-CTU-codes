/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.Choice;
import cz.cvut.fel.ear.meetingscheduler.model.DateLocation;
import cz.cvut.fel.ear.meetingscheduler.model.Meeting;
import cz.cvut.fel.ear.meetingscheduler.model.PollState;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.model.UnregisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.User;
import cz.cvut.fel.ear.meetingscheduler.security.SecurityUtils;
import cz.cvut.fel.ear.meetingscheduler.security.model.UserDetails;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
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
public class MeetingServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MeetingService sut;

    @Autowired
    private RegisteredUserService userSut;

    @Autowired
    private ChoiceService choiceSut;

    private Meeting meeting;

    @Before
    public void setUp() {
        meeting = new Meeting();
        em.persist(meeting);
        final RegisteredUser tUser1 = Generator.generateUser1();
        em.persist(tUser1);
        meeting.addUserToMeetingMap(tUser1, Role.CREATOR);
        em.persist(meeting);
    }

    @Test
    public void changePollStatusOfMeeting() {
        sut.setStatusOfMeeting(meeting, PollState.ACTIVE);

        final Meeting result = em.find(Meeting.class, meeting.getId());

        assertEquals(result.getPollState(), PollState.ACTIVE);

        sut.setStatusOfMeeting(meeting, PollState.LOCKED);
        assertEquals(result.getPollState(), PollState.LOCKED);
    }

    @Test
    public void addUserIntoMeetingWithUserRoleCreator() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        final RegisteredUser tUser2 = Generator.generateUser2();
        final RegisteredUser tUser3 = Generator.generateUser3();

        userSut.persist(tUser2);
        userSut.persist(tUser3);

        sut.addUserIntoMeetingMap(meeting, tUser2, Role.EDITOR);
        sut.addUserIntoMeetingMap(meeting, tUser3, Role.MEMBER);

        assertEquals(3, meeting.getUsersMap().size());
    }

    @Test
    public void addUserIntoMeetingWittDifferentUserRoles() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        final RegisteredUser tUser2 = Generator.generateUser2();
        final RegisteredUser tUser3 = Generator.generateUser3();
        em.persist(tUser2);
        em.persist(tUser3);

        //member user try add new user into meeting
        try {
            sut.addUserIntoMeetingMap(meeting, tUser2, Role.MEMBER);
            SecurityUtils.setCurrentUser(new UserDetails(tUser2));
            sut.addUserIntoMeetingMap(meeting, tUser3, Role.MEMBER);
            fail("AccessDeniedException should have been thrown here!");
        } catch (AccessDeniedException e) {
        }

        //creator change role, member user to editor
        SecurityUtils.setCurrentUser(new UserDetails(tUser));
        sut.addUserIntoMeetingMap(meeting, tUser2, Role.EDITOR);
        assertEquals(2, meeting.getUsersMap().size());

        //editor try add new user
        SecurityUtils.setCurrentUser(new UserDetails(tUser2));
        sut.addUserIntoMeetingMap(meeting, tUser3, Role.MEMBER);

        assertEquals(3, meeting.getUsersMap().size());

    }

    @Test
    public void removeUserFromMeetingWithUser() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        final RegisteredUser tUser2 = Generator.generateUser2();
        final RegisteredUser tUser3 = Generator.generateUser3();

        userSut.persist(tUser2);
        userSut.persist(tUser3);

        sut.addUserIntoMeetingMap(meeting, tUser2, Role.MEMBER);
        sut.addUserIntoMeetingMap(meeting, tUser3, Role.EDITOR);
        assertEquals(3, meeting.getUsersMap().size());

        sut.removeUserFromMeetingMap(meeting, tUser2);
        assertEquals(2, meeting.getUsersMap().size());

        sut.addUserIntoMeetingMap(meeting, tUser2, Role.EDITOR);
        assertEquals(3, meeting.getUsersMap().size());

    }

    @Test
    public void addChoiceIntoMeeting() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        final Choice choice1 = new Choice();
        choice1.setPoints(5);
        final Choice choice2 = new Choice();
        choice1.setPoints(8);

        sut.addChoiceToMeeting(meeting, choice1);
        sut.addChoiceToMeeting(meeting, choice2);
        assertEquals(2, meeting.getChoices().size());
    }

    @Test
    public void addChoiceIntoMeetingWithDlId() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));
        final RegisteredUser tUser2 = Generator.generateUser2();
        final RegisteredUser tUser3 = Generator.generateUser3();

        final Choice choice1 = new Choice();
        choice1.setPoints(7);
        final Choice choice2 = new Choice();
        choice2.setPoints(9);

        sut.addChoiceToMeeting(meeting, choice1);
        sut.addChoiceToMeeting(meeting, choice2);
        assertEquals(2, meeting.getChoices().size());
    }

    @Test
    public void removeChoiceFromMeeting() {
        RegisteredUser tUser = Generator.generateUser1();
        SecurityUtils.setCurrentUser(new UserDetails(tUser));
        final Choice choice1 = new Choice();
        choice1.setId(5);
        choice1.setPoints(5);
        final Choice choice2 = new Choice();
        choice2.setId(6);
        choice2.setPoints(3);

        em.persist(choice1);
        em.persist(choice2);

        sut.addChoiceToMeeting(meeting, choice1);
        sut.addChoiceToMeeting(meeting, choice2);
        assertEquals(2, meeting.getChoices().size());

        sut.removeChoiceFromMeeting(meeting, choice1);
        assertEquals(1, meeting.getChoices().size());

        //remove user which is not in the meeting
        try {
            final Choice choice = choiceSut.find(1);
            sut.removeChoiceFromMeeting(meeting, choice);
        } catch (NullPointerException ex) {
            System.out.println("Null pointer exception, choice not exits");
        }
        sut.removeChoiceFromMeeting(meeting, choice1);

        assertEquals(1, meeting.getChoices().size());
    }

    @Test
    public void addDateLocationIntoMeeting() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));
        RegisteredUser tUser3 = Generator.generateUser3();
        RegisteredUser tUser2 = Generator.generateUser2();
        em.persist(tUser3);
        em.persist(tUser2);
        sut.addUserIntoMeetingMap(meeting, tUser2, Role.EDITOR);

        final DateLocation choice1 = new DateLocation();
        final DateLocation choice2 = new DateLocation();
        final DateLocation choice3 = new DateLocation();

        // creator add datelocation
        sut.addDateLocationToMeeting(meeting, choice1);
        assertEquals(1, meeting.getPossibleDateLocations().size());

        // user, who is not in the meeting, can't add datelocation
        try {
            SecurityUtils.setCurrentUser(new UserDetails(tUser3));
            sut.addDateLocationToMeeting(meeting, choice2);
            fail("AccessDeniedException should have been thrown here!");
        } catch (AccessDeniedException ex) {
        }

        // editor add datelocation
        try {
            SecurityUtils.setCurrentUser(new UserDetails(tUser2));
            sut.addDateLocationToMeeting(meeting, choice2);
        } catch (AccessDeniedException ex) {
            fail("AccessDeniedException thrown but shouldn't have!");
        }

        sut.addUserIntoMeetingMap(meeting, tUser3, Role.MEMBER);

        // basic member can't add datelocation
        try {
            SecurityUtils.setCurrentUser(new UserDetails(tUser3));
            sut.addDateLocationToMeeting(meeting, choice3);
            fail("AccessDeniedException should have been thrown here!");
        } catch (AccessDeniedException ex) {
        }
    }

    @Test
    public void removeDateLocationFromMeeting() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        final DateLocation dl1 = Generator.generateDateLocation();
        final DateLocation dl2 = Generator.generateDateLocation();
        em.persist(dl1);
        em.persist(dl2);

        sut.addDateLocationToMeeting(meeting, dl1);
        sut.addDateLocationToMeeting(meeting, dl2);
        assertEquals(2, meeting.getPossibleDateLocations().size());

        sut.removeDateLocationFromMeeting(meeting, dl1);

        assertEquals(1, meeting.getPossibleDateLocations().size());

    }

    @Test
    public void meetingCalcBestDt() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        //generate users
        final User tUser2 = Generator.generateUser2();
        em.persist(tUser2);
        meeting.addUserToMeetingMap(tUser2, Role.MEMBER);

        final User tUser3 = Generator.generateUser3();
        em.persist(tUser3);
        meeting.addUserToMeetingMap(tUser3, Role.MEMBER);

        final User tUser4 = Generator.generateUnUser1();
        em.persist(tUser4);
        meeting.addUserToMeetingMap(tUser4, Role.MEMBER);

        //generate dateLocations
        final DateLocation dl1 = Generator.generateDateLocation();
        em.persist(dl1);
        meeting.addDatelocationToPossibleDateLocations(dl1);

        final DateLocation dl2 = Generator.generateDateLocation();
        em.persist(dl2);
        meeting.addDatelocationToPossibleDateLocations(dl2);

        final DateLocation dl3 = Generator.generateDateLocation();
        em.persist(dl3);
        meeting.addDatelocationToPossibleDateLocations(dl3);

        final DateLocation dl4 = Generator.generateDateLocation();
        em.persist(dl4);
        meeting.addDatelocationToPossibleDateLocations(dl4);

        assertEquals(4, meeting.getPossibleDateLocations().size());

        meeting.addChoice(new Choice(5, tUser4.getAccount(), meeting, dl4));
        meeting.addChoice(new Choice(7, tUser2.getAccount(), meeting, dl3));
        meeting.addChoice(new Choice(3, tUser3.getAccount(), meeting, dl1));
        meeting.addChoice(new Choice(1, tUser3.getAccount(), meeting, dl4));
        meeting.addChoice(new Choice(10, tUser4.getAccount(), meeting, dl2));
        meeting.addChoice(new Choice(5, tUser3.getAccount(), meeting, dl4));

        List<DateLocation> result = sut.CalcBestDateLocation(meeting);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dl4, result.get(0));

        meeting.addChoice(new Choice(1, tUser4.getAccount(), meeting, dl2));
        result = sut.CalcBestDateLocation(meeting);

        assertEquals(2, result.size());
        assertTrue(result.contains(dl4));
        assertTrue(result.contains(dl2));
    }

    @Test
    public void createNewMeetingFromParatemetersTest() {
        RegisteredUser tUser = Generator.generateUser1();
        em.persist(tUser);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        Meeting testMeeting = sut.createMeetingFromParameters(tUser.getId(), "test desc", "test");

        Meeting result = sut.find(testMeeting.getId());
        assertNotNull(result);
        assertEquals(testMeeting, result);

        UnregisteredUser uUser = Generator.generateUnUser1();
        em.persist(uUser);

        Meeting testMeeting2 = sut.createMeetingFromParameters(uUser.getId(), "test desc 2", "test");
        Meeting result2 = sut.find(testMeeting2.getId());
        assertNotNull(result2);
        assertEquals(testMeeting2, result2);

    }

    @Test
    public void addChoiceToMeetingFromParametersTest() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        RegisteredUser tUser3 = Generator.generateUser3();
        RegisteredUser tUser2 = Generator.generateUser2();
        em.persist(tUser2);
        em.persist(tUser3);
        DateLocation dl1 = Generator.generateDateLocation();
        DateLocation dl2 = Generator.generateDateLocation();

        sut.addDateLocationToMeeting(meeting, dl2);
        sut.addDateLocationToMeeting(meeting, dl1);

        sut.addUserIntoMeetingMap(meeting, tUser3, Role.MEMBER);

        sut.addChoiceToMeeting(meeting, dl2.getId(), 10);
        Meeting result = sut.find(meeting.getId());
        assertEquals(1, result.getChoices().size());

        //user which is not in the meeting cannot add choice
        try {
            SecurityUtils.setCurrentUser(new UserDetails(tUser2));
            sut.addChoiceToMeeting(meeting, dl2.getId(), 7);
            fail("AccessDeniedException should have been thrown here!");
        } catch (AccessDeniedException ex) {
        }

        //member also can add new choice
        try {
            SecurityUtils.setCurrentUser(new UserDetails(tUser3));
            sut.addChoiceToMeeting(meeting, dl2.getId(), 2);
        } catch (AccessDeniedException ex) {
            fail("AccessDeniedException thrown but should have not been!");
        }
    }

    @Test
    public void removeChoiceFromMeetingTestSec() {
        final RegisteredUser tUser = userSut.findById(1);
        SecurityUtils.setCurrentUser(new UserDetails(tUser));

        RegisteredUser tUser3 = Generator.generateUser3();
        RegisteredUser tUser2 = Generator.generateUser2();
        em.persist(tUser3);
        em.persist(tUser2);
        sut.addUserIntoMeetingMap(meeting, tUser3, Role.MEMBER);
        sut.addUserIntoMeetingMap(meeting, tUser2, Role.EDITOR);

        DateLocation dl2 = Generator.generateDateLocation();

        Choice choice1 = new Choice(2, tUser.getAccount(), meeting, dl2);
        choice1.setId(1);
        Choice choice2 = new Choice(2, tUser2.getAccount(), meeting, dl2);
        choice2.setId(2);
        Choice choice3 = new Choice(2, tUser3.getAccount(), meeting, dl2);
        choice3.setId(3);
        Choice choice4 = new Choice(2, tUser2.getAccount(), meeting, dl2);
        choice4.setId(4);

        sut.addChoiceToMeeting(meeting, choice4);
        sut.addChoiceToMeeting(meeting, choice3);
        sut.addChoiceToMeeting(meeting, choice2);
        sut.addChoiceToMeeting(meeting, choice1);

        Meeting result = sut.find(meeting.getId());
        assertEquals(4, result.getChoices().size());

        //member role user
        SecurityUtils.setCurrentUser(new UserDetails(tUser3));
        try {
            sut.removeChoiceFromMeetingWithId(meeting, choice2.getId());
            fail("AccessDeniedException should have been thrown here!");
        } catch (AccessDeniedException e) {
        }

        try {
            sut.removeChoiceFromMeetingWithId(meeting, choice3.getId());
        } catch (AccessDeniedException e) {
            fail("AccessDeniedException thrown but should not have been!");
        }

        //editor user role
        SecurityUtils.setCurrentUser(new UserDetails(tUser2));
        sut.removeChoiceFromMeetingWithId(meeting, choice2.getId());
        result = sut.find(meeting.getId());
        assertEquals(2, result.getChoices().size());
        sut.removeChoiceFromMeetingWithId(meeting, choice3.getId());
        result = sut.find(meeting.getId());
        assertEquals(2, result.getChoices().size());

        //creator user role
        SecurityUtils.setCurrentUser(new UserDetails(tUser));
        sut.removeChoiceFromMeetingWithId(meeting, choice1.getId());
        result = sut.find(meeting.getId());
        assertEquals(1, result.getChoices().size());
        sut.removeChoiceFromMeetingWithId(meeting, choice4.getId());
        result = sut.find(meeting.getId());
        assertEquals(0, result.getChoices().size());
    }
}
