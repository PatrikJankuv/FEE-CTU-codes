/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import cz.cvut.fel.ear.meetingscheduler.model.Agenda;
import cz.cvut.fel.ear.meetingscheduler.service.AgendaService;
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
 * @author patrik
 */
@RestController
@RequestMapping("/rest/agenda")
public class AgendaController {

    private static final Logger LOG = LoggerFactory.getLogger(RegisteredUserController.class);

    private final AgendaService service;

    @Autowired
    public AgendaController(AgendaService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Agenda getAgendaById(@PathVariable Integer id) {
        final Agenda agenda = service.find(id);
        if (agenda == null) {
            throw NotFoundException.create("RegisteredUser", id);
        } else {
            return agenda;
        }
    }
}
