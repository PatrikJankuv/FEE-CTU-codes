/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.MessageDao;
import cz.cvut.fel.ear.meetingscheduler.model.Message;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@Service
public class MessageService {

    private final MessageDao dao;

    @Autowired
    public MessageService(MessageDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("returnObject.getSender().getUsername() == principal.username"
            + " || returnObject.getRecipient().getUsername() == principal.username")
    public Message find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("filterObject.getSender().getUsername() == principal.username"
            + " || filterObject.getRecipient().getUsername() == principal.username")
    public List<Message> findConversation(RegisteredUser userFirst, RegisteredUser userSecond) {
        return dao.findConversation(userFirst, userSecond);
    }

    @Transactional(readOnly = true)
    @PostFilter("filterObject.getSender().getUsername() == principal.username"
            + " || filterObject.getRecipient().getUsername() == principal.username")
    public List<Message> findUserMessages(RegisteredUser userFirst) {
        return dao.findMyMessages(userFirst);
    }

    @Transactional
    @PostAuthorize("#message.getSender().getUsername() == principal.username")
    public void persist(Message message) {
        Objects.requireNonNull(message);
        dao.persist(message);
    }

    @Transactional
    @PreAuthorize("#message.getSender().getUsername() == principal.username")
    public void createMessage(Message message) {
        Objects.requireNonNull(message);
        dao.persist(message);
    }

    @Transactional
    @PreAuthorize("#message.getSender().getUsername() == principal.username")
    public void removeMessage(Message message) {
        Objects.requireNonNull(message);
        dao.remove(message);
    }
}
