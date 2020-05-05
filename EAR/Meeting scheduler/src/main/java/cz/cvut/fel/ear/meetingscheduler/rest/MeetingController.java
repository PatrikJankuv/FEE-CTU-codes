/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.dto.MeetingCreationDto;
import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.model.Agenda;
import cz.cvut.fel.ear.meetingscheduler.model.AgendaPoint;
import cz.cvut.fel.ear.meetingscheduler.model.Choice;
import cz.cvut.fel.ear.meetingscheduler.model.DateLocation;
import cz.cvut.fel.ear.meetingscheduler.model.Meeting;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.model.User;
import cz.cvut.fel.ear.meetingscheduler.rest.util.RestUtils;
import cz.cvut.fel.ear.meetingscheduler.service.AgendaPointService;
import cz.cvut.fel.ear.meetingscheduler.service.AgendaService;
import cz.cvut.fel.ear.meetingscheduler.service.MeetingService;
import cz.cvut.fel.ear.meetingscheduler.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Matej
 */
@RestController
@RequestMapping("/rest/meetings")
public class MeetingController {

    private static final Logger LOG = LoggerFactory.getLogger(MeetingController.class);

    private final MeetingService meetingService;
    private final AgendaService agendaService;
    private final AgendaPointService pointService;
    private final UserService userService;

    @Autowired
    public MeetingController(MeetingService meetingService, AgendaService agendaService, AgendaPointService pointService, UserService userService) {
        this.meetingService = meetingService;
        this.agendaService = agendaService;
        this.pointService = pointService;
        this.userService = userService;
    }

    /**
     * Return meeting
     *
     * @param id of meeting
     * @return meeting
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meeting getById(@PathVariable Integer id) {
        final Meeting meeting = meetingService.find(id);
        if (meeting == null) {
            throw NotFoundException.create("Meeting", id);
        }

        // SORT DATELOCATIONS
        if (meeting.getPossibleDateLocations() != null) {
            meeting.setPossibleDateLocations(
                    meeting.getPossibleDateLocations().stream()
                            .sorted((DateLocation o1, DateLocation o2) -> o1.getDatetime().compareTo(o2.getDatetime()))
                            .collect(Collectors.toList()));
        }

        // SORT CHOICES
        if (meeting.getChoices() != null) {
            meeting.setChoices(
                    meeting.getChoices().stream()
                            .sorted((o1, o2) -> o2.getPoints().compareTo(o1.getPoints())
                            ).collect(Collectors.toList())
            );
        }

        return meeting;
    }

    /**
     * Return specific choice, indecently on meeting
     *
     * @param choiceId of choice
     * @return choice
     */
    @GetMapping(value = "/choices/{choiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Choice getChoiceId(@PathVariable Integer choiceId) {
        final Choice ch = meetingService.findChoice(choiceId);
        if (ch == null) {
            throw NotFoundException.create("Meeting", choiceId);
        }
        return ch;
    }

    /**
     * Return agenda
     *
     * @param id of meeting
     * @return agenda
     */
    @GetMapping(value = "/{id}/agenda", produces = MediaType.APPLICATION_JSON_VALUE)
    public Agenda getAgendaForMeetingById(@PathVariable Integer id) {
        final Meeting meeting = meetingService.find(id);

        if (meeting == null) {
            throw NotFoundException.create("Meeting", id);
        }
        final Agenda agenda = meeting.getAgenda();
        final Agenda agendaResult = agendaService.find(agenda.getId());

        if (agendaResult == null) {
            throw NotFoundException.create("Agenda", id);
        }
        return agendaResult;
    }

    /**
     * Add agenda point to agenda
     *
     * @param id of meeting
     * @param point body of agenda point
     * @return
     */
    @PostMapping(value = "/{id}/agendapoints", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addAgendaPoint(@PathVariable Integer id, @RequestBody AgendaPoint point) {
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        final Meeting meeting = meetingService.find(id);

        try {
            final Agenda agenda = meeting.getAgenda();
            agendaService.addAgendaPoint(agenda, point);
        } catch (Exception e) {
            return new ResponseEntity<>(headers, HttpStatus.NOT_ACCEPTABLE);
        }
        LOG.debug("AgendaPoint {} successfully created", point);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);

    }

    @DeleteMapping(value = "/{id}/points/{pointId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAgendaPointFromAgenda(@PathVariable Integer id, @PathVariable Integer pointId) {
        final Meeting meeting = meetingService.find(id);
        final Agenda agenda = meeting.getAgenda();

        if (agenda == null) {
            return;
        }

        AgendaPoint agendaPoint = pointService.find(pointId);
        agendaService.removeAgendaPoint(agenda, agendaPoint);

        LOG.debug("Removed agendaPoint {}", agendaPoint);

    }

