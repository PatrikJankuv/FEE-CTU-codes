/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.model.UnregisteredUser;
import cz.cvut.fel.ear.meetingscheduler.rest.util.RestUtils;
import cz.cvut.fel.ear.meetingscheduler.service.UnregisteredUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author patrik
 */
@RestController
@RequestMapping("/rest/unregistereduser")
public class UnregisteredUserController {

    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUserController.class);

    private final UnregisteredUserService userService;

    @Autowired
    public UnregisteredUserController(UnregisteredUserService userService) {
        this.userService = userService;
    }

    /**
     * Create unregistered user, from email
     *
     * @param email
     * @return
     */
    @PostMapping(value = "/new/{email}")
    public ResponseEntity<Void> registerNewUser(@PathVariable String email) {
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        if (userService.findByEmail(email) == null) {
            try {
                UnregisteredUser user = userService.createFromParameters(email);
            } catch (Exception e) {
                return new ResponseEntity<>(headers, HttpStatus.NOT_ACCEPTABLE);
            }

            LOG.debug("User successfully registered");

            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } else {
            LOG.debug("User already exits");

            return new ResponseEntity<>(headers, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/search/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnregisteredUser getByEmail(@PathVariable String email) {
        final UnregisteredUser regUser = userService.findByEmail(email);
        if (regUser == null) {
            throw NotFoundException.create("RegisteredUser", email);
        }
        return regUser;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UnregisteredUser getRegisteredUserById(@PathVariable Integer id) {
        final UnregisteredUser user = userService.find(id);
        if (user == null) {
            throw NotFoundException.create("RegisteredUser", id);
        } else {
            return user;
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUnregisteredUserFromDB(@PathVariable Integer id) {
        final UnregisteredUser user = userService.find(id);
        userService.remove(user);

        LOG.debug("Removed unregistereduser {}", user);

    }
}
