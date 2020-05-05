/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author patrik
 */
public class MeetingModelTest {

    @Test
    public void removeChoiceTest() {
        final Choice choice1 = new Choice();
        final Choice choice2 = new Choice();
        final Meeting meeting = new Meeting();
        meeting.addChoice(choice2);
        meeting.addChoice(choice1);

        assertEquals(2, meeting.getChoices().size());

        meeting.removeChoiceFromChoices(choice2);
        assertEquals(1, meeting.getChoices().size());
    }
}
