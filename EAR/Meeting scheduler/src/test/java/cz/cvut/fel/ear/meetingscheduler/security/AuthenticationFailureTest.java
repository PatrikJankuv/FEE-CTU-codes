/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.ear.meetingscheduler.config.AppConfig;
import cz.cvut.fel.ear.meetingscheduler.security.model.LoginStatus;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author patrik
 */
public class AuthenticationFailureTest {

    private ObjectMapper mapper = new AppConfig().objectMapper();

    private AuthenticationFailure sut;

    @Before
    public void setUp() {
        this.sut = new AuthenticationFailure(mapper);
    }

    @Test
    public void authenticationFailureReturnsLoginStatusWithErrorInfo() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final String msg = "Username not found";
        final AuthenticationException e = new UsernameNotFoundException(msg);
        sut.onAuthenticationFailure(request, response, e);
        final LoginStatus status = mapper.readValue(response.getContentAsString(), LoginStatus.class);
        assertFalse(status.isSuccess());
        assertFalse(status.isLoggedIn());
        assertNull(status.getUsername());
        assertEquals(msg, status.getErrorMessage());
    }
}
