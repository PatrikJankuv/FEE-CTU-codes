package cz.cvut.fel.ear.meetingscheduler.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "message")
@NamedQueries({
    @NamedQuery(name = "Message.findConversation", query = "SELECT m FROM Message m WHERE (m.sender = :sender AND m.recipient = :recipient) OR (m.sender = :recipient AND m.recipient = :sender)")
    ,
        @NamedQuery(name = "Message.findUserMessages", query = "SELECT m FROM Message m WHERE m.sender = :user OR m.recipient = :user"),})
public class Message extends AbstractEntity implements Serializable {

    @Column(nullable = false)
    private String body;

    private String subject;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = true)
    @JoinColumn(nullable = false)
    public RegisteredUser sender;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = true)
    @JoinColumn(nullable = false)
    public RegisteredUser recipient;

    public Message() {
    }

    public Message(String body, String subject, RegisteredUser sender, RegisteredUser recipient) {
        this.body = body;
        this.subject = subject;
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public RegisteredUser getSender() {
        return sender;
    }

    public void setSender(RegisteredUser sender) {
        this.sender = sender;
    }

    public RegisteredUser getRecipient() {
        return recipient;
    }

    public void setRecipient(RegisteredUser recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Message from sender=" + sender.getUsername() + ", subject=" + subject + " body=" + body + '}';
    }

}
