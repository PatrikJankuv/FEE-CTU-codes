package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.UserGroup;
import org.springframework.stereotype.Repository;

@Repository
public class UserGroupDao extends BaseDao<UserGroup> {

    public UserGroupDao() {
        super(UserGroup.class);
    }

    public UserGroup findById(Integer id) {
        return em.createNamedQuery("UserGroup.find", UserGroup.class).setParameter("id", id).getSingleResult();
    }
}
