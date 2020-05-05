/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.Message;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.service.MessageService;
import cz.cvut.fel.ear.meetingscheduler.service.RegisteredUserService;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 *
 * @author patrik
 */
public class MessageControllerTest extends BaseControllerTestRunner {

    @Mock
    private MessageService messageServiceMock;

    @Mock
    private RegisteredUserService regServiceMock;

    @InjectMocks
    private MessageController sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(sut);
    }

    @Test
    public void getConversationTest() throws Exception {
        final RegisteredUser user1 = Generator.generateUser1();
        final RegisteredUser user2 = Generator.generateUser2();

        final List<Message> messages = IntStream.range(0, 5).mapToObj(i -> generateMessage(user1, user2)).collect(
                Collectors.toList());

        when(regServiceMock.findById(user1.getId())).thenReturn(user1);
        when(regServiceMock.findById(user2.getId())).thenReturn(user2);
        when(messageServiceMock.findConversation(user1, user2)).thenReturn(messages);
        final MvcResult mvcResult = mockMvc.perform(get("/rest/message/" + user1.getId() + "/" + user2.getId()).content(toJson(user1)).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(user2)).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertNotNull(mvcResult);

        final List<Message> result = readValue(mvcResult, new TypeReference<List<Message>>() {
        });

        assertNotNull(result);
        assertEquals(messages.size(), result.size());
        for (int i = 0; i < messages.size(); i++) {
            assertEquals(messages.get(i).getBody(), result.get(i).getBody());
            assertEquals(messages.get(i).getRecipient().getId(), result.get(i).getRecipient().getId());
            assertEquals(messages.get(i).getSender().getId(), result.get(i).getSender().getId());
        }
    }

    private Message generateMessage(RegisteredUser user1, RegisteredUser user2) {
        Random rand = new Random();
        //choose random message body
        String[] messageBody = {"hello", "hi", "whats up", "Ok", "lol"};
        String body = messageBody[rand.nextInt(5)];

        //choose random who is recipient and sender
        int i = rand.nextInt(2);
        if (i == 0) {
            return new Message(body, "message", user1, user2);
        }
        return new Message(body, "message", user2, user1);
    }
}
