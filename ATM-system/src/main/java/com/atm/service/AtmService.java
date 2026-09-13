package com.atm.service;

import com.atm.entity.Account;
import com.atm.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AtmService {

    @Autowired
    private AccountRepository accountRepository;

    public Account login(String cardId, String password) {
        Optional<Account> account = accountRepository.findByCardIdAndPassWord(cardId, password);
        return account.orElse(null);
    }

    @Transactional
    public Account createAccount(String userName, Character sex, String password, Double limit) {
        String cardId = generateCardId();
        Account account = new Account(cardId, userName, sex, password, 0.0, limit);
        return accountRepository.save(account);
    }

    public Account getAccountByCardId(String cardId) {
        return accountRepository.findById(cardId).orElse(null);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public boolean deposit(String cardId, Double amount) {
        if (amount <= 0) return false;
        Account account = accountRepository.findById(cardId).orElse(null);
        if (account == null) return false;
        account.setMoney(account.getMoney() + amount);
        accountRepository.save(account);
        return true;
    }

    @Transactional
    public String withdraw(String cardId, Double amount) {
        Account account = accountRepository.findById(cardId).orElse(null);
        if (account == null) return "账户不存在";
        if (account.getMoney() < 100) return "余额不足100元，不允许取款";
        if (amount <= 0) return "取款金额必须大于0";
        if (account.getMoney() < amount) return "余额不足，当前余额：" + account.getMoney() + "元";
        if (amount > account.getLimit()) return "超出取款限额，限额为：" + account.getLimit() + "元";
        
        account.setMoney(account.getMoney() - amount);
        accountRepository.save(account);
        return "success";
    }

    @Transactional
    public String transfer(String fromCardId, String toCardId, String recipientName, Double amount) {
        Account sender = accountRepository.findById(fromCardId).orElse(null);
        Account recipient = accountRepository.findById(toCardId).orElse(null);
        
        if (sender == null) return "转出账户不存在";
        if (recipient == null) return "转入账户不存在";
        if (fromCardId.equals(toCardId)) return "不能给自己转账";
        if (sender.getMoney() <= 0) return "您的账户没有余额";
        if (amount <= 0) return "转账金额必须大于0";
        if (sender.getMoney() < amount) return "余额不足，当前余额：" + sender.getMoney() + "元";
        
        String maskedName = "*" + recipient.getUserName().substring(1);
        String expectedName = maskedName.substring(0, 1) + recipient.getUserName().substring(1);
        if (!recipient.getUserName().equals(recipientName) && !recipient.getDisplayName().equals(recipientName)) {
            return "收款人姓名验证失败";
        }
        
        sender.setMoney(sender.getMoney() - amount);
        recipient.setMoney(recipient.getMoney() + amount);
        accountRepository.save(sender);
        accountRepository.save(recipient);
        return "success";
    }

    @Transactional
    public String changePassword(String cardId, String oldPassword, String newPassword) {
        Account account = accountRepository.findById(cardId).orElse(null);
        if (account == null) return "账户不存在";
        if (!account.getPassWord().equals(oldPassword)) return "原密码错误";
        
        account.setPassWord(newPassword);
        accountRepository.save(account);
        return "success";
    }

    @Transactional
    public String deleteAccount(String cardId) {
        Account account = accountRepository.findById(cardId).orElse(null);
        if (account == null) return "账户不存在";
        if (account.getMoney() > 0) return "账户还有余额，不允许销户";
        
        accountRepository.delete(account);
        return "success";
    }

    private String generateCardId() {
        Random random = new Random();
        while (true) {
            StringBuilder cardId = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                cardId.append(random.nextInt(10));
            }
            if (!accountRepository.existsByCardId(cardId.toString())) {
                return cardId.toString();
            }
        }
    }
}
