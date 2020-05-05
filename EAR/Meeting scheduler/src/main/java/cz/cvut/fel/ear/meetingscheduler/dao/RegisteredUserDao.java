package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import org.springframework.stereotype.Repository;

@Repository(value = "registeredUserDao")
public class RegisteredUserDao extends BaseDao<RegisteredUser> {

    public RegisteredUserDao() {
        super(RegisteredUser.class);
    }

    /**
     * can be use for search by email or username
     *
     * @param str string
     * @return RegisteredUser
     */
    public RegisteredUser findByString(String str) {

        try {
            if (str.contains("@")) {
                return em.createNamedQuery("RegisteredUser.findByEmail", RegisteredUser.class).setParameter("email", str).getResultList().get(0);
            } else {
                return em.createNamedQuery("RegisteredUser.findByUsername", RegisteredUser.class).setParameter("username", str).getResultList().get(0);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
