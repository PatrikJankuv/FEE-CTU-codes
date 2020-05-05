/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.DateLocationDao;
import cz.cvut.fel.ear.meetingscheduler.model.DateLocation;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@Service
public class DateLocationService {

    private final DateLocationDao dao;

    @Autowired
    public DateLocationService(DateLocationDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public DateLocation find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(DateLocation dl) {
        Objects.requireNonNull(dl);
        dao.persist(dl);
    }
}
