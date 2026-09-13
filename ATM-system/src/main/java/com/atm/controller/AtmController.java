package com.atm.controller;

import com.atm.entity.Account;
import com.atm.service.AtmService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AtmController {

    @Autowired
    private AtmService atmService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String cardId,
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        Account account = atmService.login(cardId, password);
        if (account != null) {
            session.setAttribute("loginAccount", account);
            return "redirect:/main";
        }
        redirectAttributes.addFlashAttribute("error", "卡号或密码错误");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String userName,
                          @RequestParam Character sex,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          @RequestParam Double limit,
                          RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "两次输入的密码不一致");
            return "redirect:/register";
        }
        
        Account account = atmService.createAccount(userName, sex, password, limit);
        redirectAttributes.addFlashAttribute("success", "开户成功！您的卡号是：" + account.getCardId());
        return "redirect:/login";
    }

    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", account);
        return "main";
    }

    @GetMapping("/account")
    public String accountPage(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", account);
        return "account";
    }

    @GetMapping("/deposit")
    public String depositPage(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", account);
        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam Double amount,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        
        if (atmService.deposit(account.getCardId(), amount)) {
            account.setMoney(account.getMoney() + amount);
            session.setAttribute("loginAccount", account);
            redirectAttributes.addFlashAttribute("success", "存款成功！当前余额：" + account.getMoney() + "元");
        } else {
            redirectAttributes.addFlashAttribute("error", "存款失败，请输入有效金额");
        }
        return "redirect:/deposit";
    }

    @GetMapping("/withdraw")
    public String withdrawPage(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", account);
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Double amount,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        
        String result = atmService.withdraw(account.getCardId(), amount);
        if ("success".equals(result)) {
            account.setMoney(account.getMoney() - amount);
            session.setAttribute("loginAccount", account);
            redirectAttributes.addFlashAttribute("success", "取款成功！当前余额：" + account.getMoney() + "元");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/withdraw";
    }

    @GetMapping("/transfer")
    public String transferPage(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", account);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String toCardId,
                          @RequestParam String recipientName,
                          @RequestParam Double amount,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        
        String result = atmService.transfer(account.getCardId(), toCardId, recipientName, amount);
        if ("success".equals(result)) {
            account.setMoney(account.getMoney() - amount);
            session.setAttribute("loginAccount", account);
            redirectAttributes.addFlashAttribute("success", "转账成功！当前余额：" + account.getMoney() + "元");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/transfer";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", account);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "两次输入的新密码不一致");
            return "redirect:/change-password";
        }
        
        String result = atmService.changePassword(account.getCardId(), oldPassword, newPassword);
        if ("success".equals(result)) {
            account.setPassWord(newPassword);
            session.setAttribute("loginAccount", account);
            redirectAttributes.addFlashAttribute("success", "密码修改成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/change-password";
    }

    @PostMapping("/delete")
    public String deleteAccount(HttpSession session, RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return "redirect:/login";
        }
        
        String result = atmService.deleteAccount(account.getCardId());
        if ("success".equals(result)) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("success", "账户已成功注销");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", result);
            return "redirect:/main";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
