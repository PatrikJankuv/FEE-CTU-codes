/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.service;

import cz.cvut.fel.ear.meetingscheduler.dao.GroupUserDao;
import cz.cvut.fel.ear.meetingscheduler.dao.RegisteredUserDao;
import cz.cvut.fel.ear.meetingscheduler.model.GroupUser;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.Role;
import cz.cvut.fel.ear.meetingscheduler.security.SecurityUtils;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author patrik
 */
@Service
public class GroupUserService {

    private final GroupUserDao dao;

    private final RegisteredUserDao userDao;

    @Autowired
    public GroupUserService(GroupUserDao dao, RegisteredUserDao usersService) {
        this.dao = dao;
        this.userDao = usersService;
    }

    @Transactional(readOnly = true)
    public List<GroupUser> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public GroupUser find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(GroupUser group) {
        Objects.requireNonNull(group);
        dao.persist(group);
    }

    /**
     * Need one user, which will be set as creator
     *
     * @param user Creator of GroupUser
     * @param name
     * @param description
     * @return new GroupUser
     */
    @Transactional
    public GroupUser createNewGroupByUser(RegisteredUser user, String name, String description) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
        final GroupUser group = new GroupUser();
        group.setDescription(description);
        group.setName(name);
        group.addUser(user, Role.CREATOR);
        dao.persist(group);
        return group;
    }

    /**
     * Need one user, which will be set as creator
     *
     * @return new GroupUser
     */
    @Transactional
    public GroupUser createNewGroup() {
        final GroupUser group = new GroupUser();
        final RegisteredUser creator = SecurityUtils.getCurrentUser();

        group.addUser(creator, Role.CREATOR);
        dao.persist(group);
        return group;
    }

    @Transactional
    @PreAuthorize("this.userCanRemoveGroup(principal.username, #group)")
    public void removeGroup(GroupUser group) {
        Objects.requireNonNull(group);
        dao.remove(group);
    }

    /**
     * Add a new user to group. A new user can be added only by the user, which
     * is a member of the group and has the role of EDITOR or CREATOR
     *
     * @param group GroupUser
     * @param user New user
     * @param role Role of the new user in the group
     */
    @Transactional
    @PreAuthorize("this.userCanEditGroup(principal.username, #group)")
    public void addUserToGroup(GroupUser group, RegisteredUser user, Role role) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);
        group.addUser(user, role);
        dao.update(group);
    }

    /**
     * Add a new user to group. A new user can be added only by the user, which
     * is a member of the group and has the role of EDITOR or CREATOR
     *
     * @param group GroupUser
     * @param user New user
     */
    @Transactional
    @PreAuthorize("this.userCanEditGroup(principal.username, #group)")
    public void addUserToGroupAsMember(GroupUser group, RegisteredUser user) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(user);
        group.addUser(user, Role.MEMBER);
        dao.update(group);
    }

    /**
     * Remove a user from group
     *
     * @param group GroupUser
     * @param user User which is remove
     */
    @Transactional
    @PreAuthorize("this.userCanEditGroup(principal.username, #group)")
    public void removeUserFromGroup(GroupUser group, RegisteredUser user) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(user);
        group.removeUser(user);
        dao.update(group);
    }

    public boolean userCanEditGroup(String username, GroupUser group) {
        final RegisteredUser user = userDao.findByString(username);
        Objects.requireNonNull(user);
        Role role = group.getUsersMap().get(user);
        return role == Role.CREATOR || role == Role.EDITOR;
    }

    public boolean userCanRemoveGroup(String username, GroupUser group) {
        final RegisteredUser user = userDao.findByString(username);
        Objects.requireNonNull(user);
        Role role = group.getUsersMap().get(user);
        return role == Role.CREATOR;
    }
}
