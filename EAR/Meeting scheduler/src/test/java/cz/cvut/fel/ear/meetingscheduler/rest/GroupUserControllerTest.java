/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.rest;

import cz.cvut.fel.ear.meetingscheduler.environment.Generator;
import cz.cvut.fel.ear.meetingscheduler.model.GroupUser;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.service.GroupUserService;
import cz.cvut.fel.ear.meetingscheduler.service.RegisteredUserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author patrik
 */
public class GroupUserControllerTest extends BaseControllerTestRunner {

    @Mock
    private GroupUserService groupServiceMock;

    @Mock
    private RegisteredUserService regServiceMock;

    @InjectMocks
    private GroupUserController sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(sut);
    }

    @Test
    public void removeGroup() throws Exception {
        final GroupUser toRemove = new GroupUser();
        toRemove.setName("lol");

        when(groupServiceMock.find(toRemove.getId())).thenReturn(toRemove);
        mockMvc.perform(delete("/rest/group/" + toRemove.getId())).andExpect(status().isNoContent());
        verify(groupServiceMock).removeGroup(toRemove);
    }

    @Test
    public void addUserToGroup() throws Exception {
        final GroupUser group = new GroupUser();
        group.setName("hah");
        group.setId(56);
        final RegisteredUser tUser1 = Generator.generateUser1();

        when(groupServiceMock.find(group.getId())).thenReturn(group);
        when(regServiceMock.findById(tUser1.getId())).thenReturn(tUser1);

        final Role role = Role.MEMBER;

        mockMvc.perform(post("/rest/group/" + group.getId() + "/user/" + tUser1.getId()).content(toJson(role)).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNoContent());

        final ArgumentCaptor<GroupUser> captor = ArgumentCaptor.forClass(GroupUser.class);
        verify(groupServiceMock).addUserToGroup(captor.capture(), eq(tUser1), eq(role));
    }
}
