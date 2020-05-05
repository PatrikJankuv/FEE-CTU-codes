/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.model.UnregisteredUser;
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
public class UnregisteredUserServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private UnregisteredUserService sut;

    @Test
    public void createUnregisteredUserFromParameters() {
        UnregisteredUser tUser = sut.createFromParameters("email@email.com");
        int tUserId = tUser.getId();

        UnregisteredUser result = sut.find(tUserId);
        assertNotNull(result);
        assertNotNull(result.getAccount());
        assertEquals("email@email.com", result.getEmail());

        UnregisteredUser resultEmail = sut.findByEmail("email@email.com");
        assertNotNull(resultEmail);
        assertNotNull(resultEmail.getAccount());
        assertEquals("email@email.com", resultEmail.getEmail());
    }

}
