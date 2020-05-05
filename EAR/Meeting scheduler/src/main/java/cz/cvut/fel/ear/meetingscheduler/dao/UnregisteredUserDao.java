package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.UnregisteredUser;
import org.springframework.stereotype.Repository;

@Repository
public class UnregisteredUserDao extends BaseDao<UnregisteredUser> {

    public UnregisteredUserDao() {
        super(UnregisteredUser.class);
    }

    public UnregisteredUser findByEmail(String str) {
        try {
            return em.createNamedQuery("UnregisteredUser.findByEmail", UnregisteredUser.class).setParameter("email", str).getResultList().get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
