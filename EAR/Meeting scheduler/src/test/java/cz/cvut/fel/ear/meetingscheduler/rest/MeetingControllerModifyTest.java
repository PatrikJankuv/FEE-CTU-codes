/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.model.DateLocation;
import cz.cvut.fel.ear.meetingscheduler.model.Meeting;
import cz.cvut.fel.ear.meetingscheduler.service.MeetingService;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author patrik
 */
public class MeetingControllerModifyTest extends BaseControllerTestRunner {

    @Mock
    private MeetingService meetingServiceMock;

    @InjectMocks
    private MeetingController sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(sut);
    }

    @Test
    public void addDateLocationToMeeting() throws Exception {
        final Meeting newMeeting = new Meeting();
        newMeeting.setId(8);
        newMeeting.setDescription("test description");
        when(meetingServiceMock.find(any())).thenReturn(newMeeting);

        final DateLocation dt = new DateLocation();
        dt.setId(12);
        dt.setDatetime(new Date(12, 12, 2020));
        dt.setLocation("Giraltovce");

        mockMvc.perform(post("/rest/meetings/" + newMeeting.getId() + "/datelocations").content(toJson(dt)).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNoContent());

        final ArgumentCaptor<DateLocation> captor = ArgumentCaptor.forClass(DateLocation.class);
        verify(meetingServiceMock).addDateLocationToMeeting(eq(newMeeting), captor.capture());
        assertEquals(dt.getId(), captor.getValue().getId());
    }

    @Test
    public void removeDateLocationFromMeeting() throws Exception {
        final Meeting newMeeting = new Meeting();
        newMeeting.setId(8);
        newMeeting.setDescription("test description");
        when(meetingServiceMock.find(any())).thenReturn(newMeeting);

        final DateLocation dateLoc = new DateLocation();
        dateLoc.setId(12);
        dateLoc.setDatetime(new Date(12, 12, 2020));
        dateLoc.setLocation("Giraltovce");

        mockMvc.perform(post("/rest/meetings/" + newMeeting.getId() + "/datelocations").content(toJson(dateLoc)).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNoContent());

        final ArgumentCaptor<DateLocation> captor = ArgumentCaptor.forClass(DateLocation.class);
        verify(meetingServiceMock).addDateLocationToMeeting(eq(newMeeting), captor.capture());

        assertEquals(dateLoc.getId(), captor.getValue().getId());

        mockMvc.perform(delete("/rest/meetings/" + newMeeting.getId() + "/datelocations/" + dateLoc.getId()).content(toJson(dateLoc)).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNoContent());
    }
}
