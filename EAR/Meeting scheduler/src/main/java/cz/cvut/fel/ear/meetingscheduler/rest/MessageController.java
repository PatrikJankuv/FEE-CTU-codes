/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.model.Message;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.rest.util.RestUtils;
import cz.cvut.fel.ear.meetingscheduler.service.MessageService;
import cz.cvut.fel.ear.meetingscheduler.service.RegisteredUserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Matej
 */
@RestController
@RequestMapping("/rest/message")
public class MessageController {

    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);

    private final MessageService service;

    private final RegisteredUserService regUserService;

    @Autowired
    public MessageController(MessageService service, RegisteredUserService regUserService) {
        this.service = service;
        this.regUserService = regUserService;
    }

    /**
     *
     * @param id of the UserGroup we want to get the details of
     * @return UserGroup of given id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Message getMessageById(@PathVariable Integer id) {
        final Message message = service.find(id);
        if (message == null) {
            throw NotFoundException.create("Message", id);
        }
        return message;
    }

    @GetMapping(value = "/{firstId}/{secondId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> getConverstion(@PathVariable Integer firstId, @PathVariable Integer secondId) {
        final RegisteredUser user1 = regUserService.findById(firstId);
        final RegisteredUser user2 = regUserService.findById(secondId);

        if (user1 == null) {
            throw NotFoundException.create("RegisteredUser", firstId);
        }
        if (user2 == null) {
            throw NotFoundException.create("RegisteredUser", secondId);
        }

        final List<Message> messages = service.findConversation(user1, user2);

        if (messages == null) {
            throw NotFoundException.create("Message", firstId);
        }

        return messages;
    }

    @GetMapping(value = "/usermessages/{firstId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> getMessages(@PathVariable Integer firstId) {
        final RegisteredUser user1 = regUserService.findById(firstId);

        if (user1 == null) {
            throw NotFoundException.create("RegisteredUser", firstId);
        }

        final List<Message> messages = service.findUserMessages(user1);

        if (messages == null) {
            throw NotFoundException.create("Message", firstId);
        }

        return messages;
    }

    /**
     * Creates message from the url, persists it in the database
     *
     * @param sendId id of sender
     * @param recId id of recipient
     * @param body body of message
     * @param subject subject of message
     * @return
     */
    @PostMapping(value = "/body/{body}/subject/{subject}/from/{sendId}/to/{recId}/")
    public ResponseEntity<Void> createMessage(@PathVariable Integer sendId, @PathVariable Integer recId, @PathVariable String body, @PathVariable String subject) {
        System.out.println("hoho");
        Message newMessage = new Message(body, subject, regUserService.findById(sendId), regUserService.findById(recId));
        System.out.println("ano");
        service.persist(newMessage);
        LOG.debug("Created message {}", newMessage);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", newMessage.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Creates message, persists it in the database, source file is json
     *
     * @param sendId id of sender
     * @param recId id of recipient
     * @param message
     * @return
     */
    @PostMapping(value = "sendMessage/{sendId}/{recId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessage(@PathVariable Integer sendId, @PathVariable Integer recId, @RequestBody Message message) {
        Message newMes = message;
        newMes.setRecipient(regUserService.findById(sendId));
        newMes.setSender(regUserService.findById(recId));

        service.persist(newMes);
        LOG.debug("Created message {}", newMes);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", newMes.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
