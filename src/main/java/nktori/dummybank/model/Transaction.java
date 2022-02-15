package nktori.dummybank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @SequenceGenerator(name = "Transaction_id_seq", sequenceName = "Transaction_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Transaction_id_seq")
    private Integer id;
    private Integer payeeId;
    private Integer payerId;
    private Integer amount;
    private LocalDateTime timestamp;

    public Transaction() {
    }

    public Transaction(Integer payeeId, Integer payerId, Integer amount, LocalDateTime timestamp) {
        this.payeeId = payeeId;
        this.payerId = payerId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPayerId() {
        return payerId;
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public Integer getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
