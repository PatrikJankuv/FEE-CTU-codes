/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.rest.handler.ErrorInfo;
import cz.cvut.fel.ear.meetingscheduler.service.RegisteredUserService;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author patrik
 */
public class RegisteredUserControllerTest extends BaseControllerTestRunner {

    @Mock
    private RegisteredUserService userService;

    @InjectMocks
    private RegisteredUserController sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(sut);
    }

    @Test
    public void getByEmailRegisteredUserThrowUnknownRegisteredUser() throws Exception {
        //treba vyriestit bodku z "test@test.com" - to nefunguje
        final String email = "test@testcom";
        final MvcResult mvcResult = mockMvc.perform(get("/rest/registeredusers/search/" + email)).andExpect(status().isNotFound()).andReturn();

        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("RegisteredUser identified by "));
        assertThat(result.getMessage(), containsString(email));
    }

    @Test
    public void getByUsernameRegisteredUserThrowUnknownRegisteredUser() throws Exception {
        final String username = "testusername";
        final MvcResult mvcResult = mockMvc.perform(get("/rest/registeredusers/search/" + username)).andExpect(status().isNotFound()).andReturn();

        final ErrorInfo result = readValue(mvcResult, ErrorInfo.class);
        assertNotNull(result);
        assertThat(result.getMessage(), containsString("RegisteredUser identified by "));
        assertThat(result.getMessage(), containsString(username));
    }

}
