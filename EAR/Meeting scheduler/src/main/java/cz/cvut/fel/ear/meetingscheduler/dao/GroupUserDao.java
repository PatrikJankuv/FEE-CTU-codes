/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.GroupUser;
import org.springframework.stereotype.Repository;

/**
 *
 * @author patrik
 */
@Repository
public class GroupUserDao extends BaseDao<GroupUser> {

    public GroupUserDao() {
        super(GroupUser.class);
    }

}
