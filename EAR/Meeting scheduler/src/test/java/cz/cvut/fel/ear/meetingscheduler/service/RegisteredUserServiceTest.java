/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class RegisteredUserServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RegisteredUserService sut;

    @Test
    public void createRegisteredUserFromParameters() {
        RegisteredUser tUser = sut.createRegisteredUserFromParameteres("adam", "jan@email.com", "123", "adam", "");
        int tUserId = tUser.getId();

        RegisteredUser result = sut.findById(tUserId);
        assertNotNull(result);
        assertNotNull(result.getAccount());
        assertEquals("adam", result.getUsername());
        assertEquals("jan@email.com", result.getEmail());
        assertEquals("adam", result.getFirstname());
        assertEquals("", result.getLastname());
    }
}
