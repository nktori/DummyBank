package nktori.dummybank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Account {

    @Id
    @SequenceGenerator(name = "Account_id_seq", sequenceName = "Account_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Account_id_seq")
    private Integer id;
    private Integer balance;

    public Account() {
    }

    public Account(Integer id, Integer balance) {
        this.id = id;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBalance() {
        return balance;
    }
}
