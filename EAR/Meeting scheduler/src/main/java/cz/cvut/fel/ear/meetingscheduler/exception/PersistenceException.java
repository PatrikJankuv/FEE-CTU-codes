/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.exception;

/**
 *
 * @author Matej
 */
public class PersistenceException extends RuntimeException {

    public PersistenceException() {
    }

    public PersistenceException(RuntimeException e) {
        this(e.getMessage(), e.getCause());
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

}
