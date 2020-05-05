/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.model.AgendaPoint;
import cz.cvut.fel.ear.meetingscheduler.service.AgendaPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Matej
 */
@RestController
@RequestMapping("/rest/point")
public class AgendaPointController {

    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUserController.class);

    private final AgendaPointService service;

    @Autowired
    public AgendaPointController(AgendaPointService service) {
        this.service = service;
    }

    /**
     *
     * @param id of the UserGroup we want to get the details of
     * @return UserGroup of given id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AgendaPoint getAgendaPointById(@PathVariable Integer id) {
        final AgendaPoint Point = service.find(id);
        if (Point == null) {
            throw NotFoundException.create("Agendapoint", id);
        } else {
            return Point;
        }
    }

}
