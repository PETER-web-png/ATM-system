package ATMsystem;


import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class ATM {
    //用集合存储所有账户对象
    private ArrayList<Account> accounts = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    Account loginAcc=new Account();

    public void start() {
        while (true) {
            System.out.println("==欢迎进入ATM系统==");
            System.out.println("1、用户登录");
            System.out.println("2、用户开户");
            System.out.println("请输入：");


            int command = sc.nextInt();
            switch (command) {
                case 1:
                    //用户登录
                    login();
                    break;
                case 2:
                    //用户开户
                    createAccount();
                    break;
                default:
                    System.out.println("没有该操作~~");
                    break;


        }
        }
    }
    private void login(){
        System.out.println("==系统登录==");
        if(accounts.size()==0){
            System.out.println("当前还没有账户，请先开户！");
            return;
        }
        while (true) {
            System.out.println("请输入您的登录卡号：");
            String cardId=sc.next();
            Account acc=getAccountByCardId(cardId);
            if(acc==null){
                System.out.println("您输入的登录卡号不存在，请确认~~");
            }else{
                while (true) {
                    System.out.println("请输入您的登录密码：");
                    String passWord=sc.next();
                    if(passWord.equals(acc.getPassWord())){
                        loginAcc=acc;
                        System.out.println("恭喜您"+acc.getUserName()+"成功登录系统，卡号为："+acc.getCardId());
                        showUserCommand();
                        return; //跳出并结束登录方法，返回进入系统
                    } else{
                        System.out.println("您输入的密码不正确，请确认~~");
                    }
                }
            }
        }
    }
    private void showUserCommand(){
        while (true) {
            System.out.println(loginAcc.getUserName()+"您可以选择如下操作处理账户");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、密码修改");
            System.out.println("6、退出");
            System.out.println("7、注销当前账户");
            System.out.println("请选择:");
            int command=sc.nextInt();
            switch(command){
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    updatePassword();
                    return;
                case 6:
                    //退出当前账户
                    System.out.println(loginAcc.getUserName()+"您退出账户成功");
                    return; //结束方法
                case 7:
                    if(deleteAccount()){
                        //返回欢迎界面
                        return;
                    }
                    break;
                default:
                    System.out.println("您当前选择的操作不存在，请确认~~~");
                    break;
            }
        }
    }

    private void updatePassword() {
        System.out.println("==账户密码修改操作==");
        while(true){
            System.out.println("请输入当前账户密码");

            String passWord=sc.next();
            /*if(loginAcc.getPassWord().equals(passWord)){
              用这个先判断
              如果不正确else直接结束return
            }*/
            while (true) {
                if(loginAcc.getPassWord().equals(passWord)){
                    System.out.println("请输入新账户的密码");
                    String newPassWord=sc.next();
                    System.out.println("请再次输入新账户的密码");
                    String okPassWord=sc.next();
                    if(newPassWord.equals(okPassWord)){
                        loginAcc.setPassWord(okPassWord);
                        System.out.println("您的密码修改成功~~");
                        return;
                    } else{
                        System.out.println("您输入的两次密码不一致，请重新输入~~~");
                    }

                } else{
                    System.out.println("你输入的密码不正确，请再次输入~~");
                    break;
                }
            }
        }
    }

    private boolean deleteAccount() {
        System.out.println("==销户操作==");
        System.out.println("请您确认销户吗? y/n");
        String command=sc.next();
        switch(command){
            case "y":
             if(loginAcc.getMoney()==0){
                 accounts.remove(loginAcc);
                 System.out.println("您的账户已成功销户~~");
                 return true;
             } else{
                 System.out.println("您的账户还有余额，不允许销户操作~~");
                 return false;
             }
            default:
                System.out.println("好的，您的账户保留~~");
                return false;
        }
    }

    private void transferMoney() {
        sc.nextLine();   //吃掉nextInt()的换行符
        System.out.println("==用户转账==");
        if(accounts.size()<2){
            System.out.println("当前系统中只有一个账户，无法为其他用户转账~~");
            return ;
        }
        if(loginAcc.getMoney()==0){
            System.out.println("您自己都没钱，还是别给别人转账了吧");
            return ;
        }
        while(true) {
            System.out.println("请输入转账的卡号");
            String cardId=sc.nextLine();
            Account acc=getAccountByCardId(cardId);
            if(acc == null){
                System.out.println("您输入的卡号不存在，请重新输入~~");
                continue;          //输入错误接着再次输入
            }else if (acc.getCardId().equals(loginAcc.getCardId()))
            {
                System.out.println("不能向自己的账户转账，请重新输入~~");
                continue;
            }else
            {

                while (true) {
                 //   String name="*"+ acc.getUserName().substring(1);
                    String name = acc.getUserName().length() > 1 ? "*" + acc.getUserName().substring(1) : "*";
                    System.out.println("请您输入"+name+"的姓");
                    String preName=sc.next().trim();
                    String firstName = acc.getUserName().substring(0, 1);
                    if(firstName.equals(preName)) {

                        while (true) {
                            System.out.println("请输入转账的金额");
                            double money = sc.nextDouble();
                            if (money <= 0) {
                                System.out.println("转账金额必须大于0，请重新输入");
                                continue;
                            }

                            if (loginAcc.getMoney() >= money) {

                                loginAcc.setMoney(loginAcc.getMoney() - money);

                                acc.setMoney(acc.getMoney() + money);
                                System.out.println("转账成功，您的账户余额为" + loginAcc.getMoney() + "元");
                                return;    //退出转账方法
                            } else {
                                System.out.println("您的余额不足，最多可转" + loginAcc.getMoney() + "元");
                            }
                        }
                    }else{
                        System.out.println("您输入的不正确，请重新输入");
                        continue;
                    }
                }
            }
        }
    }

    private void drawMoney() {
        if(loginAcc.getMoney()<100){
            System.out.println("您当前账户余额不足100元，不允许取钱~~");
            return;
        }
        while (true) {
            System.out.println("请输入您的取款金额：");
            double money=sc.nextDouble();

            if(loginAcc.getMoney()<money){
                System.out.println("余额不足"+"您当前余额为："+loginAcc.getMoney());
            }else{
                if(money>loginAcc.getLimit()){
                    System.out.println("您当前取款超出限额"+"您的取款限额是："+loginAcc.getLimit());
                } else{
                    loginAcc.setMoney(loginAcc.getMoney()-money);
                    System.out.println("您当前取款"+money+"成功"+"账户余额为："+loginAcc.getMoney());
                    break;
                }
            }
        }
    }

    private void depositMoney() {
        System.out.println("==存钱操作==");
        System.out.println("请您输入存款金额：");
        double money=sc.nextDouble();
        loginAcc.setMoney(loginAcc.getMoney()+money);
        System.out.println("恭喜您存款："+money+"当前账户余额为："+loginAcc.getMoney());
    }

    private void showLoginAccount(){
        System.out.println("==您当前的账户信息如下==");
        System.out.println("卡号："+loginAcc.getCardId());
        System.out.println("户主："+loginAcc.getUserName());
        System.out.println("性别："+loginAcc.getSex());
        System.out.println("余额："+loginAcc.getMoney());
        System.out.println("每次取现额度："+loginAcc.getLimit());
    }
    private void createAccount() {
        System.out.println("==系统开户操作==");

        Account acc = new Account();
        System.out.println("请输入您的账户名称：");
        String name = sc.next();
        acc.setUserName(name);

        while (true) {
            System.out.println("请输入您的性别：");
            char sex = sc.next().charAt(0);
            if (sex == '男' || sex == '女') {
                acc.setSex(sex);
                break;
            } else {
                System.out.println("您输入的性别有误，只能是男或女");
            }

        }
        while (true) {
            System.out.println("请输入您的账户密码：");
            String passWord = sc.next();
            System.out.println("请再次输入您的账户密码：");
            String okPassWord = sc.next();
            if (passWord.equals(okPassWord)) {
                acc.setPassWord(passWord);
                break;
            } else {
                System.out.println("您输入的密码不对，请再次输入");
            }
        }

        System.out.println("请输入您的取现额度：");
        double limit = sc.nextDouble();
        acc.setLimit(limit);
        //设置随机卡号，重点
        String newCardId=createCardId();
        acc.setCardId(newCardId);

       accounts.add(acc);
        System.out.println("恭喜您!"+"姓名为；"+ acc.getUserName()+"的用户创建好了");
        System.out.println("开户卡号是："+acc.getCardId());
    }

    private String createCardId(){
        while (true) {
            String cardId="";
            Random r=new Random();
            for (int i = 0; i < 8; i++) {
                int data=r.nextInt(10);
                cardId+=data;
            }
            Account acc=getAccountByCardId(cardId);
            if(acc==null){
                return cardId;
            }
        }
    }
    //根据卡号查找账户对象
    private Account getAccountByCardId(String cardId){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc=accounts.get(i);
            if(acc.getCardId().equals(cardId)){
                return acc;
            }
        }
        return null;
    }

}


