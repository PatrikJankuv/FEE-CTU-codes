package cz.cvut.fel.ear.meetingscheduler.service.security;

import cz.cvut.fel.ear.meetingscheduler.dao.RegisteredUserDao;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final RegisteredUserDao userDao;

    @Autowired
    public UserDetailsService(RegisteredUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final RegisteredUser user = userDao.findByString(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }
        return new cz.cvut.fel.ear.meetingscheduler.security.model.UserDetails(user);
    }
}