    /**
     * Return dateLocation with the most points
     *
     * @param id of meeting
     * @return list of dateLocations
     */
    @GetMapping(value = "/{id}/best_datelocation", produces = MediaType.APPLICATION_JSON_VALUE)
    public DateLocation getBestDateLocationForMeeting(@PathVariable Integer id) {
        final Meeting meeting = meetingService.find(id);

        if (meeting == null) {
            throw NotFoundException.create("Meeting", id);
        }

        final List<DateLocation> best = meetingService.CalcBestDateLocation(meeting);

        if (best == null || best.isEmpty()) {
            throw NotFoundException.create("Best datelocation", id);
        }
        return best.get(0);
    }

    /**
     * Create new meeting, from data in URL
     *
     *
     * @param userid id of creator
     * @param description of meeting
     * @param name of meeting
     * @return new Meeting object
     */
    @PostMapping()
    public ResponseEntity<Void> createNewMeetingFromParameters(@RequestBody MeetingCreationDto params) {
        final Meeting newMeeting;
        newMeeting = meetingService.createMeetingFromParameters(params.getUserId(),
                params.getDescription(), params.getName());
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", newMeeting.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Add user to meeting and set role MEMBER
     *
     * @param id of Group
     * @param userid id of user
     * @return
     */
    @PostMapping(value = "/{id}/members/")
    public ResponseEntity<Void> addMemberUserToMeeting(@PathVariable Integer id, @RequestBody User u) {
        final Meeting meeting = this.meetingService.find(id);

        LOG.debug("User {} added to meeting {}", u.getId(), meeting);
        meetingService.addUserIntoMeetingMap(meetingService.find(id), userService.find(u.getId()), Role.MEMBER);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/members/{memId}")
    public ResponseEntity<Void> deleteMemberFromMeeting(@PathVariable Integer id, @PathVariable Integer memId) {
        final Meeting meeting = meetingService.find(id);
        if (meeting == null) {
            throw NotFoundException.create("Meeting", id);
        }
        final User user = userService.find(memId);
        if (user == null) {
            throw NotFoundException.create("User", id);
        }
        meetingService.removeUserFromMeetingMap(meeting, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Add user to meeting and set role EDITOR
     *
     * @param id of Group
     * @param userid id of user
     * @return
     */
    @PostMapping(value = "/{id}/editors/{userid}")
    public ResponseEntity<Void> addEditorUserToMeeting(@PathVariable Integer id, @PathVariable Integer userid) {
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");

        try {
            final Meeting meeting = this.meetingService.find(id);

            LOG.debug("User {} added to meeting {}", userid, meeting);
            meetingService.addUserIntoMeetingMap(this.meetingService.find(id), this.userService.find(userid), Role.EDITOR);
        } catch (Exception e) {
            return new ResponseEntity<>(headers, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    /**
     * Add datelocation to meeting
     *
     * @param id of Group
     * @param dl json body of datelocation, will create new object from this
     * data
     */
    @PostMapping(value = "/{id}/datelocations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> addDateLocation(@PathVariable Integer id, @RequestBody DateLocation dl) {
        final Meeting meeting = getById(id);
        meetingService.addDateLocationToMeeting(meeting, dl);

        LOG.debug("Datelocation {} added to meeting {}", dl, meeting);

        HttpHeaders headers
                = RestUtils.createLocationHeaderFromCurrentUri("/{id}", dl.getId());

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Create choice in the meeting
     *
     * @param id of Meeting
     * @param dlId id of Datelocation, which was selected
     * @param points points for DateLocation
     * @return
     */
    @PostMapping(value = "{id}/choices/")
    public ResponseEntity<Void> addChoiceToMeeting(@PathVariable Integer id, @RequestBody Choice ch) {

        final Meeting newMeeting = getById(id);

        Choice newChoice = meetingService.addChoiceToMeeting(newMeeting,
                ch.getDateLocation().getId(), ch.getPoints());

        LOG.debug("User {} created new choice");
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", newChoice.getId());
        return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);
    }

    /**
     * Remove datelocation from meeting
     *
     * @param id of meeting
     * @param dlId id of datelocation, which should be remove
     */
    @DeleteMapping(value = "/{id}/datelocations/{dlId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeDateLocation(@PathVariable Integer id, @PathVariable Integer dlId) {
        final Meeting meeting = getById(id);
        meetingService.removeDateLocationFromMeetingWithId(meeting, dlId);

        LOG.debug("DateLocation {} removed from meeting {}", dlId, meeting);
    }

    /**
     * Remove choice from meeting
     *
     * @param id of Meeting
     * @param choiceId id of Choice, which should be remove
     */
    @DeleteMapping(value = "/{id}/choices/{choiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeChoice(@PathVariable Integer id, @PathVariable Integer choiceId) {
        final Meeting meeting = getById(id);
        meetingService.removeChoiceFromMeetingWithId(meeting, choiceId);

        LOG.debug("DateLocation {} removed from meeting {}", choiceId, meeting);
    }

    @GetMapping(value = "/{id}/datelocations")
    public List<DateLocation> getAllDateLocations(@PathVariable Integer id) {
        final Meeting meeting = getById(id);
        return meeting == null
                ? Collections.EMPTY_LIST
                : meeting.getPossibleDateLocations();
    }

}
