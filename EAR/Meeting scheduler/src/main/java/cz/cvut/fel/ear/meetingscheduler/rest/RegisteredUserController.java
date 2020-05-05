/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.exception.PersistenceException;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.rest.util.RestUtils;
import cz.cvut.fel.ear.meetingscheduler.security.DefaultAuthenticationProvider;
import cz.cvut.fel.ear.meetingscheduler.service.RegisteredUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author patrik
 */
@RestController
@RequestMapping("/rest/registeredusers")
public class RegisteredUserController {

    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUserController.class);

    private final RegisteredUserService userService;

    private DefaultAuthenticationProvider provider;

    @Autowired
    public RegisteredUserController(RegisteredUserService userService, DefaultAuthenticationProvider provider) {
        this.userService = userService;
        this.provider = provider;
    }

    /**
     * Generate RegisterUser from inserted parameters
     *
     * @param email
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerNewUser(@RequestBody RegisteredUser user) {
        HttpHeaders headers;
        try {
            RegisteredUser newUser = userService.createRegisteredUserFromParameteres(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getFirstname(),
                    user.getLastname());
            headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", newUser.getId());
        } catch (PersistenceException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // user with same email or username exists
        }
        LOG.debug("User successfully registered");

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Login user into system
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@RequestBody RegisteredUser u) {
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");

        final Authentication auth = new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword());

        try {
            provider.authenticate(auth);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(headers, HttpStatus.NOT_ACCEPTABLE);
        }

        LOG.debug("User successfully login");

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping(value = "/search/{str}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegisteredUser getByString(@PathVariable String str) {
        final RegisteredUser regUser = userService.findByString(str);
        if (regUser == null) {
            throw NotFoundException.create("RegisteredUser", str);
        }
        return regUser;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegisteredUser getRegisteredUserById(@PathVariable Integer id) {
        final RegisteredUser user = userService.findById(id);
        if (user == null) {
            throw NotFoundException.create("RegisteredUser", id);
        } else {
            return user;
        }
    }

}
