package com.atm.repository;

import com.atm.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByCardIdAndPassWord(String cardId, String passWord);
    boolean existsByCardId(String cardId);
}
