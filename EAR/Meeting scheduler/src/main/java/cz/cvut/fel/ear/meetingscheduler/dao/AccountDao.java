package cz.cvut.fel.ear.meetingscheduler.dao;

import cz.cvut.fel.ear.meetingscheduler.model.Account;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao extends BaseDao<Account> {

    public AccountDao() {
        super(Account.class);
    }
}
