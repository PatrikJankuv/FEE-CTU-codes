/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.AgendaDao;
import cz.cvut.fel.ear.meetingscheduler.dao.ChoiceDao;
import cz.cvut.fel.ear.meetingscheduler.dao.DateLocationDao;
import cz.cvut.fel.ear.meetingscheduler.dao.MeetingDao;
import cz.cvut.fel.ear.meetingscheduler.dao.RegisteredUserDao;
import cz.cvut.fel.ear.meetingscheduler.dao.UnregisteredUserDao;
import cz.cvut.fel.ear.meetingscheduler.dao.UserDao;
import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.model.Account;
import cz.cvut.fel.ear.meetingscheduler.model.Agenda;
import cz.cvut.fel.ear.meetingscheduler.model.Choice;
import cz.cvut.fel.ear.meetingscheduler.model.DateLocation;
import cz.cvut.fel.ear.meetingscheduler.model.Meeting;
import cz.cvut.fel.ear.meetingscheduler.model.PollState;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.model.User;
import cz.cvut.fel.ear.meetingscheduler.security.SecurityUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@Service
public class MeetingService {

    private final MeetingDao dao;

    private final AgendaDao agendaDao;

    private final UserDao userDao;

    private final RegisteredUserDao regUserDao;

    private final DateLocationDao dlDao;

    private final ChoiceDao choiceDao;
    private final UnregisteredUserDao unregUserDao;

    @Autowired
    public MeetingService(MeetingDao dao, AgendaDao agendaDao, UserDao userDao,
            DateLocationDao dlDao, ChoiceDao choicaDao, RegisteredUserDao registeredUserDao,
            UnregisteredUserDao unregUserDao) {
        this.dao = dao;
        this.agendaDao = agendaDao;
        this.userDao = userDao;
        this.dlDao = dlDao;
        this.choiceDao = choicaDao;
        this.regUserDao = registeredUserDao;
        this.unregUserDao = unregUserDao;
    }

    @Transactional(readOnly = true)
    public List<Meeting> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Meeting find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public Choice findChoice(Integer id) {
        return choiceDao.find(id);
    }

    @Transactional
    public void persist(Meeting meeting) {
        Objects.requireNonNull(meeting);
        dao.persist(meeting);
    }

    /**
     *
     * @param meeting for dateLocation need to be calculate
     * @return dateLocation with the most points, can be more than just one
     */
    public List<DateLocation> CalcBestDateLocation(Meeting meeting) {
        List<Choice> choices = meeting.getChoices();
        HashMap<DateLocation, Integer> dl = new HashMap<>();

        if (choices == null) {
            return null;
        }

        //sum points for every datelocation
        choices.forEach((c) -> {
            if (!dl.containsKey(c.getDateLocation())) {
                dl.put(c.getDateLocation(), c.getPoints());
            } else {
                dl.put(c.getDateLocation(), dl.get(c.getDateLocation()) + c.getPoints());
            }
        });

        List<DateLocation> maxIdList = new ArrayList<>();
        int maxPoints = 0;

        //chose the biggest one
        for (Map.Entry<DateLocation, Integer> entry : dl.entrySet()) {
            if (entry.getValue() > maxPoints) {
                maxPoints = entry.getValue();
                maxIdList.clear();
                maxIdList.add(entry.getKey());
            } else if (entry.getValue() == maxPoints) {
                maxIdList.add(entry.getKey());
            }
        }

        return maxIdList;
    }

    @Transactional
    public Meeting createMeeting(User user) {
        Objects.requireNonNull(user);
        final Meeting newMeeting = new Meeting();

        dao.persist(newMeeting);
        return newMeeting;
    }

