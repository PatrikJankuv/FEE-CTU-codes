/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.AccountDao;
import cz.cvut.fel.ear.meetingscheduler.dao.UnregisteredUserDao;
import cz.cvut.fel.ear.meetingscheduler.model.Account;
import cz.cvut.fel.ear.meetingscheduler.model.UnregisteredUser;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@Service
public class UnregisteredUserService {

    private final AccountDao accountDao;

    private final UnregisteredUserDao dao;

    @Autowired
    public UnregisteredUserService(AccountDao accountDao, UnregisteredUserDao dao) {
        this.accountDao = accountDao;
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public UnregisteredUser find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public UnregisteredUser createFromParameters(String email) {
        final Account newAccunt = new Account();
        accountDao.persist(newAccunt);

        final UnregisteredUser newUser = new UnregisteredUser();
        newUser.setAccount(newAccunt);
        newUser.setEmail(email);

        dao.persist(newUser);
        return newUser;
    }

    @Transactional(readOnly = true)
    public UnregisteredUser findByEmail(String username) {
        return dao.findByEmail(username);
    }

    @Transactional
    public void remove(UnregisteredUser user) {
        Objects.requireNonNull(user);
        dao.remove(user);
    }
}
