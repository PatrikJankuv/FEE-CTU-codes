/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.dto;

/**
 *
 * @author Matej
 */
public class MeetingCreationDto {

    private Integer userId; // user id of author of this meeting
    private String description;
    private String name;

    public Integer getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

}
