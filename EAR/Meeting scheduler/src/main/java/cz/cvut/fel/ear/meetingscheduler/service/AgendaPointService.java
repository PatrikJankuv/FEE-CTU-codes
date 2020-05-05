/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.AgendaPointDao;
import cz.cvut.fel.ear.meetingscheduler.model.AgendaPoint;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@Service
public class AgendaPointService {

    private final AgendaPointDao dao;

    @Autowired
    public AgendaPointService(AgendaPointDao pointDao) {
        this.dao = pointDao;
    }

    @Transactional(readOnly = true)
    public AgendaPoint find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(AgendaPoint point) {
        Objects.requireNonNull(point);
        dao.persist(point);
    }

    @Transactional
    public void remove(AgendaPoint point) {
        Objects.requireNonNull(point);
        dao.remove(point);
    }

}
