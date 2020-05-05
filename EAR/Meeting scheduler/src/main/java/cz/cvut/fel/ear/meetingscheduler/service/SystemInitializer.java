/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author patrik
 */
public class SystemInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(SystemInitializer.class);

    private final RegisteredUserService regUserService;

    private final PlatformTransactionManager txManager;

    @Autowired
    public SystemInitializer(RegisteredUserService userService,
            PlatformTransactionManager txManager) {
        this.regUserService = userService;
        this.txManager = txManager;
    }

    @PostConstruct
    private void initSystem() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute((status) -> {
            generateRegisteredUser();
            return null;
        });
    }

    private void generateRegisteredUser() {
        if (regUserService.exists("ADAM")) {
            return;
        }

        final RegisteredUser adam = new RegisteredUser();
        adam.setUsername("ADAM");
        adam.setFirstname("Adam");
        adam.setLastname("First user");
        adam.setEmail("adam@test.com");
        adam.setId(1);
        adam.setPassword("Aa123");
        LOG.info("Generated first user " + adam.getUsername() + "/" + adam.getPassword());
        regUserService.persist(adam);
    }
}
