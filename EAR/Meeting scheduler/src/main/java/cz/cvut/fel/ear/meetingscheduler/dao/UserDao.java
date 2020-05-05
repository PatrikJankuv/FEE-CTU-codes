package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.model.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends BaseDao<User> {

    @PersistenceContext
    private EntityManager em;

    public UserDao() {
        super(User.class);
    }

    public User findByEmail(String email) {
        List<User> users
                = em.createQuery("SELECT u FROM RegisteredUser u WHERE u.email = :email"
                        + "UNION SELECT u FROM UnregisteredUser u WHERE u.email = :email")
                        .setParameter("email", email)
                        .getResultList();
        if (users.isEmpty()) {
            throw new NotFoundException("User with given email not found");
        }
        return users.get(0);
    }

//    @Override
//    public User find(Integer id) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<User> findAll() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void persist(User entity) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void persist(Collection<User> entities) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public User update(User entity) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void remove(User entity) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public boolean exists(Integer id) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
