package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.Message;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDao extends BaseDao<Message> {

    public MessageDao() {
        super(Message.class);
    }

    /**
     * Return all messages between two registered users
     *
     * @param recipient first RegisteredUser
     * @param sender second REgisteredUser
     * @return
     */
    public List<Message> findConversation(RegisteredUser recipient, RegisteredUser sender) {
        return em.createNamedQuery("Message.findConversation", Message.class).setParameter("recipient", recipient).setParameter("sender", sender).getResultList();
    }

    /**
     * Return all messages of user
     *
     * @param user RegisteredUser
     * @return
     */
    public List<Message> findMyMessages(RegisteredUser user) {
        return em.createNamedQuery("Message.findUserMessages", Message.class).setParameter("user", user).getResultList();
    }
}
