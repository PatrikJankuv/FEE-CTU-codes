/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.AgendaDao;
import cz.cvut.fel.ear.meetingscheduler.dao.AgendaPointDao;
import cz.cvut.fel.ear.meetingscheduler.model.Agenda;
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
public class AgendaService {

    private final AgendaDao dao;

    private final AgendaPointDao pointDao;

    @Autowired
    public AgendaService(AgendaDao dao, AgendaPointDao pointDao) {
        this.dao = dao;
        this.pointDao = pointDao;
    }

    @Transactional(readOnly = true)
    public Agenda find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void addAgendaPoint(Agenda agenda, AgendaPoint agendaPoint) {
        Objects.requireNonNull(agenda);
        Objects.requireNonNull(agendaPoint);
        pointDao.persist(agendaPoint);
        agenda.addAgendaPoint(agendaPoint);

        dao.update(agenda);
    }

    @Transactional
    public void removeAgendaPoint(Agenda agenda, AgendaPoint agendaPoint) {
        Objects.requireNonNull(agenda);
        Objects.requireNonNull(agendaPoint);
        agenda.removeAgendaPoint(agendaPoint);
        pointDao.remove(agendaPoint);
        dao.update(agenda);
    }
}
