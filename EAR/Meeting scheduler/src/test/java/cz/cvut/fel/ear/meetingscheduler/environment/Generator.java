/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.ear.meetingscheduler.environment;

import cz.cvut.fel.ear.meetingscheduler.model.Account;
import cz.cvut.fel.ear.meetingscheduler.model.DateLocation;
import cz.cvut.fel.ear.meetingscheduler.model.Meeting;
import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import cz.cvut.fel.ear.meetingscheduler.model.UnregisteredUser;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author patrik
 */
public class Generator {

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    /**
     *
     * @return
     */
    public static Meeting generateMeeting() {
        final Meeting m = new Meeting();
        m.setName("Meeting number " + randomInt());
        m.setDescription("Description " + randomInt());

        return m;
    }

    public static DateLocation generateDateLocation() {
        String[] locations = {"PRAGUE", "BRNO", "PLZEN", "OSTRAVA", "LIBEREC", "OLOMOUC", "MOST", "PARDUBICE"};

        final DateLocation dl = new DateLocation();
        dl.setLocation(locations[RAND.nextInt(8)]);
        dl.setDatetime(new Date(120, RAND.nextInt(12), RAND.nextInt(31), RAND.nextInt(25), RAND.nextInt(2) * 30));

        return dl;
    }

    public static RegisteredUser generateUser1() {
        final RegisteredUser tUser = new RegisteredUser();
        final Account acc = new Account();
        acc.setId(21);
        tUser.setAccount(acc);
        tUser.setUsername("testUsername1");
        tUser.setFirstname("First");
        tUser.setLastname("Last");
        tUser.setEmail("test1@test.com");
        tUser.setId(1);
        tUser.setPassword("Aa123");
        Account account = new Account();
        account.setId(5);
        tUser.setAccount(account);

        return tUser;
    }

    public static RegisteredUser generateUser2() {
        final RegisteredUser tUser2 = new RegisteredUser();
        final Account acc = new Account();
        acc.setId(22);
        tUser2.setAccount(acc);
        tUser2.setUsername("testUsername2");
        tUser2.setFirstname("First");
        tUser2.setLastname("Last");
        tUser2.setEmail("test2@test.com");
        tUser2.setId(2);
        tUser2.setPassword("Aa123");

        return tUser2;
    }

    public static RegisteredUser generateUser3() {
        final RegisteredUser tUser3 = new RegisteredUser();
        final Account acc = new Account();
        acc.setId(23);
        tUser3.setAccount(acc);
        tUser3.setUsername("testUsername3");
        tUser3.setFirstname("First");
        tUser3.setLastname("Last");
        tUser3.setEmail("test3@test.com");
        tUser3.setId(3);
        tUser3.setPassword("Aa123");

        return tUser3;
    }

    public static UnregisteredUser generateUnUser1() {
        final UnregisteredUser tUser3 = new UnregisteredUser();
        final Account acc = new Account();
        acc.setId(24);
        tUser3.setAccount(acc);
        tUser3.setEmail("test4@test.com");
        tUser3.setId(4);

        return tUser3;
    }

    public static DateLocation generateDl() {
        Random rand = new Random();
        final DateLocation dl = new DateLocation();
        dl.setDatetime(new Date(2020, 2, 2, 12, 0, 0));
        dl.setLocation("Praha");
        dl.setId(rand.nextInt(50));

        return dl;
    }

}