    @Transactional
    @PreAuthorize("this.userCanCreate(#userId, principal.toString())")
    public Meeting createMeetingFromParameters(Integer userId, String description, String name) {
        //find user
        Objects.requireNonNull(userId);
        final User autorUser = userDao.find(userId);
        Objects.requireNonNull(autorUser);
        //create new meeting and agenda for meeting
        final Meeting newMeeting = new Meeting();
        final Agenda agenda = new Agenda();

        agendaDao.persist(agenda);

        newMeeting.addUserToMeetingMap(autorUser, Role.CREATOR);
        newMeeting.setAgenda(agenda);
        newMeeting.setDescription(description);
        newMeeting.setName(name);
        newMeeting.setPollState(PollState.ACTIVE);

        dao.persist(newMeeting);
        return newMeeting;
    }

    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting)")
    public void addUserIntoMeetingMap(Meeting meeting, User user, Role role) {
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);
        meeting.addUserToMeetingMap(user, role);
        dao.update(meeting);
    }

    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting)")
    public void removeUserFromMeetingMap(Meeting meeting, User user) {
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(user);

        meeting.removeUserFromMeetingMap(user);
        dao.update(meeting);
    }

    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting)")
    public void setStatusOfMeeting(Meeting meeting, PollState state) {
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(state);
        meeting.setPollState(state);
        dao.update(meeting);
    }

    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting)")
    public void addDateLocationToMeeting(Meeting meeting, DateLocation dl) {
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(dl);
        dlDao.persist(dl);
        meeting.addDatelocationToPossibleDateLocations(dl);
        dao.update(meeting);
    }

    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting)")
    public void removeDateLocationFromMeeting(Meeting meeting, DateLocation dl
    ) {
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(dl);
        meeting.removeDateLocationFromPossibleDateLocation(dl);
        dao.update(meeting);
    }

    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting)")
    public void addChoiceToMeeting(Meeting meeting, Choice choice
    ) {
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(choice);
        meeting.addChoice(choice);
        dao.update(meeting);
    }

    /**
     * Remove datelocation from meeting, Only Creator or Editor can remove
     * dateloaction
     *
     * @param meeting
     * @param dlId
     */
    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting)")
    public void removeDateLocationFromMeetingWithId(Meeting meeting, Integer dlId
    ) {
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(dlId);
        DateLocation dl = dlDao.find(dlId);
        if (dl == null) {
            throw new NotFoundException();
        }
        meeting.removeDateLocationFromPossibleDateLocation(dl);
        dao.update(meeting);
    }

    @Transactional
    @PreAuthorize("this.userIsMember(principal.toString(), #meeting)"
            + " && this.userCanCreate(#userId, principal.toString())")
    public Choice addChoiceToMeetingWithId(Meeting meeting, Integer userId,
            Integer idDl, Integer points) {
        DateLocation dl;
        //check if meeting contain selected dateLocation
        if (meeting.getPossibleDateLocations().contains(dlDao.find(idDl))) {
            dl = dlDao.find(idDl);
        } else {
            return null;
        }
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(dl);

        final User autorUser = userDao.find(userId);

        Objects.requireNonNull(autorUser);
        Account account = autorUser.getAccount();

        //create new instance of choice
        Choice choice = new Choice(points, account, meeting, dl);
        System.out.println("choice " + choice.getId() + " " + choice.getPoints());
        choiceDao.persist(choice);
        meeting.addChoiceToChoices(choice);
        dao.update(meeting);

        return choice;
    }

    @Transactional
    @PreAuthorize("this.userIsMember(principal.toString(), #meeting)")
    public Choice addChoiceToMeeting(Meeting meeting, Integer idDl,
            Integer points
    ) {
        DateLocation dl;
        //check if meeting contain selected dateLocation
        if (meeting.getPossibleDateLocations().contains(dlDao.find(idDl))) {
            dl = dlDao.find(idDl);
        } else {
            throw NotFoundException.create("DateLocation", idDl);
        }

        final RegisteredUser curUser = SecurityUtils.getCurrentUser();

        Objects.requireNonNull(meeting);
        Objects.requireNonNull(curUser);
        Objects.requireNonNull(dl);

        Objects.requireNonNull(curUser);
        Account account = userDao.find(curUser.getId()).getAccount();

        //create new instance of choice
        Choice choice = new Choice(points, account, meeting, dl);
        for (Choice c : meeting.getChoices()) {
            if (choice.getDateLocation().equals(c.getDateLocation())
                    && choice.getUser().equals(c.getUser())) {
                c.setPoints(points);
                return choiceDao.update(c);
            }
        }
        choiceDao.persist(choice);
        meeting.addChoiceToChoices(choice);
        dao.update(meeting);

        return choice;
    }

    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting) "
            + "|| this.userCanEditChoice(principal.toString(), #choice.getId())")
    public void removeChoiceFromMeeting(Meeting meeting, Choice choice
    ) {
        Objects.requireNonNull(meeting);
        Objects.requireNonNull(choice);
        meeting.removeChoiceFromChoices(choice);
        dao.update(meeting);
    }

    /**
     * Choice from meeting can remove only creator, editor or author of choice
     *
     * @param meeting
     * @param choiceId id of choice
     */
    @Transactional
    @PreAuthorize("this.userCanEditMeeting(principal.toString(), #meeting)"
            + " || this.userCanEditChoice(principal.toString(), #choiceId)")
    public void removeChoiceFromMeetingWithId(Meeting meeting, Integer choiceId
    ) {
        Objects.requireNonNull(meeting);
        Choice choice = choiceDao.find(choiceId);

        Objects.requireNonNull(choice);
        meeting.removeChoiceFromChoices(choice);
        dao.update(meeting);
    }

    public boolean userCanEditMeeting(String username, Meeting meeting) {
        RegisteredUser user = regUserDao.findByString(username);
        if (user == null) {
            return false;
        }
        Role role = meeting.getUsersMap().get(user.getAccount());
        return role == Role.EDITOR || role == Role.CREATOR;
    }

    public boolean userCanEditChoice(String username, Integer choiceId) {
        Choice choice = choiceDao.find(choiceId);
        RegisteredUser user = regUserDao.findByString(username);
        Objects.requireNonNull(user);
        Objects.requireNonNull(choice);
        return choice.getUser().equals(user.getAccount());
    }

    public boolean userIsMember(String username, Meeting meeting) {
        RegisteredUser user = regUserDao.findByString(username);
        if (user == null) {
            return false;
        }
        Role role = meeting.getUsersMap().get(user.getAccount());
        return role != null;
    }

    public boolean userCanCreate(Integer userId, String principalUsername) {
        RegisteredUser user = regUserDao.find(userId);
        boolean isCorrectUser = user != null && user.getUsername() == principalUsername;
        boolean isUnregisteredUser = unregUserDao.find(userId) != null;

        return isUnregisteredUser || isCorrectUser;
    }

}
