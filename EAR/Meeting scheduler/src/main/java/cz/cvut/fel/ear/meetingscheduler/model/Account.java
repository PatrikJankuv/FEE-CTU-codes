package cz.cvut.fel.ear.meetingscheduler.model;

import javax.persistence.*;

/**
 * @author Matej
 * @version 1.0
 * @created 13-lis-2019 16:20:38
 */
@Entity
public class Account extends AbstractEntity {

    public Account() {

    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj.getClass() == this.getClass()
                && obj.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

}
