/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.ChoiceDao;
import cz.cvut.fel.ear.meetingscheduler.model.Choice;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@Service
public class ChoiceService {

    private final ChoiceDao dao;

    @Autowired
    public ChoiceService(ChoiceDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public Choice find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Choice choice) {
        Objects.requireNonNull(choice);
        dao.persist(choice);
    }
}
