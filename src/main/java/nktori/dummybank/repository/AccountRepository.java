package nktori.dummybank.repository;

import nktori.dummybank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + ?2 WHERE a.id = ?1")
    void updateAccountBalanceById(Integer id, Integer amount);
}
