/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.AccountDao;
import cz.cvut.fel.ear.meetingscheduler.dao.RegisteredUserDao;
import cz.cvut.fel.ear.meetingscheduler.exception.PersistenceException;
import cz.cvut.fel.ear.meetingscheduler.model.Account;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@Service
public class RegisteredUserService {

    private final AccountDao accountDao;

    private final RegisteredUserDao dao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisteredUserService(AccountDao accountDao, RegisteredUserDao dao, PasswordEncoder passwordEncoder) {
        this.accountDao = accountDao;
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void persist(RegisteredUser user) {
        Objects.requireNonNull(user);

        user.encodePassword(passwordEncoder);
        dao.persist(user);
    }

    @Transactional
    public RegisteredUser createRegisteredUserFromParameteres(String username, String email, String password, String firstname, String lastname) {
        if (dao.findByString(email) == null && dao.findByString(username) == null) {
            final Account newAccunt = new Account();
            accountDao.persist(newAccunt);
            final RegisteredUser newUser = new RegisteredUser();
            newUser.setAccount(newAccunt);
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setFirstname(firstname);
            newUser.setLastname(lastname);
            newUser.setPassword(password);
            newUser.encodePassword(passwordEncoder);

            dao.persist(newUser);

            return newUser;
        }
        throw new PersistenceException("User already exists!");
    }

    @Transactional(readOnly = true)
    public RegisteredUser findById(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public RegisteredUser findByString(String email) {
        return dao.findByString(email);
    }

    @Transactional(readOnly = true)
    public boolean exists(String username) {
        return dao.findByString(username) != null;
    }
}
