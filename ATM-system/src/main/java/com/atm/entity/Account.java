package com.atm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(length = 8)
    @Pattern(regexp = "^\\d{8}$", message = "卡号必须是8位数字")
    private String cardId;

    @NotBlank(message = "用户名不能为空")
    @Column(nullable = false)
    private String userName;

    @NotNull(message = "性别不能为空")
    @Column(nullable = false)
    private Character sex;

    @NotBlank(message = "密码不能为空")
    @Column(nullable = false)
    private String passWord;

    @Column(nullable = false)
    private Double money = 0.0;

    @NotNull(message = "取现额度不能为空")
    @Column(name = "withdraw_limit", nullable = false)
    private Double limit;

    public Account() {
    }

    public Account(String cardId, String userName, Character sex, String passWord, Double money, Double limit) {
        this.cardId = cardId;
        this.userName = userName;
        this.sex = sex;
        this.passWord = passWord;
        this.money = money;
        this.limit = limit;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return userName + (sex == '男' ? "先生" : "女士");
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }
}
