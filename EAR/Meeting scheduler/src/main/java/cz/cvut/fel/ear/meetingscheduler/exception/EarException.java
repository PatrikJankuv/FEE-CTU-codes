/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.exception;

/**
 *
 * @author patrik
 */
public class EarException extends RuntimeException {

    public EarException() {
    }

    public EarException(String message) {
        super(message);
    }

    public EarException(String message, Throwable cause) {
        super(message, cause);
    }

    public EarException(Throwable cause) {
        super(cause);
    }
}
