package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.model.GroupUser;
import cz.cvut.fel.ear.meetingscheduler.model.Message;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.rest.util.RestUtils;
import cz.cvut.fel.ear.meetingscheduler.security.SecurityUtils;
import cz.cvut.fel.ear.meetingscheduler.service.GroupUserService;
import cz.cvut.fel.ear.meetingscheduler.service.MessageService;
import cz.cvut.fel.ear.meetingscheduler.service.RegisteredUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@RestController
@RequestMapping("/rest/group")
public class GroupUserController {

    private static final Logger LOG = LoggerFactory.getLogger(GroupUserController.class);

    private final GroupUserService service;

    private final RegisteredUserService regUserService;

    private final MessageService messageService;

    @Autowired
    public GroupUserController(GroupUserService service, RegisteredUserService regUserService, MessageService messageService) {
        this.service = service;
        this.regUserService = regUserService;
        this.messageService = messageService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupUser getGroupById(@PathVariable Integer id) {
        final GroupUser group = service.find(id);
        if (group == null) {
            throw NotFoundException.create("Message", id);
        }
        return group;
    }

    /**
     * Return list of users in a group
     *
     * @param id of GroupUser
     * @return
     */
    @GetMapping(value = "/{id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<RegisteredUser, Role> getListOfUsers(@PathVariable Integer id) {
        final GroupUser group = service.find(id);
        if (group == null) {
            throw NotFoundException.create("Group", id);
        }

        return group.getUsersMap();
    }

    /**
     * Return list of users in a group
     *
     * @param id of GroupUser
     * @return
     */
    @GetMapping(value = "/{id}/userslist", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegisteredUser> getFirstUsers(@PathVariable Integer id) {
        final GroupUser group = service.find(id);
        if (group == null) {
            throw NotFoundException.create("Group", id);
        }
        //get map
        Map<RegisteredUser, Role> users = group.getUsersMap();
        //get key
        Set<RegisteredUser> keys = users.keySet();
        List<RegisteredUser> userList = new ArrayList<>();

        //add to list of users
        keys.forEach((k) -> {
            userList.add(regUserService.findById(k.getId()));
        });

        //return users in readable form
        return userList;
    }

    @PostMapping(value = "/{groupId}/user/{userId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserIntoGroup(@PathVariable Integer groupId, @PathVariable Integer userId, @RequestBody Role role) {
        final RegisteredUser user = regUserService.findById(userId);
        final GroupUser group = service.find(groupId);

        service.addUserToGroup(group, user, role);
        LOG.debug("User {} added into Group", user.getUsername(), group.getName());
    }

    /**
     * no tested
     *
     * @param id of User, who creates group
     * @param name
     * @param description
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> createGroup(@RequestBody GroupUser newGroup) {
        final RegisteredUser user = SecurityUtils.getCurrentUser();

        final GroupUser group = service.createNewGroupByUser(user, newGroup.getName(), newGroup.getDescription());

        LOG.debug("Created group {}", group);

        HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", group.getId());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGroup(@PathVariable Integer id) {
        final GroupUser toRemove = service.find(id);
        if (toRemove == null) {
            return;
        }

        service.removeGroup(toRemove);
        LOG.debug("Removed message {}", toRemove);

    }

    /**
     * Creates message and send all users, persists it in the database, source
     * file is json
     *
     * @param groupId id of group
     * @param senderId id of sender
     * @param message body of the message
     * @return
     */
    @PostMapping(value = "/{groupId}/sendMessage/{senderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessageAllUserS(@PathVariable Integer groupId, @PathVariable Integer senderId, @RequestBody Message message) {
        final GroupUser group = service.find(groupId);
        if (group == null) {
            throw NotFoundException.create("Message", groupId);
        }
        try {
            final RegisteredUser curUser = SecurityUtils.getCurrentUser();
            if (group.getUsersMap().containsKey(regUserService.findById(curUser.getId()))) {
                //get map
                Map<RegisteredUser, Role> users = group.getUsersMap();
                //get key - users
                Set<RegisteredUser> usersSet = users.keySet();

                //send every user message
                usersSet.forEach((RegisteredUser k) -> {
                    Message newMes = new Message();
                    newMes.setBody(message.getBody());
                    newMes.setSubject(message.getSubject());
                    newMes.setRecipient(regUserService.findById(k.getId()));
                    newMes.setSender(regUserService.findById(senderId));
                    System.out.println("REC " + newMes.getRecipient().getId());
                    System.out.println("SEN " + newMes.getSender().getId());
                    messageService.persist(newMes);
                    LOG.debug("Created message {}", newMes);
                });

                final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", group.getId());
                return new ResponseEntity<>(headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.debug("Bad credentials send message {}", group);

        }
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", group.getId());
        return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
    }
}
