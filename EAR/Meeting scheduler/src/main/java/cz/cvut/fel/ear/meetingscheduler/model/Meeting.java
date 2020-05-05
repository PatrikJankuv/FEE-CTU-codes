package cz.cvut.fel.ear.meetingscheduler.model;

import cz.cvut.fel.ear.meetingscheduler.exception.NotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class Meeting extends AbstractEntity {

    @Column
    private String description;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<DateLocation> possibleDateLocations;

//    @JoinTable(name="roleformeeting",
//            joinColumns = @JoinColumn(name="meeting_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    @ManyToMany
//    private List<User> users;
    @ManyToMany
    @MapKeyColumn(name = "MEETING_ROLE")
    @ElementCollection
    @CollectionTable(name = "USER_ROLE_MEETING")
    @MapKeyJoinColumn(name = "USER_ID")
    @Column(name = "USER_ROLE")
    private Map<Account, Role> userRoleForMeeting;

    @OneToOne
    private Agenda agenda;

    @Enumerated(EnumType.STRING)
    private PollState pollState;

//    @JoinColumn(name = "meeting_id")???
    @OneToMany
    private List<Choice> choices;

    public Meeting() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DateLocation> getPossibleDateLocations() {
        return possibleDateLocations;
    }

    public void setPossibleDateLocations(List<DateLocation> possibleDateLocations) {
        this.possibleDateLocations = possibleDateLocations;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public PollState getPollState() {
        return pollState;
    }

    public void setPollState(PollState pollState) {
        this.pollState = pollState;
    }

    public List<Choice> getChoices() {
        return this.choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addChoice(Choice choice) {
        Objects.requireNonNull(choice);

        if (choices == null) {
            this.choices = new ArrayList<>();
        }

        choices.add(choice);
    }

    public Map<Account, Role> getUsersMap() {
        return this.userRoleForMeeting;
    }

    public void setUsersMap(Map<Account, Role> users) {
        this.userRoleForMeeting = users;
    }

    /**
     * Add user from userRoleForMeeting map
     *
     * @param user
     * @param role
     */
    public void addUserToMeetingMap(User user, Role role) {
        if (this.userRoleForMeeting == null) {
            this.userRoleForMeeting = new HashMap<>();
        }

        this.userRoleForMeeting.put(user.getAccount(), role);
    }

    /**
     * Remove user from map of user
     *
     * @param user User which have to be remove
     */
    public void removeUserFromMeetingMap(User user) {
        if (this.userRoleForMeeting == null || this.userRoleForMeeting.isEmpty() || user == null) {
            return;
        }
        this.userRoleForMeeting.remove(user.getAccount());
        System.out.println("successfuly removed " + user.getId());
    }

    public boolean isUserInGroup(User user) {
        return this.userRoleForMeeting.containsKey(user.getAccount());
    }

    /**
     * Add dateLocation from posibleDateLocations list
     *
     * @param dl
     */
    public void addDatelocationToPossibleDateLocations(DateLocation dl) {
        if (this.possibleDateLocations == null) {
            this.possibleDateLocations = new ArrayList<>();
        }
        this.possibleDateLocations.add(dl);
    }

    /**
     * Remove dateLocation from list of posibleDateLocations
     *
     * @param dl User which have to be remove
     */
    public void removeDateLocationFromPossibleDateLocation(DateLocation dl) {
        if (this.possibleDateLocations == null || this.possibleDateLocations.isEmpty()) {
            throw new NotFoundException("Datelocation not found!");
        }
        this.possibleDateLocations.remove(dl);
    }

    /**
     * Add choice to choices list
     *
     * @param choice
     */
    public void addChoiceToChoices(Choice choice) {
        if (this.choices == null) {
            this.choices = new ArrayList<>();
        }
        this.choices.add(choice);
    }

    /**
     * Remove choice from list of choices
     *
     * @param choice User which have to be remove
     */
    public void removeChoiceFromChoices(Choice choice) {
        if (this.choices == null || this.choices.isEmpty()) {
            return;
        }
        this.choices.remove(choice);
    }

}
